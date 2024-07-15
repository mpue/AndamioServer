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
package org.pmedv.plugins;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.beans.objects.GalleryBean;
import org.pmedv.beans.objects.GalleryType;
import org.pmedv.beans.objects.ImageBean;
import org.pmedv.beans.objects.TemplateBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.context.AppContext;
import org.pmedv.core.html.HTMLConstants;
import org.pmedv.core.html.Link;
import org.pmedv.core.html.Rel;
import org.pmedv.core.html.Table;
import org.pmedv.core.html.TableColumn;
import org.pmedv.core.html.TableRow;
import org.pmedv.pojos.Config;
import org.pmedv.services.GalleryService;
import org.pmedv.services.TemplateService;
import org.pmedv.util.ArrayUtils;
import org.pmedv.web.ServerUtil;

/**
 * <p>
 * The <code>XGalleryPlugin</code> is used to display a customizable gallery inside
 * a page. Usually a gallery is created by the AndamioManager and can then be published.
 * </p>
 * <p>
 * Each gallery has a template assigned from which the gallery page is being generated.
 * </p>
 * 
 * @see org.pmedv.services.TemplateService
 * @see org.pmedv.services.GalleryService
 * @see org.pmedv.beans.objects.GalleryBean
 * @see org.pmedv.beans.objects.GalleryType
 * 
 * 
 * @author Matthias Pueski
 *
 */
public class XGalleryPlugin extends AbstractPlugin implements IPlugin, Serializable {

	private static final long serialVersionUID = -4646713969183322406L;

	private static final Log log = LogFactory.getLog(XGalleryPlugin.class);
	
	public XGalleryPlugin () {
		super();
		pluginID = "XGalleryPlugin";
		paramDescriptors.put("plugin_gallery_name", resourceBundle.getString("plugin.gallery.field.name"));
	}	
	
	public String getContent() {
		
		String galleryName;
		
		try {
			galleryName = paramMap.get("plugin_gallery_name").trim();
		}
		catch (NullPointerException n) {
			galleryName = "";
		}		
		
		if (galleryName.length() > 1) {
			
			GalleryService service = (GalleryService)AppContext.getApplicationContext().getBean("galleryService");			
			TemplateService templateService = (TemplateService)AppContext.getApplicationContext().getBean("templateService");
			
			GalleryBean gallery = service.findGalleryByName(galleryName);
			
			if (gallery != null) {
				TemplateBean template = templateService.findTemplateByName(gallery.getTemplate());				
				setTitle(gallery.getGalleryname());				
				return renderTable(gallery, template);
			}
				
			
			else return "Gallery "+galleryName+" is not available.";
		}
		else 
			return "No gallery found.";
		
	}
	
	private String renderTable(GalleryBean gallery, TemplateBean template) {
		
		Config config = (Config) DAOManager.getInstance().getConfigDAO().findByID(1);
		
		String imagepath = protocol+"://"+ServerUtil.getHostUrl(request)+"/"+config.getLocalPath()+config.getGallerypath()+gallery.getGalleryname()+"/";
		
		final int rows = ArrayUtils.getNumOfRows(gallery.getImages().size(), gallery.getColumns());
		final int columns = gallery.getColumns();
		final int size = gallery.getImages().size();

		int index = 0;

		final Object[] imagesArray = gallery.getImages().toArray();

		Table table = new Table();
		
		table.setStyleClass("gallery");
		
		table.setStyleClass(template.getTableStyleClass());
		table.setBorder(false);
		table.setAlign(HTMLConstants.Align.CENTER);
		
		for (int row = 0; row < rows; row++) {
			
			TableRow tableRow = new TableRow();
			tableRow.setStyleClass(template.getTableRowStyleClass());
			tableRow.setValign(HTMLConstants.Align.CENTER);
			
			for (int column = 0; column < columns; column++) {
				
				TableColumn tableColumn = new TableColumn();
				tableColumn.setStyleClass(template.getTableDataStyleClass());
				tableColumn.setAlign(HTMLConstants.Align.CENTER);
				tableColumn.setValign(HTMLConstants.Align.CENTER);
				
				if (index < size) {
					
					
					ImageBean i = (ImageBean) imagesArray[index];

					// now create thumbnail
					
					String imageSrc = "<img class=\"thumbnailImage\" src=\""+imagepath+"thumbs/thb_"+i.getFilename()+"\""
					
						// TODO : Check height and width since when we select the width, the thumbnails are not scaled correct inside the browser.
					
						// + "\" height=\"" + gallery.getThumbheight()  
						+ "\" valign=\""+ HTMLConstants.Align.MIDDLE 
						+ "\" border=\"0\">";
					

					Link link = new Link();
					link.setTarget(Link.Target.BLANK);
					link.setTitle(i.getName());
					link.setHref(imagepath+i.getFilename());
					link.setData(imageSrc);

					if (template.getGalleryType().equals(GalleryType.LIGHTBOX))
						link.setRel(Rel.LIGHTBOX);
					
					Table imageTable = new Table();
					imageTable.setWidth("100%");
					imageTable.setStyleClass("thumbnailTable");
					
					TableRow imageRow = new TableRow();
					TableColumn imageColumn = new TableColumn();
					
					imageColumn.setStyleClass("thumbnail");					
					imageColumn.setData(link.render());					
					imageRow.addColumn(imageColumn);
					imageTable.addRow(imageRow);

					TableRow captionRow = new TableRow();
					TableColumn captionColumn = new TableColumn();
					captionColumn.setData(i.getName());
					captionColumn.setStyleClass("caption");
					
					captionRow.addColumn(captionColumn);
					imageTable.addRow(captionRow);
					
					
					tableColumn.setData(imageTable.render());
					
					index++;
					
				} 
				else { // empty cell
					tableColumn.setData("&nbsp;");
				}
				
				tableRow.addColumn(tableColumn);
			}
			table.addRow(tableRow);
		}
		
		String tableHtml = table.render();		
		String templateHtml = template.getHtml();
		
		if (template != null && templateHtml != null) {

			StringBuffer html = new StringBuffer();
			
			html.append("<style type=\"text/css\">");
			html.append("\n");
			html.append(template.getCss());
			html.append("\n");
			html.append("</style>");
			html.append("\n");
			html.append(templateHtml.replace("##GALLERY##", tableHtml));
			html.append("\n");
		
			return html.toString();
			
		}
		else {
			log.info("No template found, rendering raw gallery.");
			return tableHtml;
		}
		
		
		
	}
	
}
