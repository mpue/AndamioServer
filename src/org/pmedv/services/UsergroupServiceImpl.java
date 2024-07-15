/**
 * Weberknecht AndamioManager - Open Source Content Management 
 * 
 * Written and maintained by Matthias Pueski
 * 
 * Copyright (c) 2009 Matthias Pueski
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */
package org.pmedv.services;

import java.util.ArrayList;
import java.util.List;

import org.pmedv.beans.objects.UsergroupBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Usergroup;

public class UsergroupServiceImpl implements RemoteAccessService<UsergroupBean> {

	@Override
	public Long create(UsergroupBean object) throws IllegalArgumentException {

		if (object == null)
			throw new IllegalArgumentException("Usergroup must not be null.");
		if (object.getName() == null || object.getName().length() < 4)
			throw new IllegalArgumentException("Group name must not be left blank or shorter than 4.");

		Usergroup g = (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByName(object.getName());

		if (g != null)
			throw new IllegalArgumentException("A group with this name already exists.");

		g = new Usergroup();

		g.setDescription(object.getDescription());
		g.setName(object.getName());

		if (DAOManager.getInstance().getUsergroupDAO().createAndStore(g) != null) {

			g = (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByName(object.getName());
			return g.getId();

		}

		return null;

	}

	@Override
	public boolean delete(UsergroupBean object) throws IllegalArgumentException {

		if (object == null)
			throw new IllegalArgumentException("Usergroup must not be null.");

		Usergroup g = (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByName(object.getName());

		if (g == null)
			throw new IllegalArgumentException("The specified group does not exist.");

		return DAOManager.getInstance().getUsergroupDAO().delete(g);

	}

	@Override
	public ArrayList<UsergroupBean> findAll() {

		List<?> _groups = DAOManager.getInstance().getUsergroupDAO().findAllItems();

		ArrayList<UsergroupBean> groups = new ArrayList<UsergroupBean>();

		for (Object o : _groups) {

			Usergroup group = (Usergroup) o;
			UsergroupBean ugb = new UsergroupBean();

			ugb.setId(group.getId());
			ugb.setDescription(group.getDescription());
			ugb.setName(group.getName());

			groups.add(ugb);

		}

		return groups;
	}

	@Override
	public UsergroupBean findById(Long id) throws IllegalArgumentException {

		if (id == null)
			throw new IllegalArgumentException("Group id must not be null.");

		Usergroup group = (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByID(id);

		if (group == null)
			throw new IllegalArgumentException("A group with id " + id + " does not exist.");

		UsergroupBean ugb = new UsergroupBean();

		ugb.setId(group.getId());
		ugb.setDescription(group.getDescription());
		ugb.setName(group.getName());

		return ugb;
	}

	@Override
	public UsergroupBean findByName(String name) throws IllegalArgumentException {

		if (name == null)
			throw new IllegalArgumentException("Group name must not be null.");

		Usergroup group = (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByName(name);

		if (group == null)
			throw new IllegalArgumentException("A group with the name " + name + " does not exist.");

		UsergroupBean ugb = new UsergroupBean();

		ugb.setId(group.getId());
		ugb.setDescription(group.getDescription());
		ugb.setName(group.getName());

		return ugb;
	}

	@Override
	public boolean update(UsergroupBean object) throws IllegalArgumentException {

		if (object == null)
			throw new IllegalArgumentException("Usergroup must not be null.");

		Usergroup g = (Usergroup) DAOManager.getInstance().getUsergroupDAO().findByID(object.getId());

		if (g == null)
			throw new IllegalArgumentException("The specified group does not exist.");

		g.setDescription(object.getDescription());
		g.setName(object.getName());

		return DAOManager.getInstance().getUsergroupDAO().update(g);
	}

}
