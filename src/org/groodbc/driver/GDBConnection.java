package org.groodbc.driver;

import java.sql.Connection;
import java.sql.Savepoint;
import java.sql.SQLException;
import java.util.Properties;

import java.util.concurrent.Executor;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.Map;


//import java.util.UUID;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.*;
//import java.io.*;
//import java.lang.*;


/**
 * Implements standard <code>java.sql.Connection</code> .
 * It passes all requests to database <code>connection</code> specified in constructor.
 * There are some features of connection managing:
 * <ul>
 * <li><code>timeout</code> property to check connection idle time
 * <li><code>isOld()</code> method to check does time of maximum inactivity passed.
 * <li><code>stackTrace</code> property that used if connection is not released to trace caller.
 * </ul>
 */
public class GDBConnection implements Connection {
	static GroovyShell shell = null; //new GroovyShell();
	static Map<String,Map<String,Class<Script>>> scriptCache = null; //nested map to use a weak hash map
	
	/** connection parameters */
	Object data;

	//last time in millis when connection has been established
	private long lastConnectTime=0;

	/**
	 * Creates <code>Connection</code>.
	 * @param dataSet the data object that will be passed to create prepared statement as $data
	 */
	public GDBConnection(Object data) {
		//System.out.println("GDBConnection() :: "+data);
		this.data = data;
	}

	public static GDBConnection eval(String scriptText) throws SQLException {
		return eval(scriptText,null);
	}
	public static GDBConnection eval(String scriptText, Map<String,Object> binding) throws SQLException {
		Object data = null;
		try {
			//data = groovy.util.Eval.me(script);
			Script script = getGroovyScript(scriptText,scriptText);
			Map vars=script.getBinding().getVariables();
			vars.clear();
			
			if(binding!=null && binding.size()>0){
				//set parameters for evaluation script
				vars.putAll(binding);
			}
			data = script.run();
		} catch(Throwable t){
			throw new GDBException("Failed to create script connection: "+t,t);
		}
		if(data==null)throw new GDBException("Failed to create script connection: data can't be null");
		return new GDBConnection( data );
	}
	
	//script key is a script without modifiers (modifiers are used in prepared statement)
	static Script getGroovyScript(String scriptText, String scriptKey) throws SQLException {
		try{
			if(shell==null){
				CompilerConfiguration conf = new CompilerConfiguration();
				conf.setDebug(true);
				shell = new GroovyShell(conf);
				//scriptCache = new ConcurrentHashMap(); //
				scriptCache = Collections.synchronizedMap( new WeakHashMap(1000) );
			}
			Map<String,Class<Script>> scriptsByKey = scriptCache.get(scriptKey);
			if(scriptsByKey==null){
				scriptsByKey = new ConcurrentHashMap(3);
				scriptCache.put(scriptKey, scriptsByKey);
				//System.out.println("scriptCache for key "+Long.toHexString(scriptKey.hashCode())+" created");
			}else{
				//System.out.println("scriptCache for key "+Long.toHexString(scriptKey.hashCode())+" exists");
			}
			Class<Script> clazz = scriptsByKey.get(scriptText);
			if(clazz==null){
				String scriptName="groodbc_"+Long.toHexString(scriptText.hashCode())+".groovy";
				clazz = (Class<Script>) shell.parse(scriptText, scriptName).getClass();
				scriptsByKey.put(scriptText,clazz);
				//System.out.println("script "+clazz+" created");
			}else{
				//System.out.println("script "+clazz+" exists");
			}
			
			Script script=(Script)clazz.newInstance();
			return script;
		}catch(Throwable t){
			throw new GDBException("Failed to parse groovy script: "+t,t);
		}
	}
	
    
    
    //Connection interface methods
    //All methods below this line are described in java.sql.Connection class
    /**
     * Method clearWarnings
     * standard <code>JDBC Connection</code> method
     * @throws SQLException
     */
    public void clearWarnings() throws SQLException {
        //nothing to do
    }

    /**
     * Method close
     * returns this connection to ConnectionPool
     * @throws SQLException
     */
    public void close() {
        data = null;
    }

    /**
     * Method commit
     * standard <code>JDBC Connection</code> method
     * @throws SQLException
     */
    public void commit() {
        //no commit
    }

    /**
     * Method createStatement
     * standard <code>JDBC Connection</code> method
     *
     * @return
     * @throws SQLException
     */
    public java.sql.Statement createStatement() throws SQLException {
        return new GDBPreparedStatement(this, null);
    }

