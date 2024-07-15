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
package org.pmedv.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pmedv.beans.objects.TemplateBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Template;

public class TemplateServiceImpl implements TemplateService{

	@Override
	public boolean createTemplate(TemplateBean template) throws IllegalArgumentException {
		
		if (template == null)
			throw new IllegalArgumentException("Template must not be null.");
		if (template.getName() == null || template.getName().length() < 1)
			throw new IllegalArgumentException("You must provide a template name.");
		if (DAOManager.getInstance().getTemplateDAO().findByName(template.getName()) != null)
			throw new IllegalArgumentException("A template with this name already exists.");
		
		Template t = new Template();
		
		t.setDescription(template.getDescription());
		t.setDivStyleClass(template.getDivStyleClass());
		t.setGalleryType(template.getGalleryType());
		t.setName(template.getName());
		t.setPlaceInDiv(template.isPlaceInDiv());
		t.setTableDataStyleClass(template.getTableDataStyleClass());
		t.setTableRowStyleClass(template.getTableRowStyleClass());
		t.setTableStyleClass(template.getTableStyleClass());
		t.setHtml(template.getHtml());
		t.setCss(template.getCss());
		
		return DAOManager.getInstance().getTemplateDAO().createAndStore(t) != null;
	}

	@Override
	public boolean deleteTemplate(Long templateId) throws IllegalArgumentException {
		
		if (templateId == null) {
			throw new IllegalArgumentException("templateId must not be null.");
		}
		
		Template template = (Template)DAOManager.getInstance().getTemplateDAO().findByID(templateId);

		if (template == null)
			throw new IllegalArgumentException("Template does not exist.");

		return DAOManager.getInstance().getTemplateDAO().delete(template);
		
	}

	@Override
	public ArrayList<TemplateBean> findAll() {

		ArrayList<TemplateBean> templates = new ArrayList<TemplateBean>();
		
		List <?> _templates = DAOManager.getInstance().getTemplateDAO().findAllItems();
		
		for (Iterator<?> it = _templates.iterator();it.hasNext();) {
			
			Template template = (Template)it.next();
			
			TemplateBean t = new TemplateBean();

			t.setDescription(template.getDescription());
			t.setDivStyleClass(template.getDivStyleClass());
			t.setGalleryType(template.getGalleryType());
			t.setName(template.getName());
			t.setPlaceInDiv(template.isPlaceInDiv());
			t.setTableDataStyleClass(template.getTableDataStyleClass());
			t.setTableRowStyleClass(template.getTableRowStyleClass());
			t.setTableStyleClass(template.getTableStyleClass());
			t.setId(template.getId());
			t.setHtml(template.getHtml());
			t.setCss(template.getCss());

			templates.add(t);
		}
		
		return templates;
	}

	@Override
	public TemplateBean findTemplateById(Long templateId) throws IllegalArgumentException {

		if (templateId == null)
			throw new IllegalArgumentException("TemplateId must not be null!");
		
		Template template = (Template)DAOManager.getInstance().getTemplateDAO().findByID(templateId);
		
		if (template == null)
			return null;
		
		TemplateBean t = new TemplateBean();

		t.setDescription(template.getDescription());
		t.setDivStyleClass(template.getDivStyleClass());
		t.setGalleryType(template.getGalleryType());
		t.setName(template.getName());
		t.setPlaceInDiv(template.isPlaceInDiv());
		t.setTableDataStyleClass(template.getTableDataStyleClass());
		t.setTableRowStyleClass(template.getTableRowStyleClass());
		t.setTableStyleClass(template.getTableStyleClass());
		t.setId(template.getId());
		t.setHtml(template.getHtml());
		t.setCss(template.getCss());

		return t;
	}

	@Override
	public TemplateBean findTemplateByName(String name) {
		
		if (name == null)
			throw new IllegalArgumentException("Template name must not be null!");
		
		Template template = (Template)DAOManager.getInstance().getTemplateDAO().findByName(name);
		
		if (template == null)
			return null;
		
		TemplateBean t = new TemplateBean();
	
		t.setDescription(template.getDescription());
		t.setDivStyleClass(template.getDivStyleClass());
		t.setGalleryType(template.getGalleryType());
		t.setName(template.getName());
		t.setPlaceInDiv(template.isPlaceInDiv());
		t.setTableDataStyleClass(template.getTableDataStyleClass());
		t.setTableRowStyleClass(template.getTableRowStyleClass());
		t.setTableStyleClass(template.getTableStyleClass());
		t.setId(template.getId());
		t.setHtml(template.getHtml());
		t.setCss(template.getCss());
		
		return t;
	}

	@Override
	public boolean updateTemplate(TemplateBean template) throws IllegalArgumentException {

		if (template == null)
			throw new IllegalArgumentException("Template must not be null.");
		
		Template t = (Template)DAOManager.getInstance().getTemplateDAO().findByName(template.getName());
		
		if (t == null)
			throw new IllegalArgumentException("Template does not exist.");
		
		t.setDescription(template.getDescription());
		t.setDivStyleClass(template.getDivStyleClass());
		t.setGalleryType(template.getGalleryType());
		t.setName(template.getName());
		t.setPlaceInDiv(template.isPlaceInDiv());
		t.setTableDataStyleClass(template.getTableDataStyleClass());
		t.setTableRowStyleClass(template.getTableRowStyleClass());
		t.setTableStyleClass(template.getTableStyleClass());
		t.setHtml(template.getHtml());
		t.setCss(template.getCss());
		
		return DAOManager.getInstance().getTemplateDAO().update(t);
	}

}
