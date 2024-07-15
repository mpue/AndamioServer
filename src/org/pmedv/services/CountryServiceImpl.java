package org.pmedv.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.pmedv.beans.objects.CountryBean;
import org.pmedv.beans.objects.UserBean;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Country;
import org.pmedv.pojos.User;

/**
 * @author Matthias Pueski (13.05.2010)
 *
 */

public class CountryServiceImpl implements RemoteAccessService<CountryBean> {

	@Override
	public Long create(CountryBean object) throws IllegalArgumentException {

		if (object == null)
			throw new IllegalArgumentException("Country must not be null.");
		if (object.getName() == null)
			throw new IllegalArgumentException("Name must not be null");
		if (object.getCode() == null)
			throw new IllegalArgumentException("Code must not be null");
		
		Country country = (Country)DAOManager.getInstance().getCountryDAO().findByName(object.getName());
		
		if (country != null)
			throw new IllegalArgumentException("A country with the name "+country.getName()+" already exists."); 

		country = new Country();
		
		country.setName(object.getName());
		country.setCode(object.getCode());
		
		if (DAOManager.getInstance().getCountryDAO().createAndStore(country) != null) {			
			return DAOManager.getInstance().getCountryDAO().getMaxId();						
		}
		
		return null;
	}

	@Override
	public boolean delete(CountryBean object) throws IllegalArgumentException {		
		
		if (object == null)
			throw new IllegalArgumentException("Country must not be null.");
		
		Country country = (Country)DAOManager.getInstance().getCountryDAO().findByID(object.getId());		
		return DAOManager.getInstance().getCountryDAO().delete(country);
	}

	@Override
	public ArrayList<CountryBean> findAll() {

		List<?> countries = DAOManager.getInstance().getCountryDAO().findAllItems();
		
		ArrayList<CountryBean> _countries = new ArrayList<CountryBean>();
		
		for (Iterator<?> it = countries.iterator();it.hasNext();) {
			
			Country country = (Country)it.next();
			
			CountryBean countryBean = new CountryBean();
			
			countryBean.setId(country.getId());
			countryBean.setName(country.getName());
			countryBean.setCode(country.getCode());
			
			_countries.add(countryBean);
			
		}
		
		return _countries;
	}

	@Override
	public CountryBean findById(Long id) throws IllegalArgumentException {

		if (id == null)
			throw new IllegalArgumentException("country id must not be null.");
		
		Country country = (Country)DAOManager.getInstance().getCountryDAO().findByID(id);
		
		if (country == null)
			throw new IllegalArgumentException("The country with the id "+id+" does not exist"); 
		
		CountryBean countryBean = new CountryBean();
		
		countryBean.setId(country.getId());
		countryBean.setName(country.getName());
		countryBean.setCode(country.getCode());
		
		if (country.getUsers() != null) {
			
			for (User user : country.getUsers()) {
				
				UserBean u = new UserBean();
				
				u.setId(user.getId().intValue());
				u.setName(user.getName());
				u.setFirstName(user.getFirstName());
				u.setLastName(user.getLastName());
				u.setActive(user.getActive());
				u.setEmail(user.getEmail());
				u.setJoinDate(user.getJoinDate());
				u.setLand(user.getLand());
				u.setName(user.getName());
				u.setNewRecord(false);
				u.setOrt(user.getOrt());
				u.setPassword(user.getPassword());
				u.setRanking(user.getRanking());
				u.setTelefon(user.getTelefon());
				u.setTitle(user.getTitle());
				
				countryBean.getUsers().add(u);
			}
			
		}
		
		return countryBean;
	}

	@Override
	public CountryBean findByName(String name) throws IllegalArgumentException {
		
		if (name == null)
			throw new IllegalArgumentException("country name must not be null.");
		
		Country country = (Country)DAOManager.getInstance().getCountryDAO().findByName(name);
		
		if (country == null)
			throw new IllegalArgumentException("The country with the name "+name+" does not exist"); 
		
		CountryBean countryBean = new CountryBean();
		
		countryBean.setId(country.getId());
		countryBean.setName(country.getName());
		countryBean.setCode(country.getCode());
		
		return countryBean;
		
	}

	@Override
	public boolean update(CountryBean object) throws IllegalArgumentException {

		
		if (object == null)
			throw new IllegalArgumentException("Country must not be null.");
		
		Country country = (Country)DAOManager.getInstance().getCountryDAO().findByID(object.getId());		

		if (country == null)
			throw new IllegalArgumentException("The country with the id "+object.getId()+" does not exist.");
		
		country.setName(object.getName());
		country.setCode(object.getCode());
		
		return DAOManager.getInstance().getCountryDAO().update(country);
		
	}

}
