package org.groodbc.driver;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Enumeration;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;

import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Logger;


/**
 * A DBProxyDriver class provides all <code>java.sql.Driver</code> requirements.
 * <P>
 */
public class GDBDriver implements Driver{

	private static final String myUrl="groodbc:org:";
	public Properties conInfo;
	
	//self register driver
	private static GDBDriver m_defaultDrvr=null;
	static{
		try {
			if(m_defaultDrvr==null){
				m_defaultDrvr=new GDBDriver();
				DriverManager.registerDriver(m_defaultDrvr);
			}
		}catch(Exception e){
			System.err.println("org.groodbc.driver.GDBDriver Exception:");
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Constructs a new Driver.
	 * @throws SQLException if any sql exception occured
	 */
	public GDBDriver() throws SQLException{
	}


	/**
	 * Returns true if the driver thinks that it can open
	 * a connection to the given URL.
	 * @param url the database connection url to check.
	 * @throws SQLException if any sql exception occured
	 */
	public boolean acceptsURL(String url) throws SQLException{
		if(url==null)return false;
		return url.startsWith(myUrl);
	}

	/**
	 * Attempts to make a database connection to the given URL
	 * @param url database connection url.
	 * @param info database connection parameters (user, password, etc.).
	 * @return a new database connection
	 * @throws SQLException if any sql exception occured
	 * @see java.sql.Driver
	 */
	public Connection connect(String url, Properties info) throws SQLException{
		if(!acceptsURL(url))return null;
		String script=url.substring(myUrl.length());
		return GDBConnection.eval(script);
	}

	/**
	 * Gets the driver's major version number
	 * @return the driver's major version number
	 */
	public int getMajorVersion(){
		return 2;
	}

	/**
	 * Gets the driver's minor version number
	 * @return the driver's minor version number
	 */
	public int getMinorVersion(){
		return 0;
	}

	public Properties getConnInfo(){
		return null;
	}
	
	
	/**
	 * Gets information about the possible properties for this driver
	 * @param url database connection url.
	 * @param info database connection parameters.
	 * @see java.sql.Driver
	 */
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException{
		return new DriverPropertyInfo[0];
	}

	/**
	 * Reports whether this driver is a genuine JDBC COMPLIANT driver
	 * @see java.sql.Driver
	 */
	public boolean jdbcCompliant(){
		return true;
	}
	
    public Logger getParentLogger() throws SQLFeatureNotSupportedException{
    	throw new GDBFeatureNotSupportedException();
    }
}

