import java.util.*;
class GenerateSudoku{
	private int[][][] sudoku;
	private Random rand=new Random();

	static int maxGivens(int size, int percent){
		return (int)Math.round(size*size*percent/100.0);
		}
	static int countGivens(int[][][] p){
		int n=0;
		for(int[][] row: p)
			for(int[] cell: row)
				if(cell[1]==0) n++;
		return n;
		}
	// full: complete valid solution from ABC
	// user: the user's partial puzzle, or null for "start from empty board"
	GenerateSudoku(int[][][] full, int[][][] user, int percent){
		int n=full.length;
		sudoku=new int[n][n][2];
		List<Integer> hidden=new ArrayList<Integer>();
		int given=0;
		for(int r=0; r<n; r++){
			for(int c=0; c<n; c++){
				if(user!=null && user[r][c][1]==0){
					sudoku[r][c][0]=user[r][c][0];	// user's given, untouched
					sudoku[r][c][1]=0;
					given++;
					}
				else{
					sudoku[r][c][0]=0;
					sudoku[r][c][1]=1;
					hidden.add(r*n+c);
					}
				}
			}
		Collections.shuffle(hidden, rand);
		int extra=Math.max(0, maxGivens(n, percent)-given);
		for(int i=0; i<extra && i<hidden.size(); i++){
			int r=hidden.get(i)/n, c=hidden.get(i)%n;
			sudoku[r][c][0]=full[r][c][0];
			sudoku[r][c][1]=0;
			}
		}
	protected int[][][] getSudoku(){
		return sudoku;
		}
	}