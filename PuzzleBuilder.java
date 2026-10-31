import java.util.*;

// Builds a random puzzle for "Play" / "New Game".
// 1. Make a full, valid solution grid:
//    - empty board  -> shuffle a known valid pattern (instant for 9x9, 16x16 and 25x25)
//    - user's board -> complete the user's cells with an exact-cover search (Dancing Links)
// 2. Keep the user's cells and reveal random extra cells until the given-cell count is reached.
// 3. Check that the finished puzzle breaks no Sudoku rule.
class PuzzleBuilder{
	static final int OK=0, TOO_MANY=1, INVALID=2, NO_SOLUTION=3;
	private static final long TIME_LIMIT_MS=15000;	// give up on a user board that cannot be completed

	int status;
	int[][][] puzzle;

	private final Random rand=new Random();
	private int n, box;

	// user==null means "start from an empty board"
	PuzzleBuilder(int size, int[][][] user, int percent){
		n=size;
		box=(int)Math.sqrt(size);
		if(user!=null){
			if(GenerateSudoku.countGivens(user)>GenerateSudoku.maxGivens(size, percent)){
				status=TOO_MANY; return;
				}
			if(!new Validator(user, subgrids(size)).checkValidity()){
				status=INVALID; return;
				}
			}
		int[][][] full=(user==null || GenerateSudoku.countGivens(user)==0) ? shuffledGrid() : complete(user);
		if(full==null){ status=NO_SOLUTION; return; }
		if(!new Validator(full, subgrids(size)).checkAnswer()){ status=INVALID; return; }

		puzzle=new GenerateSudoku(full, user, percent).getSudoku();
		if(!new Validator(puzzle, subgrids(size)).checkValidity()){ status=INVALID; puzzle=null; return; }
		status=OK;
		}

	static String message(int status){
		switch(status){
			case TOO_MANY: return "There are more given cells than the chosen percentage allows.";
			case INVALID: return "The board breaks a Sudoku rule (a number repeats in a row, column or box).";
			case NO_SOLUTION: return "These given cells could not be completed into a full Sudoku. Change or remove some of them and try again.";
			default: return "";
			}
		}

