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

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mysql.jdbc.Connection;

/**
 * 
 * @author Matthias Pueski
 *
 */

public class SchemaInstaller {
 
	private static final Log log = LogFactory.getLog(SchemaInstaller.class);
	
	private static final String driverClass = "com.mysql.jdbc.Driver";

    private Connection conn;
    private String dbUrl;
    private String dbUser;
    private String dbPass;
    private String schemaLocation;
    private String databaseName;
    private boolean createDatabase = false;
    
	/**
     * Creates a new SchemaInstaller in order to install the system database schema.
     * 
     * @param schemaLocation  the path, where the schema file exists 
     * @param host        the database host
     * @param port		  the port of the database host (usually 3306)
     * @param dbName      the name of the database to use
     * @param dbUser      the user of the database schema
     * @param dbPass      the password for the user
     */
    public SchemaInstaller(String schemaLocation, String dbUrl, String dbUser, String dbPass) {
    	this.schemaLocation = schemaLocation;
    	this.dbUrl = dbUrl;    	
    	this.dbUser = dbUser;
    	this.dbPass = dbPass;
    }
    
	/**
     * Creates a new SchemaInstaller in order to install the system database schema.
     * 
     * @param databaseName   the name of the database to be created
     * @param schemaLocation the path, where the schema file exists 
     * @param host        the database host
     * @param port		  the port of the database host (usually 3306)
     * @param dbName      the name of the database to use
     * @param dbUser      the user of the database schema
     * @param dbPass      the password for the user
     */
    public SchemaInstaller(String databaseName,String schemaLocation, String dbUrl, String dbUser, String dbPass, boolean create) {
    	this.databaseName = databaseName;
    	this.schemaLocation = schemaLocation;
    	this.dbUrl = dbUrl;    	
    	this.dbUser = dbUser;
    	this.dbPass = dbPass;
    	this.createDatabase = create;
    }

    
    /**
     * Connects to the database.
     * 
     * @throws SQLException
     */
    private void connect() throws SQLException{
    	
        try {
            Class.forName(driverClass);
            conn = (Connection) DriverManager.getConnection(dbUrl +"?user=" + dbUser + "&password=" + dbPass);
            log.info("loaded driver : "+driverClass);
        }
        catch (ClassNotFoundException e) {
        	log.info("could not find driver class "+driverClass);
        }
        catch (SQLException e) {
        	log.info("An SQL exception occured during install.");
            e.printStackTrace();
        }
        
    }

    /**
     * Executes an sql query
     * 
     * @param query  the single query to execute
     * @return       true if the query was successful
     * @throws SQLException
     */
    private boolean doSQL(String query) throws SQLException {
    	
        try {
            java.sql.Statement stat = conn.createStatement();

            log.debug("executing query : "+query);

            stat.executeUpdate(query);
            stat.close();
            
            return true;
        }
        catch (Exception e) {
        	log.info("An SQL exception occured during install.");
        	e.printStackTrace();
            throw new RuntimeException("Unable to install.");
        }        
        
    }
    
    /**
     * Starts the installation.
     */
    public boolean doInstall () {
    	
        try {
        	
        	
            if (createDatabase) {
            	
            	String oldDbUrl = dbUrl;
            	
            	dbUrl = "jdbc:mysql://localhost:3306/mysql";
            	
            	connect();
            	
            	String createQuery = "create database "+databaseName;            
            	doSQL(createQuery);
            	
                conn.close();
                
                dbUrl = oldDbUrl;
            }
            
        	
            connect();
            
            String contents;
            
			try {
				contents = FileUtils.readFileToString(new File(schemaLocation),"ISO-8859-1");
			}
			catch (IOException e) {
				log.info("I/O Exception occured, aborting install.");
				return false;
			}
            StringTokenizer st = new StringTokenizer(contents,";");

            while (st.hasMoreElements()) {

                String  s = st.nextToken();
                doSQL(s);
            }

            conn.close();
            return true;
        }
        catch (SQLException ex) {
        	log.info("An SQL exception occured during install.");        	
        	return false;
        }

    }

}
