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
package org.pmedv.cms.daos;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class CountryDAO extends AbstractDAO {

	public CountryDAO() {
	}
	
	@Override
	protected String getQueryAll() {
		return "from Country country order by country.name";
	}

	@Override
	protected String getQueryById() {
		return "from Country country where country.id = ?";
	}

	@Override
	protected String getQueryByName() {
		return "from Country country where country.name like ? order by country.name";
	}

	@Override
	protected String getQueryElementsByName() {
		return null;
	}
	
	public long getMaxId() {

		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();

		Object o = null;

		try {
			o = session.createQuery("select max(country.id) from Country country").uniqueResult();

			session.flush();
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			log.error(e.getMessage());
		}

		return (Long) o;

	}	


}
