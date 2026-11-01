import java.awt.Color;
import java.awt.Font;
import java.io.File;

// colours and font shared by the honeycomb-styled widgets
class HoneyTheme{
	static final Color INK=new Color(12,8,6);
	static final Color DARK_ORANGE=new Color(205,95,0);
	static final Color ORANGE=new Color(250,140,10);
	static final Color HONEY=new Color(255,214,70);
	static final Color HONEY_LIGHT=new Color(255,230,120);
	static final Color SELECT_TEXT=new Color(255,244,190);
	private static Font base;

	// the game's lettering (font/MetalMania.ttf, SIL Open Font License); falls back to Arial Bold if the file is missing
	static Font font(float size){
		if(base==null){
			try{
				base=Font.createFont(Font.TRUETYPE_FONT, new File("font/MetalMania.ttf"));
				}
			catch(Exception e){
				base=new Font("Arial", Font.BOLD, 12);
				}
			}
		return base.deriveFont(size);
		}
	}