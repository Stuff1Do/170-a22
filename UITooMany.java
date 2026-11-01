import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

// "Too many given cells" dialog, drawn on the same panel as the Exit dialogs
public class UITooMany extends generalPanel{
	private JPanel pane;
	private JLabel bg, info;
	protected JButton okay;
	private static final int BG_X=100, BG_Y=174;		// img/bg/toomany.png is 600x329, like save.png

	UITooMany(JPanel pane, int have, int max, int percent){
		this.pane=pane;
		pane.removeAll();
		pane.setOpaque(true);
		okay=addButton(pane, "img/exit/okay.png", "img/exit/h_okay.png", 0, 0);
		okay.setLocation(BG_X+300-okay.getWidth()/2, BG_Y+251-okay.getHeight()/2);	// middle of the pill in the picture
		info=addLabel(pane, "YOU ENTERED "+have+" BUT "+percent+"% ALLOWS "+max, HoneyTheme.INK, BG_X+70, BG_Y+112, 460, 56, javax.swing.JLabel.CENTER);
		info.setFont(HoneyTheme.font(26f));
		bg=addLabel(pane, "img/bg/toomany.png", BG_X, BG_Y);
		pane.setVisible(true);
		pane.revalidate();
		pane.repaint();
		}
	protected void decompose(){
		pane.removeAll();
		pane.setVisible(false);
		pane.repaint();
		bg=info=null;
		okay=null;
		}
	}