    /**
     * Method createStatement
     * standard <code>JDBC Connection</code> method
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     */
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return new GDBPreparedStatement(this, null);
    }

    /**
     * Method createStatement
     * standard <code>JDBC Connection</code> method
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     */
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new GDBPreparedStatement(this, null);
    }

    /**
     * Method getAutoCommit
     * standard <code>JDBC Connection</code> method
     * @return
     * @throws SQLException
     */
    public boolean getAutoCommit() {
        return true;
    }

    /**
     * Method getCatalog
     * @return
     * @throws SQLException
     */
    public String getCatalog() {
        return null; //no catalogs
    }

    /**
     * Method getMetaData
     * standard <code>JDBC Connection</code> method
     * @return
     * @throws SQLException
     */
    public java.sql.DatabaseMetaData getMetaData() {
        //not supported yet. maybe in the future.
        return new GDBDatabaseMetaData(this);
    }

    /**
     * Method getTransactionIsolation
     * standard <code>JDBC Connection</code> method
     * @return
     * @throws SQLException
     */
    public int getTransactionIsolation() {
        return 0;
    }

    /**
     * Method getTypeMap
     * standard <code>JDBC Connection</code> method
     * @return
     * @throws SQLException
     */
    public java.util.Map getTypeMap() {
        return new java.util.HashMap();
    }

    /**
     * Method getWarnings
     * standard <code>JDBC Connection</code> method
     * @return
     * @throws SQLException
     */
    public java.sql.SQLWarning getWarnings() {
        return null;
    }

    /**
     * Method isClosed
     * standard <code>JDBC Connection</code> method
     * @return
     * @throws SQLException
     */
    public boolean isClosed() {
        return data == null;
    }

    /**
     * Method isReadOnly
     * standard <code>JDBC Connection</code> method
     * @return
     * @throws SQLException
     */
    public boolean isReadOnly() {
        return false;
    }

    /**
     * Method nativeSQL
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @return
     * @throws SQLException
     */
    public String nativeSQL(String sql) throws SQLException {
        //???  not supported yet. should be   ???
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Method prepareCall
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @return
     * @throws SQLException
     */
    public java.sql.CallableStatement prepareCall(String sql) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Method prepareCall
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     */
    public java.sql.CallableStatement prepareCall(
            String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        //not supported
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Method prepareCall
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     */
    public java.sql.CallableStatement prepareCall(
            String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        //not supported
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Method prepareStatement
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @return
     * @throws SQLException
     */
    public java.sql.PreparedStatement prepareStatement(String sql) throws SQLException {
        return new GDBPreparedStatement(this, sql);
    }

    /**
     * Method prepareStatement
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @return
     * @throws SQLException
     */
    public java.sql.PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        //maybe for the future
        return new GDBPreparedStatement(this, sql);
    }

    /**
     * Method prepareStatement
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @return
     * @throws SQLException
     */
    public java.sql.PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
        //later
        return new GDBPreparedStatement(this, sql);
    }

    /**
     * Method prepareStatement
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @return
     * @throws SQLException
     */
    public java.sql.PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        //no such a keys.
        return new GDBPreparedStatement(this, sql);
    }

    /**
     * Method prepareStatement
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     */
    public java.sql.PreparedStatement prepareStatement(
            String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        //not supported.
        return new GDBPreparedStatement(this, sql);
    }

    /**
     * Method prepareStatement
     * standard <code>JDBC Connection</code> method
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws SQLException
     */
    public java.sql.PreparedStatement prepareStatement(
            String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        //no support.
        return new GDBPreparedStatement(this, sql);
    }

    /**
     * Method rollback
     * standard <code>JDBC Connection</code> method
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        //nothing to do
    }

    /**
     * Method rollback
     * standard <code>JDBC Connection</code> method
     * @throws SQLException
     */
    public void rollback(Savepoint savepoint) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Changes the holdability of <code>ResultSet</code> objects
     * created using this <code>Connection</code> object to the given
     * holdability.
     *
     * @param holdability a <code>ResultSet</code> holdability constant; one of
     *        <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *        <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException if a database access occurs, the given parameter
     *         is not a <code>ResultSet</code> constant indicating holdability,
     *         or the given holdability is not supported
     * @see #getHoldability
     * @see ResultSet
     * @since 1.4
     */
    public void setHoldability(int holdability) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Retrieves the current holdability of <code>ResultSet</code> objects
     * created using this <code>Connection</code> object.
     *
     * @return the holdability, one of
     *        <code>ResultSet.HOLD_CURSORS_OVER_COMMIT</code> or
     *        <code>ResultSet.CLOSE_CURSORS_AT_COMMIT</code>
     * @throws SQLException if a database access occurs
     * @see #setHoldability
     * @see ResultSet
     * @since 1.4
     */
    public int getHoldability() throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Creates an unnamed savepoint in the current transaction and 
     * returns the new <code>Savepoint</code> object that represents it.
     *
     * @return the new <code>Savepoint</code> object
     * @exception SQLException if a database access error occurs
     *            or this <code>Connection</code> object is currently in
     *            auto-commit mode
     * @see Savepoint
     * @since 1.4
     */
    public Savepoint setSavepoint() throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Creates a savepoint with the given name in the current transaction
     * and returns the new <code>Savepoint</code> object that represents it.
     *
     * @param name a <code>String</code> containing the name of the savepoint
     * @return the new <code>Savepoint</code> object
     * @exception SQLException if a database access error occurs
     *            or this <code>Connection</code> object is currently in
     *            auto-commit mode
     * @see Savepoint
     * @since 1.4
     */
    public Savepoint setSavepoint(String name) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Removes the given <code>Savepoint</code> object from the current 
     * transaction. Any reference to the savepoint after it have been removed 
     * will cause an <code>SQLException</code> to be thrown.
     *
     * @param savepoint the <code>Savepoint</code> object to be removed
     * @exception SQLException if a database access error occurs or
     *            the given <code>Savepoint</code> object is not a valid 
     *            savepoint in the current transaction
     * @since 1.4
     */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Method setAutoCommit
     * standard <code>JDBC Connection</code> method
     * @param b
     * @throws SQLException
     */
    public void setAutoCommit(boolean b) throws SQLException {
        //always autocommit
    }

    /**
     * Method setCatalog
     * standard <code>JDBC Connection</code> method
     * @param catalog
     * @throws SQLException
     */
    public void setCatalog(String catalog) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Method setReadOnly
     * standard <code>JDBC Connection</code> method
     * @param b
     * @throws SQLException
     */
    public void setReadOnly(boolean b) throws SQLException {
        if(!b)throw new GDBFeatureNotSupportedException();
    }

    /**
     * Method setTransactionIsolation
     * standard <code>JDBC Connection</code> method
     * @param i
     * @throws SQLException
     */
    public void setTransactionIsolation(int i) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    /**
     * Method setTypeMap
     * standard <code>JDBC Connection</code> method
     * @param map
     * @throws SQLException
     */
    public void setTypeMap(java.util.Map map) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    public java.sql.Clob createClob() throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    public java.sql.Blob createBlob() throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    public boolean isValid(int timeout) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    public java.sql.Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    public java.sql.Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    public <T> T unwrap(java.lang.Class<T> iface) throws java.sql.SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }

    public boolean isWrapperFor(java.lang.Class<?> iface) throws java.sql.SQLException {
        //no support.
        throw new GDBFeatureNotSupportedException();
    }


    //finalization
    protected void finalize() {
    	try{
	    	this.close();
    	}catch(Exception e){}
    }


        
    //JAVA 6 VERSION
    
	public java.sql.NClob createNClob() throws SQLException{
		//no support.
		throw new GDBFeatureNotSupportedException();
	}


	public java.sql.SQLXML createSQLXML() throws SQLException{
		//no support.
		throw new GDBFeatureNotSupportedException();
	}

	public String getClientInfo(String name) throws SQLException{
		//no support.
		throw new GDBFeatureNotSupportedException();
	}


	public java.util.Properties getClientInfo() throws SQLException{
		//no support.
		throw new GDBFeatureNotSupportedException();
	}

	public void setClientInfo(String name, String value) throws java.sql.SQLClientInfoException{
		//no support.
	}

	public void setClientInfo(java.util.Properties properties) throws java.sql.SQLClientInfoException{
		//no support.
	}
	
    // JAVA 7
    
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException{}

    public int getNetworkTimeout() throws SQLException{ return 0; }
	
    public void abort(Executor executor) throws SQLException{}
    
    public void setSchema(String schema) throws SQLException{}
    public String getSchema() throws SQLException{return "groovy";}
    
}
