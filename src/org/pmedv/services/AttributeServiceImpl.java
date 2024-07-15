package org.pmedv.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.beans.objects.AttributeBean;
import org.pmedv.beans.objects.AttributeList;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;

public class AttributeServiceImpl implements AttributeService {

	private static final Log log = LogFactory.getLog(AttributeServiceImpl.class);

	@Override
	public Long create(AttributeBean attribute) throws IllegalArgumentException {

		if (attribute == null)
			throw new IllegalArgumentException("Attribute must not be null.");
		if (attribute.getKey() == null || attribute.getKey().length() < 1)
			throw new IllegalArgumentException("You must provide a key for the attribute.");

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);
		Long newId = null;

		final String attributeRepository = config.getBasepath() + "WEB-INF/classes/attributes.xml";

		log.info("Loading attributes from " + attributeRepository);

		File attributeRepositoryFile = new File(attributeRepository);

		if (!attributeRepositoryFile.exists()) {

			AttributeList list = new AttributeList();
			ArrayList<AttributeBean> attributes = new ArrayList<AttributeBean>();
			list.setAttribute(attributes);

			try {
				Marshaller m = JAXBContext.newInstance(AttributeList.class).createMarshaller();
				m.marshal(list, attributeRepositoryFile);
			}
			catch (JAXBException e) {
				log.info("could not create attribute repository");
				return null;
			}

		}

		AttributeList attributeList = null;

		Long lastId = 0L;

		try {
			Unmarshaller u = JAXBContext.newInstance(AttributeList.class).createUnmarshaller();
			attributeList = (AttributeList) u.unmarshal(attributeRepositoryFile);

			if (attributeList.getAttribute() == null) {

				ArrayList<AttributeBean> attributes = new ArrayList<AttributeBean>();
				attributeList.setAttribute(attributes);

			}

			if (attributeList.getAttribute().contains(attribute))
				throw new IllegalArgumentException("An attribute with this key already exists.");

			/**
			 * Find the last given id
			 */

			for (AttributeBean currentAttribute : attributeList.getAttribute()) {
				if (currentAttribute.getId() > lastId)
					lastId = currentAttribute.getId();
			}

			// get a higher one

			lastId++;

			attribute.setId(lastId);
			attributeList.getAttribute().add(attribute);

			newId = lastId;

		}
		catch (JAXBException e) {
			log.info("Could not deserialize attribute repository.");
			return null;
		}

		if (attributeList != null) {

			try {
				Marshaller m = JAXBContext.newInstance(AttributeList.class).createMarshaller();
				m.marshal(attributeList, attributeRepositoryFile);
			}
			catch (JAXBException e) {
				log.info("could not write attribute repository");
				return null;
			}

		}

