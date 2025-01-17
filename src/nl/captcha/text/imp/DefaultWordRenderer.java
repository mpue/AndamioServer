/*
 * Created on Sep 14, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package nl.captcha.text.imp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.util.Properties;
import java.util.Random;

import nl.captcha.servlet.Constants;
import nl.captcha.text.WordRenederer;
import nl.captcha.util.Helper;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DefaultWordRenderer implements WordRenederer {
	
	private Properties props = null;
	
	public DefaultWordRenderer(Properties props) {
		this.props =props;
	}
	
	public DefaultWordRenderer() {
	 
	}
	
	/** 
	 * Render a word to a BufferedImage.
	 * 
	 * @param word The word to be rendered.
	 * @param width The width of the image to be created.
	 * @param height The heigth of the image to be created.
	 * @return The BufferedImage created from the word,
	 */
	public  BufferedImage renderWord (String word, int width, int height) {
		
		
		
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();           
//		GraphicsDevice gd = ge.getDefaultScreenDevice();
//		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
		Graphics2D g2D = image.createGraphics();
		g2D.setColor(Color.black);
		
		RenderingHints hints = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY));
		
		g2D.setRenderingHints(hints);
		
		Font[] fonts = Helper.getFonts(props);
		Random generator = new Random();
		
		char[] wc =word.toCharArray();
		Color fontColor = Helper.getColor(props, Constants.SIMPLE_CAPTCHA_TEXTPRODUCER_FONTC,Color.black);
		g2D.setColor(fontColor);
		FontRenderContext frc = g2D.getFontRenderContext();
		int startPosX = 25;
		for (int i = 0;i<wc.length;i++) {
			char[] itchar = new char[]{wc[i]};
			//g2D.setColor(Color.black);
			int choiceFont = generator.nextInt(fonts.length) ;
			Font itFont = fonts[choiceFont];
			g2D.setFont(itFont);
			LineMetrics lmet = itFont.getLineMetrics(itchar,0,itchar.length,frc);
			GlyphVector gv = itFont.createGlyphVector(frc, itchar);
			double charWitdth = gv.getVisualBounds().getWidth();
			
			g2D.drawChars(itchar,0,itchar.length,startPosX ,35);
			startPosX = startPosX+(int)charWitdth+2;
			//
	    	
		}// for next char array.

		return image;
 

	
	}

	/**
	 * @param properties
	 */
	public void setProperties(Properties properties) {
		props = properties;
	}

}