	// ---------- empty board: shuffle a valid pattern ----------
	private int[][][] shuffledGrid(){
		int[] rows=shuffledLines(), cols=shuffledLines(), digits=shuffledRange(n);
		int[][][] full=new int[n][n][2];
		for(int r=0; r<n; r++)
			for(int c=0; c<n; c++){
				int row=rows[r], col=cols[c];
				int value=(box*(row%box)+row/box+col)%n;	// standard valid base pattern
				full[r][c][0]=digits[value]+1;
				}
		return full;
		}
	// row (or column) order that keeps every box intact: shuffle the bands, then the lines inside each band
	private int[] shuffledLines(){
		int[] bands=shuffledRange(box), lines=new int[n];
		for(int b=0, i=0; b<box; b++){
			int[] inside=shuffledRange(box);
			for(int k=0; k<box; k++)
				lines[i++]=bands[b]*box+inside[k];
			}
		return lines;
		}
	private int[] shuffledRange(int count){
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0; i<count; i++) list.add(i);
		Collections.shuffle(list, rand);
		int[] out=new int[count];
		for(int i=0; i<count; i++) out[i]=list.get(i);
		return out;
		}

	// ---------- user's board: Dancing Links (Knuth's Algorithm X) ----------
	// Each choice "number d in cell (r,c)" is a row; each rule ("cell filled", "row has d",
	// "column has d", "box has d") is a column. A solution picks rows that cover every column once.
	// Choices are shuffled, and the search restarts with a bigger budget if it gets stuck.
	private int[] L, R, U, D, colOf, rowOf, count;
	private int[] choice, picked;
	private int depth;
	private long steps, stepLimit;
	private boolean exhausted;

	private int[][][] complete(int[][][] user){
		long deadline=System.currentTimeMillis()+TIME_LIMIT_MS;
		for(long budget=20000; System.currentTimeMillis()<deadline; budget=budget*2){
			buildLinks(user);
			steps=0;
			stepLimit=budget;
			exhausted=false;
			depth=0;
			if(search(deadline)){
				int[][][] full=new int[n][n][2];
				for(int r=0; r<n; r++)
					for(int c=0; c<n; c++)
						if(user[r][c][1]==0 && user[r][c][0]!=0)
							full[r][c][0]=user[r][c][0];
				for(int i=0; i<depth; i++){
					int ch=picked[i];
					full[ch/(n*n)][(ch/n)%n][0]=ch%n+1;
					}
				return full;
				}
			if(!exhausted)
				return null;	// every option was tried: the user's cells cannot be completed
			}
		return null;	// ran out of time
		}
	private void buildLinks(int[][][] user){
		int cols=4*n*n, rows=n*n*n, nodes=1+cols+4*rows;
		L=new int[nodes]; R=new int[nodes]; U=new int[nodes]; D=new int[nodes];
		colOf=new int[nodes]; rowOf=new int[nodes]; count=new int[cols+1];
		picked=new int[n*n];
		for(int c=0; c<=cols; c++){	// node 0 is the root, nodes 1..cols are column headers
			L[c]=c==0 ? cols : c-1;
			R[c]=c==cols ? 0 : c+1;
			U[c]=D[c]=c;
			}
		int next=cols+1;
		int[] order=shuffledRange(rows);	// random row order gives a different solution each time
		int[] firstNode=new int[rows];
		for(int ch: order){
			int r=ch/(n*n), c=(ch/n)%n, d=ch%n;
			int[] hits={1+r*n+c, 1+n*n+r*n+d, 1+2*n*n+c*n+d, 1+3*n*n+boxOf(r, c)*n+d};
			int first=next;
			for(int k=0; k<4; k++, next++){
				int col=hits[k];
				colOf[next]=col; rowOf[next]=ch;
				U[next]=U[col]; D[next]=col; D[U[col]]=next; U[col]=next;
				count[col]++;
				L[next]=k==0 ? first+3 : next-1;
				R[next]=k==3 ? first : next+1;
				}
			firstNode[ch]=first;
			}
		// the user's cells are fixed: take those choices before searching
		for(int r=0; r<n; r++)
			for(int c=0; c<n; c++)
				if(user[r][c][1]==0 && user[r][c][0]!=0){
					int node=firstNode[(r*n+c)*n+user[r][c][0]-1];
					cover(colOf[node]);
					for(int j=R[node]; j!=node; j=R[j]) cover(colOf[j]);
					}
		}
	private boolean search(long deadline){
		if(R[0]==0)
			return true;	// every rule is satisfied
		if(++steps>stepLimit || (steps%1000==0 && System.currentTimeMillis()>deadline)){
			exhausted=true;
			return false;
			}
		int col=R[0];	// the rule with the fewest ways to satisfy it
		for(int c=R[0]; c!=0; c=R[c])
			if(count[c]<count[col]) col=c;
		if(count[col]==0)
			return false;
		cover(col);
		for(int node=D[col]; node!=col; node=D[node]){
			picked[depth++]=rowOf[node];
			for(int j=R[node]; j!=node; j=R[j]) cover(colOf[j]);
			if(search(deadline)) return true;
			for(int j=L[node]; j!=node; j=L[j]) uncover(colOf[j]);
			depth--;
			if(exhausted) break;
			}
		uncover(col);
		return false;
		}
	private void cover(int col){
		R[L[col]]=R[col]; L[R[col]]=L[col];
		for(int i=D[col]; i!=col; i=D[i])
			for(int j=R[i]; j!=i; j=R[j]){
				D[U[j]]=D[j]; U[D[j]]=U[j];
				count[colOf[j]]--;
				}
		}
	private void uncover(int col){
		for(int i=U[col]; i!=col; i=U[i])
			for(int j=L[i]; j!=i; j=L[j]){
				count[colOf[j]]++;
				D[U[j]]=j; U[D[j]]=j;
				}
		R[L[col]]=col; L[R[col]]=col;
		}
	private int boxOf(int r, int c){
		return (r/box)*box+c/box;
		}

	static Subgrid[] subgrids(int n){
		Subgrid[] sg=new Subgrid[n];
		int dy=(int)Math.sqrt(n), dx=n/dy;
		for(int ctr=0, x=0; ctr<n; ctr++, x++){
			sg[ctr]=new Subgrid(x*dx, (ctr/dy)*dy, dx, dy);
			if((ctr+1)%dy==0 && ctr>0) x=-1;
			}
		return sg;
		}
	}