		return newId;

	}

	@SuppressWarnings("unused")
	@Override
	public boolean delete(AttributeBean attribute) throws IllegalArgumentException {

		if (attribute == null)
			throw new IllegalArgumentException("Attribute must not be null.");
		if (attribute.getId() == null)
			throw new IllegalArgumentException("Attribute id must not be null.");

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		final String attributeRepository = config.getBasepath() + "WEB-INF/classes/attributes.xml";

		File attributeRepositoryFile = new File(attributeRepository);

		AttributeList attributeList = null;

		try {
			Unmarshaller u = JAXBContext.newInstance(AttributeList.class).createUnmarshaller();
			attributeList = (AttributeList) u.unmarshal(attributeRepositoryFile);

			for (Iterator<AttributeBean> it = attributeList.getAttribute().iterator(); it.hasNext();) {

				AttributeBean currentAttribute = it.next();

				if (currentAttribute.getId().equals(attribute.getId())) {
					it.remove();
				}

			}

		}
		catch (JAXBException e) {
			log.info("Could not deserialize attribute repository.");
			return false;
		}

		if (attributeList != null) {

			try {
				Marshaller m = JAXBContext.newInstance(AttributeList.class).createMarshaller();
				m.marshal(attributeList, attributeRepositoryFile);
				return true;
			}
			catch (JAXBException e) {
				log.info("could not write account repository");
				return false;
			}

		}
		return false;
	}

	@Override
	public ArrayList<AttributeBean> findAll() {

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		try {
			Unmarshaller u = (Unmarshaller) JAXBContext.newInstance(AttributeList.class).createUnmarshaller();

			final String attributeRepository = config.getBasepath() + "WEB-INF/classes/attributes.xml";

			AttributeList attributeList = (AttributeList) u.unmarshal(new File(attributeRepository));

			if (attributeList == null) {
				attributeList = new AttributeList();
				ArrayList<AttributeBean> attributes = new ArrayList<AttributeBean>();
				attributeList.setAttribute(attributes);
			}

			if (attributeList.getAttribute() == null) {
				ArrayList<AttributeBean> attributes = new ArrayList<AttributeBean>();
				attributeList.setAttribute(attributes);
			}

			return attributeList.getAttribute();

		}
		catch (JAXBException e) {
			log.error("Unable to create JAXB instance.");
		}

		return null;
	}

	@Override
	public AttributeBean findById(Long id) throws IllegalArgumentException {

		if (id == null)
			throw new IllegalArgumentException("Attribute id must not be null.");

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		try {
			Unmarshaller u = (Unmarshaller) JAXBContext.newInstance(AttributeList.class).createUnmarshaller();

			final String attributeRepository = config.getBasepath() + "WEB-INF/classes/attributes.xml";

			AttributeList attributes = (AttributeList) u.unmarshal(new File(attributeRepository));

			for (AttributeBean attribute : attributes.getAttribute()) {
				if (attribute.getId().equals(id))
					return attribute;
			}

		}
		catch (JAXBException e) {
			log.error("Unable to create JAXB instance.");
		}

		return null;
	}

	@Override
	public AttributeBean findByKey(String key) throws IllegalArgumentException {

		if (key == null)
			throw new IllegalArgumentException("Attribute key must not be null.");

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		try {
			Unmarshaller u = (Unmarshaller) JAXBContext.newInstance(AttributeList.class).createUnmarshaller();

			final String attributeRepository = config.getBasepath() + "WEB-INF/classes/attributes.xml";

			AttributeList attributes = (AttributeList) u.unmarshal(new File(attributeRepository));

			for (AttributeBean attribute : attributes.getAttribute()) {
				if (attribute.getKey().equals(key))
					return attribute;
			}

		}
		catch (JAXBException e) {
			log.error("Unable to create JAXB instance.");
		}

		return null;
	}

	@Override
	public boolean update(AttributeBean attribute) throws IllegalArgumentException {

		if (attribute == null)
			throw new IllegalArgumentException("Attribute must not be null.");
		if (attribute.getId() == null)
			throw new IllegalArgumentException("Attribute id must not be null.");
		if (attribute.getKey() == null || attribute.getKey().length() < 1)
			throw new IllegalArgumentException("You must provide a key for the attribute.");

		Config config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1);

		try {
			Unmarshaller u = (Unmarshaller) JAXBContext.newInstance(AttributeList.class).createUnmarshaller();

			final String attributeRepository = config.getBasepath() + "WEB-INF/classes/attributes.xml";

			AttributeList attributes = (AttributeList) u.unmarshal(new File(attributeRepository));

			for (AttributeBean currentAttribute : attributes.getAttribute()) {

				if (currentAttribute.getId().equals(attribute.getId())) {

					currentAttribute.setDataType(attribute.getDataType());
					currentAttribute.setDescription(attribute.getDescription());
					currentAttribute.setKey(attribute.getKey());
					currentAttribute.setValue(attribute.getValue());

					try {
						Marshaller m = JAXBContext.newInstance(AttributeList.class).createMarshaller();
						m.marshal(attributes, new File(attributeRepository));
						return true;
					}
					catch (JAXBException e) {
						log.info("could not write attribute repository");
						return false;
					}

				}

			}
		}
		catch (JAXBException e) {
			log.error("Unable to create JAXB instance.");
		}

		return false;

	}

}
