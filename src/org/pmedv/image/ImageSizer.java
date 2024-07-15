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
package org.pmedv.image;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.ImageObserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class ImageSizer {
	
	protected static final Log log = LogFactory.getLog(ImageSizer.class);
			
    private Dimension size = new Dimension(-1, -1);
    private boolean incomplete;
 
    private ImageObserver obs = new ImageObserver() {
        public synchronized boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            if ((infoflags & WIDTH) != 0)
                size.width = width;
            if ((infoflags & HEIGHT) != 0)
                size.height = height;
            if ((infoflags & (ERROR | ABORT)) != 0)
                incomplete = true;
            boolean done = resultKnown();
            if (done)
                notifyAll();
            return !done;
        }
    };
 
    public ImageSizer(Image image) {
        size.width = image.getWidth(obs);
        size.height = image.getHeight(obs);
    }
 
    private boolean resultKnown() {
        return size.width != -1 && size.height != -1 || incomplete;
    }
 
    //returns null iff error or abort
    public Dimension getImageSize() throws InterruptedException {
        synchronized (obs) {
            while (!resultKnown())
                obs.wait();
            return incomplete ? null : new Dimension(size);
        }
    }
 
    
	public Image scaleImage(Image image,int width, int height,int percent)throws InterruptedException {

		int ImageHeight = height;
		int ImageWidth  = width;
		
		log.debug("Original Image Dimensions : "+ImageWidth+"x"+ImageHeight);
		
		Image ScaledImage;
		
		ScaledImage = image.getScaledInstance(

				(ImageWidth * percent) / 100,
				(ImageHeight * percent) / 100,
				Image.SCALE_SMOOTH );
		
		return ScaledImage;
		
	}
}
