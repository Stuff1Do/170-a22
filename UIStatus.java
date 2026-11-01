import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
public class UIStatus extends generalPanel{
	private JPanel pane;
	private JLabel bg, info;
	protected JButton yes, no, save, open, reset;
	UIStatus(String str, JPanel pane){
		this.pane=pane;
		yes=addButton(pane, "img/exit/yes.png", "img/exit/h_yes.png",296,27);
		no=addButton(pane, "img/exit/no.png", "img/exit/h_no.png",403,27);
		save=addButton(pane, "img/game/save.png","img/game/save.png",385,18);
		open=addButton(pane, "img/game/open.png","img/game/open.png",327,18);
		reset=addButton(pane, "img/game/reset.png","img/game/reset.png",443,18);
		yes.setVisible(str.equals("create"));
		no.setVisible(yes.isVisible());
		save.setVisible(!yes.isVisible());
		open.setVisible(!yes.isVisible());
		reset.setVisible(!yes.isVisible());
		info=addLabel(pane, "", 92, 57, 410, 17);	// given-cell counter, used in create mode (below YES/NO, which end at y=56)
		info.setFont(new Font("Arial", Font.BOLD, 13));
		bg=addLabel(pane, "img/status/"+str+".png",0,0);
		}
	protected void setVisible(boolean isVisible){
		pane.setVisible(isVisible);
		pane.repaint();
		}
	protected void setInfo(String text, boolean isError){
		info.setText(text);
		info.setForeground(isError ? Color.red : new Color(120,40,0));
		}
	protected void decompose(){
		pane.removeAll();
		bg=info=null;
		yes=no=null;
		}
	}