import java.io.File;
class PuzzleBuilder{
	static final int OK=0, TOO_MANY=1, INVALID=2, NO_SOLUTION=3;
	int status;
	int[][][] puzzle;

	// user==null means "start from empty board"
	PuzzleBuilder(int size, int[][][] user, int percent, int numEmp, int numOnlook, int maxCycle){
		int[][][] start=new int[size][size][2];
		if(user==null){
			for(int[][] row: start)
				for(int[] cell: row) cell[1]=1;
			}
		else{
			if(GenerateSudoku.countGivens(user)>GenerateSudoku.maxGivens(size, percent)){
				status=TOO_MANY; return;
				}
			for(int r=0; r<size; r++)
				for(int c=0; c<size; c++){
					start[r][c][0]=user[r][c][0];
					start[r][c][1]=user[r][c][1];
					}
			if(!new Validator(start, subgrids(size)).checkValidity()){
				status=INVALID; return;
				}
			}
		int[][][] full=solveWithABC(start, numEmp, numOnlook, maxCycle);
		if(full==null){ status=NO_SOLUTION; return; }
		puzzle=new GenerateSudoku(full, user, percent).getSudoku();
        if(!new Validator(puzzle, subgrids(size)).checkValidity()){ status=INVALID; return; }
		status=OK;
		}
	private int[][][] solveWithABC(int[][][] start, int numEmp, int numOnlook, int maxCycle){
		try{
			File tmp=File.createTempFile("abc", ".xls");
			PrintResult printer=new PrintResult(tmp.getPath());
			ABC abc=new ABC(printer, start, numEmp, numOnlook, maxCycle);
			abc.start();
			abc.join();
			printer.close();
			tmp.delete();
			if(abc.getFitness()!=1) return null;
			int[][][] sol=abc.getBestSolution();
			if(!new Validator(sol, subgrids(sol.length)).checkAnswer()) return null;
			return sol;
			}
		catch(Exception e){
			return null;
			}
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