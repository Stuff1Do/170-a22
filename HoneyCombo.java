import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

// A drop-down that matches the honeycomb theme. The box itself is transparent, so it sits on top of
// an orange field drawn in the background picture; the list that opens is honey-coloured.
public class HoneyCombo extends JComboBox<String>{
	HoneyCombo(String[] items){
		super(items);
		setUI(new HoneyUI());
		setOpaque(false);
		setFont(HoneyTheme.font(19f));
		setForeground(HoneyTheme.INK);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setMaximumRowCount(7);
		setRenderer(new DefaultListCellRenderer(){
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean focus){
				JLabel label=(JLabel)super.getListCellRendererComponent(list, value, index, selected, false);
				label.setFont(HoneyTheme.font(19f));
				label.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
				if(index==-1){				// the value shown in the closed box
					label.setOpaque(false);
					label.setForeground(HoneyTheme.INK);
					}
				else{
					label.setOpaque(true);
					label.setBackground(selected ? HoneyTheme.ORANGE : (index%2==0 ? HoneyTheme.HONEY : HoneyTheme.HONEY_LIGHT));
					label.setForeground(selected ? HoneyTheme.SELECT_TEXT : HoneyTheme.INK);
					}
				return label;
				}
			});
		}

	private static class HoneyUI extends BasicComboBoxUI{
		public void installUI(JComponent c){
			super.installUI(c);
			listBox.setSelectionForeground(HoneyTheme.INK);		// the focused closed box uses this colour
			}
		protected JButton createArrowButton(){
			JButton button=new JButton(){
				public void paintComponent(Graphics g){
					Graphics2D g2=(Graphics2D)g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					int w=getWidth(), h=getHeight();
					g2.setPaint(new GradientPaint(0, 0, new Color(255,208,30), 0, h, new Color(200,115,0)));
					g2.fillRoundRect(3, 4, w-6, h-8, 14, 14);
					g2.setColor(new Color(130,55,0));
					g2.setStroke(new BasicStroke(1.5f));
					g2.drawRoundRect(3, 4, w-7, h-9, 14, 14);
					int cx=w/2, cy=h/2;
					g2.setColor(HoneyTheme.INK);
					g2.fillPolygon(new int[]{cx-6, cx+6, cx}, new int[]{cy-3, cy-3, cy+5}, 3);
					g2.dispose();
					}
				};
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setOpaque(false);
			button.setBorder(null);
			return button;
			}
		public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus){
			}									// the orange field is part of the background picture
		protected ComboPopup createPopup(){
			BasicComboPopup popup=new BasicComboPopup(comboBox);
			popup.setBorder(BorderFactory.createLineBorder(HoneyTheme.DARK_ORANGE, 3));
			popup.getList().setBackground(HoneyTheme.HONEY);
			return popup;
			}
		}
	}