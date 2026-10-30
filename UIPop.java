import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
public class UIPop extends generalPanel{
	//the picker is a honeycomb: rows of hexagons centered on CENTER_X, STEP_X apart, rows STEP_Y apart
	//(the background pictures "img/game control/<size>x<size>.png" are drawn on the same grid)
	private static final int CENTER_X=240, STEP_X=73, STEP_Y=65;
	private JPanel pane;
	private JPanel panel;
	private JLabel bg;
	private int rows[];	//hexagons per row; the first row is erase, text box, cancel
	private int topY;	//y of the first row's centers
	protected int size, btnX, btnY;
	protected JButton cancel, erase;
	protected JButton btn[];
	protected JTextField field;
	UIPop(int size, JPanel pane){
		this.pane=pane;
		this.size=size;
		panel=addPanel(pane, 5, 84, 500,500);
		panel.setOpaque(true);
		if(size==9){
			rows=new int[]{3,4,3,2};
			topY=147;
			}
		else if(size==16){
			rows=new int[]{3,4,5,4,3};
			topY=120;
			}
		else{
			rows=new int[]{3,4,5,4,5,4,3};
			topY=55;
			}
		placeButtons();
		bg=addLabel(panel, "img/game control/"+size+"x"+size+".png",0,0);
		field.grabFocus();
		}
	//puts every button in the middle of its hexagon
	private void placeButtons(){
		btn=new JButton[size];
		int num=0;
		for(int row=0; row<rows.length; row++){
			for(int ctr=0; ctr<rows[row]; ctr++){
				int x=(int)Math.floor(CENTER_X+(ctr-(rows[row]-1)/2.0)*STEP_X+0.5);
				int y=topY+row*STEP_Y;
				if(row==0 && ctr==0)
					erase=addButton(panel, "img/box/misc/clear.png", x-20, y-20);
				else if(row==0 && ctr==1)
					field=addTextField(panel, "", x-30, y-19, 60, 38);
				else if(row==0)
					cancel=addButton(panel, "img/box/misc/cancel.png", x-20, y-20);
				else if(num<size){
					btn[num]=addButton(panel, "img/game control/numbers/"+(num+1)+".png", x-20, y-20);
					num++;
					}
				}
			}
		}
	protected void setVisible(boolean isVisible, int btnX, int btnY, int num){
		pane.setVisible(isVisible);
		if(num==0)
			field.setText("");
		else
			field.setText(num+"");
		field.grabFocus();
		this.btnX=btnX;
		this.btnY=btnY;
		}
	protected void decompose(){
		pane.removeAll();
		panel=null;
		bg=null;
		cancel=erase=null;
		for(int ctr=0; ctr<size; ctr++)
			btn[ctr]=null;
		}
	}
