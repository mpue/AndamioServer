package org.pmedv.cms.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pmedv.cms.daos.DAOManager;
import org.pmedv.pojos.Config;


public class CMSProperties {
	
	private static CMSProperties instance = null;
	private Config config;
	private  Properties applicationProps = null;
	
	public Properties getAppProps() {
		return applicationProps;
	}

	private static boolean propsLoaded = false;
	private static final Log log = LogFactory.getLog(CMSProperties.class);
	
	public static CMSProperties getInstance() {
		if (instance == null) {
			instance = new CMSProperties();
		}
		return instance;
	}
	
	protected CMSProperties() {
		
		config = (Config)DAOManager.getInstance().getConfigDAO().findByID(1L);
		
		try {
			String propertiesLoc = config.getBasepath() + "WEB-INF/application.properties";
			log.info("Loading application.properties from " + propertiesLoc);
			FileInputStream is = new FileInputStream(new File(propertiesLoc));
			applicationProps = new Properties();
			applicationProps.load(is);
			propsLoaded = true;

		}
		catch (FileNotFoundException e1) {
			log.error("Could not load application.properties");
		}
		catch (IOException e1) {
			log.error("Could not load application.properties");
		}
		
	}
	
	public boolean isPropsLoaded() {
		return propsLoaded;
	}

}
