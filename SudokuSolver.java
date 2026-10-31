import java.util.*;
class SudokuSolver{
	private static final Random rand=new Random();
	// returns a full valid grid consistent with the givens, or null if none exists
	static int[][][] solve(int[][][] start){
		int n=start.length, box=(int)Math.sqrt(n);
		int[][] g=new int[n][n];
		for(int r=0; r<n; r++)
			for(int c=0; c<n; c++)
				g[r][c]=start[r][c][1]==0 ? start[r][c][0] : 0;
		if(!fill(g, n, box)) return null;
		int[][][] out=new int[n][n][2];
		for(int r=0; r<n; r++)
			for(int c=0; c<n; c++){
				out[r][c][0]=g[r][c];
				out[r][c][1]=start[r][c][1];
				}
		return out;
		}
	private static boolean fill(int[][] g, int n, int box){
		int br=-1, bc=-1;
		List<Integer> best=null;
		for(int r=0; r<n; r++)
			for(int c=0; c<n; c++)
				if(g[r][c]==0){
					List<Integer> cand=candidates(g, n, box, r, c);
					if(cand.isEmpty()) return false;
					if(best==null || cand.size()<best.size()){ best=cand; br=r; bc=c; }
					}
		if(best==null) return true;
		Collections.shuffle(best, rand);
		for(int v: best){
			g[br][bc]=v;
			if(fill(g, n, box)) return true;
			}
		g[br][bc]=0;
		return false;
		}
	private static List<Integer> candidates(int[][] g, int n, int box, int r, int c){
		boolean[] used=new boolean[n+1];
		for(int i=0; i<n; i++){ used[g[r][i]]=true; used[g[i][c]]=true; }
		int sr=r/box*box, sc=c/box*box;
		for(int i=0; i<box; i++)
			for(int j=0; j<box; j++) used[g[sr+i][sc+j]]=true;
		List<Integer> l=new ArrayList<Integer>();
		for(int v=1; v<=n; v++) if(!used[v]) l.add(v);
		return l;
		}
	}