/**

	Weberknecht AndamioManager - Open Source Content Management
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataSourcePlugin extends AbstractPlugin implements IPlugin,
		Serializable {

	private static final long serialVersionUID = 1582971809710772946L;

	protected static Log log = LogFactory.getLog(DataSourcePlugin.class);

	private DataSource dataSource = null;

	public DataSourcePlugin() {

		super();
		pluginID = "DataSourcePlugin";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.pmedv.plugins.IPlugin#getContent()
	 */
	public String getContent() {

		try {
			return getData();
		} catch (NamingException e) {
			return "Could not obtain database";
		}
	}

	private String getData() throws NamingException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		StringBuffer dataBuffer = new StringBuffer();

		InitialContext initCtx = new InitialContext();
		Context ctx = (Context) initCtx.lookup("java:comp/env");

		if (ctx == null) {
			log.error("No InitialContext available");
		} else {
			dataSource = (DataSource) ctx.lookup("jdbc/localhostDB");
		}

		Connection conn = null;
		Statement stmt = null; // Or PreparedStatement if needed
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from users");

			while (rs.next()) {

				for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
					dataBuffer.append(rs.getObject(i) + " ");
					dataBuffer.append("<br/>");
				}

			}

			rs.close();
			rs = null;
			stmt.close();
			stmt = null;
			conn.close(); // Return to connection pool
			conn = null; // Make sure we don't close it twice

		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} 
		finally {
			// Always make sure result sets and statements are closed,
			// and the connection is returned to the pool
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					;
				}
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					;
				}
				stmt = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} 
				catch (SQLException e) {
					;
				}
				conn = null;
			}
		}

		return dataBuffer.toString();
	}

}
