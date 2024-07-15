/**

	WeberknechtCMS - Open Source Content Management
	Written and maintained by Matthias Pueski 
	
	Copyright (c) 2009 Matthias Pueski
	
	This program is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/
package org.pmedv.util;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtils {

	protected static final Log log = LogFactory.getLog(ImageUtils.class);
	
	public static void createThumbnail(String src,int width, int height, String dest) throws Exception {

		log.info("Creating thumnail for file : "+src+" to "+dest);
		
		// String FileLocation = filePath + fileName;

		Image image = Toolkit.getDefaultToolkit().getImage(src);

		MediaTracker mediaTracker = new MediaTracker(new Container());
		mediaTracker.addImage(image, 0);
		mediaTracker.waitForID(0);

		// determine thumbnail size from WIDTH and HEIGHT

		int thumbWidth = width;
		int thumbHeight = height;
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;

		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}

		// draw original image to thumbnail image object and
		// scale it to the new size on-the-fly

		BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

		// save thumbnail image to OUTFILE

		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
		log.debug("Created Thumbnail : " + dest);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
		param.setQuality(1.0f, false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(thumbImage);
		out.close();
	}

	/**
	 * Loads an image safely with a <code>MediaTracker<code> object
	 * 
	 * @param name
	 * @param parent
	 * @return
	 */
	public static synchronized Image getImageFile(String name, JFrame parent)
	{
	   MediaTracker tracker = new MediaTracker(parent);
	   Image openedImage=Toolkit.getDefaultToolkit().getImage(name);
	   tracker.addImage(openedImage,1);
	   try
	   {
	       tracker.waitForAll();
	   }
	   catch (InterruptedException ie)
	   {
	   }
	   return openedImage;
	}

	public static void scaleJPG(String src, int width, int height, String dest) throws IOException {

		/*
		
		// Affine transform stuff produces ugly thumbnails, maybe we recheck it later
		
		BufferedImage bsrc = ImageIO.read(new File(src));		
		BufferedImage bdest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bdest.createGraphics();
		
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON );
		g.setRenderingHint( RenderingHints.KEY_INTERPOLATION,	RenderingHints.VALUE_INTERPOLATION_BICUBIC );
		
		AffineTransform at = AffineTransform.getScaleInstance((double) width / bsrc.getWidth(), (double) height / bsrc.getHeight());		
		AffineTransformOp aop = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);		
		g.drawImage(bsrc, aop, 0, 0);

		ImageIO.write(bdest, "JPG", new File(dest));
		
		*/

		BufferedImage bsrc = ImageIO.read(new File(src));		
		
		log.info("got image : "+bsrc);
		
		int thumbWidth = width;
		int thumbHeight = height;
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = bsrc.getWidth(null);
		int imageHeight = bsrc.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;

		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}

		Image scaledImage = bsrc.getScaledInstance(thumbWidth,thumbHeight,Image.SCALE_SMOOTH);
		
		BufferedImage bdest = new BufferedImage(thumbWidth,thumbHeight,BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = bdest.createGraphics();
		g.drawImage(scaledImage,0,0,null);
		ImageIO.write(bdest, "JPG", new File(dest));
		
	}

	public static int[] getArrayFromImage(Image img, int width, int height) throws InterruptedException {
		int[] pixels = new int[width * height];
		PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixels, 0, width);
		pg.grabPixels();
		return pixels;
	}

	public static Image getImageFromArray(int[] pixels, int width, int height) {
		MemoryImageSource mis = new MemoryImageSource(width, height, pixels, 0, width);
		Toolkit tk = Toolkit.getDefaultToolkit();
		return tk.createImage(mis);
	}	

}
