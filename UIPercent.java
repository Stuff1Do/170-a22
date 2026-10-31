import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
public class UIPercent extends generalPanel{
	private JPanel pane;
	private JLabel bg;
	protected JComboBox combo;
	protected JButton okay, cancel, yes, no;
	protected int step=0;

	// the background image is 600x329 and is placed at (100,174), like img/bg/save.png
	private static final int BG_X=100, BG_Y=174;
	// centres of the two button pills baked into the background (panel coordinates)
	private static final int LEFT_X=BG_X+217, RIGHT_X=BG_X+411, PILL_Y=BG_Y+251;

	UIPercent(JPanel pane){
		this.pane=pane;
		pane.removeAll();					// clear whatever the previous dialog left behind
		pane.setOpaque(true);
		String pct[]=new String[20];
		for(int i=0; i<20; i++)
			pct[i]=(i*5)+"%";				// 0% ... 95%
		combo=addJCombo(pane, pct, BG_X+292, BG_Y+142, 180, 32);
		combo.setSelectedIndex(2);			// 10% is the default
		okay=addButton(pane, "img/exit/okay.png", "img/exit/h_okay.png", 0, 0);
		cancel=addButton(pane, "img/exit/cancel.png", "img/exit/h_cancel.png", 0, 0);
		yes=addButton(pane, "img/exit/yes.png", "img/exit/h_yes.png", 0, 0);
		no=addButton(pane, "img/exit/no.png", "img/exit/h_no.png", 0, 0);
		centerOn(okay, LEFT_X, PILL_Y);
		centerOn(cancel, RIGHT_X, PILL_Y);
		centerOn(yes, LEFT_X, PILL_Y);
		centerOn(no, RIGHT_X, PILL_Y);
		yes.setVisible(false);
		no.setVisible(false);
		bg=addLabel(pane, "img/bg/percent.png", BG_X, BG_Y);	// added last so it sits behind the controls
		pane.setVisible(true);
		pane.revalidate();
		pane.repaint();
		}
	private void centerOn(JButton b, int cx, int cy){
		b.setLocation(cx-b.getWidth()/2, cy-b.getHeight()/2);
		}
	protected int getPercent(){
		return combo.getSelectedIndex()*5;
		}
	protected void nextStep(){				// (a) percentage  ->  (b) start from an empty board?
		step=1;
		changePicture(bg, "img/bg/percent2.png");
		combo.setVisible(false);
		okay.setVisible(false);
		cancel.setVisible(false);			// Yes/No take the place of OK/Cancel
		yes.setVisible(true);
		no.setVisible(true);
		pane.repaint();
		}
	protected void decompose(){
		pane.removeAll();
		pane.setVisible(false);
		pane.repaint();
		bg=null;
		combo=null;
		okay=cancel=yes=no=null;
		}
	}