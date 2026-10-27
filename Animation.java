import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
public class Animation{
	private JPanel pane;
	private JLabel board;
	private int sudokuArray[][][];
	private int size, startX, startY, inc;
	protected JLabel btn[][];
	private ImageIcon normal[];
	private generalPanel gp=new generalPanel();
	Animation(){}
	Animation(int sudokuArray[][][], JPanel pane){
		this.pane=pane;
		this.sudokuArray=sudokuArray;
		pane.setVisible(true);
		setConstants();
		}
	//one label per cell; empty cells change picture as the solver works
	private void setConstants(){
		size=sudokuArray.length;
		startX=size/6+6;
		startY=86;
		inc=504/size;
		normal=new ImageIcon[size+1];
		for(int num=0; num<=size; num++)
			normal[num]=new ImageIcon("img/box/"+size+"x"+size+"/normal/"+num+".png");
		btn=new JLabel[size][size];
		for(int ctr=0, X=startX, Y=startY; ctr<size; ctr++, Y+=inc, X=startX){
			for(int count=0; count<size; count++, X+=inc){
				if(sudokuArray[ctr][count][1]==0)
					btn[ctr][count]=gp.addLabel(pane, "img/box/"+size+"x"+size+"/given/"+sudokuArray[ctr][count][0]+".png", X, Y);
				else
					btn[ctr][count]=gp.addLabel(pane, normal[0], X, Y);
				}
			}
		board=gp.addLabel(pane,"img/board/"+size+"x"+size+".png",0,84);
		}
	protected int[][][] getSudokuArray(){
		return sudokuArray;
		}
	//shows the solver's current best board in every empty cell
	protected void changePic(int solution[][][]){
		for(int row=0; row<size; row++){
			for(int col=0; col<size; col++){
				if(sudokuArray[row][col][1]==1)
					btn[row][col].setIcon(normal[solution[row][col][0]]);
				}
			}
		sudokuArray=solution;
		}
	protected void setSudoku(int solution[][][]){
		sudokuArray=solution;
		}
	protected void decompose(){
		pane.removeAll();
		board=null;
		sudokuArray=null;
		btn=null;
		normal=null;
		gp=null;
		pane.setVisible(false);
		}
	}
