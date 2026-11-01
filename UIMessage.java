import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
// A message pop-up in the game's style: honeycomb background, a title in the game's colors,
// the message, and an OKAY button. Used instead of the plain system message box (SudokuBee.message()).
public class UIMessage extends generalPanel{
	// img/bg/message.png is the Random Puzzle pop-up (600x329) without its text, so it goes in the same spot
	private static final int BG_X=100, BG_Y=174;
	private static final int PILL_X=304, PILL_Y=251;	// center of the OKAY pill inside the background
	private JPanel pane;
	private JLabel bg;
	private JComponent title, text;
	protected JButton okay;
	UIMessage(JPanel pane, String heading, String message){
		this.pane=pane;
		pane.removeAll();					// clear whatever the previous pop-up left behind
		pane.setOpaque(true);
		title=new Title(heading);
		title.setBounds(BG_X, BG_Y+30, 600, 76);
		pane.add(title);
		text=centeredText(message, 480);
		text.setLocation(BG_X+60, BG_Y+166-text.getHeight()/2);	// centered in the space between the title and OKAY
		pane.add(text);
		okay=addButton(pane, "img/exit/okay.png", "img/exit/h_okay.png", 0, 0);
		okay.setLocation(BG_X+PILL_X-okay.getWidth()/2, BG_Y+PILL_Y-okay.getHeight()/2);	// on the pill drawn in the background
		bg=addLabel(pane, "img/bg/message.png", BG_X, BG_Y);	// added last so it sits behind the rest
		pane.setVisible(true);
		pane.revalidate();
		pane.repaint();
		final JPanel shown=pane;
		javax.swing.SwingUtilities.invokeLater(() -> shown.repaint());	// once more after it is on screen: the first
		}																// paint can miss the end of the last text line
	protected void decompose(){
		pane.removeAll();
		pane.setVisible(false);
		pane.repaint();
		bg=null;
		title=text=null;
		okay=null;
		}
	// see-through text box that wraps the message to the given width and centers each line ("\n" starts a new line)
	private JTextPane centeredText(String message, int width){
		JTextPane area=new JTextPane();
		area.setEditable(false);
		area.setFocusable(false);
		area.setOpaque(false);
		area.setBorder(null);
		area.setBackground(new Color(0,0,0,0));
		area.setFont(new Font("Arial", Font.BOLD, 17));
		area.setForeground(new Color(70,25,0));
		area.setText(message);
		SimpleAttributeSet center=new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setLeftIndent(center, 30);	// lines wrap 30px in from each side; the screen's letters can be a little
		StyleConstants.setRightIndent(center, 30);	// wider than the measured ones, and this room keeps them from being cut off
		StyledDocument doc=area.getStyledDocument();
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		area.setSize(width, Short.MAX_VALUE);		// lets the box work out how tall the wrapped text is
		area.setSize(width, area.getPreferredSize().height);
		return area;
		}
	// the title like the game's own titles: yellow-orange letters, a dark red outline and a red glow
	private static class Title extends JComponent{
		private final String words;
		Title(String words){
			this.words=words.toUpperCase();
			}
		protected void paintComponent(Graphics g){
			Graphics2D g2=(Graphics2D)g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			TextLayout layout=null;
			for(int size=46; size>=20; size-=2){	// shrink a long title until it fits
				layout=new TextLayout(words, new Font("Impact", Font.PLAIN, size), g2.getFontRenderContext());
				if(layout.getBounds().getWidth()<=getWidth()-60)
					break;
				}
			Rectangle2D box=layout.getBounds();
			double x=(getWidth()-box.getWidth())/2-box.getX(), y=(getHeight()-box.getHeight())/2-box.getY();
			Shape letters=layout.getOutline(AffineTransform.getTranslateInstance(x, y));
			g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.setColor(new Color(255,40,0,90));
			g2.draw(letters);
			g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.setColor(new Color(150,15,0));
			g2.draw(letters);
			g2.setPaint(new GradientPaint(0, (float)(y+box.getY()), new Color(255,236,80), 0, (float)(y+box.getY()+box.getHeight()), new Color(255,130,0)));
			g2.fill(letters);
			g2.dispose();
			}
		}
	}
