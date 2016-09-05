package org.groodbc.driver;


import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.io.Reader;
import java.io.InputStream;
import groovy.lang.Script;


public class GDBPreparedStatement implements PreparedStatement {
	
	//original sql statement
	private String sql=null;
	private GDBConnection con;
	private Map param;
    //private ResultSet result = null;
    private GDBResultSet result = null;

	
	protected GDBPreparedStatement(GDBConnection con, String sql)throws SQLException{
		this.con=con;
		setSql(sql);
	}
	
	
	protected void setSql(String sql){
		this.result=null;
		param = new LinkedHashMap();
		this.sql=sql;
	}

    public boolean execute() throws SQLException{
    	Script script = con.getGroovyScript( sql );
    	//prepare binding
    	Map vars=script.getBinding().getVariables();
		vars.clear();
		vars.put("data",con.data);
		vars.put("param",param);
		
    	List rows = (List)script.run();
    	
    	if (rows!=null){
    		result = new GDBResultSet(rows);
    		return true;
    	}else{
    		result = null;
    		return false;
    	}
    	
    }

    public ResultSet executeQuery() throws SQLException{
    	execute();
    	return getResultSet();
    }
    
    public int executeUpdate() throws SQLException{
    	execute();
    	return 1;
    }
    
    public void close() throws SQLException {
        if (this.result != null) {
            this.result.close();
        }
        result = null;
    }
    
    public ResultSet getResultSet() throws SQLException {
    	ResultSet r=this.result;
    	this.result=null;
        return r;
    }
    
    public boolean getMoreResults() throws SQLException {
        return this.result!=null;
    }

    public int getUpdateCount() throws SQLException {
        return -1;
    }
    
	/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
	/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!===PREPARED STATEMENT===!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

    public void setNull(int parameterIndex, int sqlType) throws SQLException{
    	param.put( new Integer(parameterIndex),null);
    }
    
    public void setObject(int parameterIndex, Object x) throws SQLException{
    	param.put( new Integer(parameterIndex),x);
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException{
    	setObject(parameterIndex, x);
    }
    
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException{
    	 setObject(parameterIndex, x);
    }
    
    public void setString(int parameterIndex, String x) throws SQLException{
    	param.put( new Integer(parameterIndex), x);
    }

    public void setShort(int parameterIndex, short x) throws SQLException{
    	param.put( new Integer(parameterIndex),new Short(x));
    }

    public void setInt(int parameterIndex, int x) throws SQLException{
    	param.put( new Integer(parameterIndex), new Integer(x));
    }

    public void setLong(int parameterIndex, long x) throws SQLException{
    	param.put( new Integer(parameterIndex), new Long(x));
    }
    
    public void setFloat(int parameterIndex, float x) throws SQLException{
    	param.put( new Integer(parameterIndex), new Float(x));
    }

    public void setDouble(int parameterIndex, double x) throws SQLException{
    	param.put( new Integer(parameterIndex), new Double(x));
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException{
    	param.put( new Integer(parameterIndex), x);
    }

    public void setDate(int parameterIndex, java.sql.Date x) throws SQLException{
		param.put( new Integer(parameterIndex), x );
    }

    public void clearParameters() throws SQLException{
    	param.clear();
    }

    public ResultSetMetaData getMetaData() throws SQLException{
    	return null;
    }

    public void setDate(int parameterIndex, java.sql.Date x, Calendar cal) throws SQLException{
    	setDate(parameterIndex, x);
    }


	public void setNull (int parameterIndex, int sqlType, String typeName) throws SQLException{
		setNull(parameterIndex,sqlType);
    }

    public void clearWarnings() throws SQLException {
    }
    
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }
    
    public int getQueryTimeout() throws SQLException {
    	return 0;
    }
    
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    public int getMaxRows() throws SQLException {
        return 0;
    }


    /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!====NOT SUPPORTED====!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/

    public void setMaxFieldSize(int max) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }
    public void setMaxRows(int max) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }
    public void setEscapeProcessing(boolean enable) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }
    public void setQueryTimeout(int seconds) throws SQLException {
    }
    public void cancel() throws SQLException {
    }
    public ResultSet executeQuery(String sql) throws SQLException {
    	this.setSql(sql);
    	return executeQuery();
    }
    public int executeUpdate(String sql) throws SQLException {
    	throw new GDBFeatureNotSupportedException();
    }
    public boolean execute(String sql) throws SQLException {
    	this.setSql(sql);
    	return execute();
    }
    public void setCursorName(String name) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }
    
    public void setBytes(int parameterIndex, byte x[]) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException{
    	param.put( new Integer(parameterIndex), new Boolean(x));
    }

    public void setByte(int parameterIndex, byte x) throws SQLException{
    	param.put( new Integer(parameterIndex), new Byte(x));
    }

    public void setTime(int parameterIndex, java.sql.Time x) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setTimestamp(int parameterIndex, java.sql.Timestamp x) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public void setTime(int parameterIndex, java.sql.Time x, Calendar cal) 
	    throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public void setTimestamp(int parameterIndex, java.sql.Timestamp x, Calendar cal)
	    throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public void setURL(int parameterIndex, java.net.URL x) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public ParameterMetaData getParameterMetaData() throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public void addBatch() throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public void setRef (int parameterIndex, Ref x) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public void setBlob (int parameterIndex, Blob x) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public void setClob (int parameterIndex, Clob x) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
    public void setArray (int parameterIndex, Array x) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }


    //--------------------------JDBC 2.0-----------------------------
    public void setFetchDirection(int direction) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }
    
    public int getFetchDirection() throws SQLException {
        return ResultSet.FETCH_FORWARD;
    }

    public void setFetchSize(int rows) throws SQLException {
        //throw new GDBFeatureNotSupportedException();
    }

    public int getFetchSize() throws SQLException {
        return 1;
    }

    public int getResultSetConcurrency() throws SQLException {
        return ResultSet.CONCUR_READ_ONLY;
    }

    public int getResultSetType() throws SQLException {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    public void addBatch(String sql) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public void clearBatch() throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public int[] executeBatch() throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public Connection getConnection() throws SQLException {
        return con;
    }

    public boolean getMoreResults(int current) throws SQLException {
        return getMoreResults();
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public int executeUpdate(String sql, int columnIndexes[]) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public int executeUpdate(String sql, String columnNames[]) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public boolean execute(String sql, int columnIndexes[]) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public boolean execute(String sql, String columnNames[]) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public int getResultSetHoldability() throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }
    
    public boolean isClosed() throws SQLException {
        return result == null;
    }

    public void setPoolable(boolean poolable) throws SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public boolean isPoolable() throws SQLException {
        return false;
    }

    //!!!!!  Wrapper    !!!!!!
    public <T> T unwrap(java.lang.Class<T> iface) throws java.sql.SQLException {
        throw new GDBFeatureNotSupportedException();
    }

    public boolean isWrapperFor(java.lang.Class<?> iface) throws java.sql.SQLException {
        throw new GDBFeatureNotSupportedException();
    }  
    
    
    
	/* java 6 */    
    
    
    

    public void setRowId(int parameterIndex, RowId x) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

 
    public void setNString(int parameterIndex, String value) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }


    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setNClob(int parameterIndex, NClob value) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }


    public void setClob(int parameterIndex, Reader reader, long length)
        throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }


    public void setBlob(int parameterIndex, InputStream inputStream, long length)
        throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }


    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }


    public void setAsciiStream(int parameterIndex, java.io.InputStream x, long length)
	    throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setBinaryStream(int parameterIndex, java.io.InputStream x, 
			 long length) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setCharacterStream(int parameterIndex,
       			  java.io.Reader reader,
			  long length) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setAsciiStream(int parameterIndex, java.io.InputStream x)
	    throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setBinaryStream(int parameterIndex, java.io.InputStream x)
    throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setCharacterStream(int parameterIndex,
       			  java.io.Reader reader) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }


    public void setClob(int parameterIndex, Reader reader)
       throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }


    public void setBlob(int parameterIndex, InputStream inputStream)
        throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }

    public void setNClob(int parameterIndex, Reader reader)
       throws SQLException{
    	throw new GDBFeatureNotSupportedException();
    }
       
    public void closeOnCompletion() throws SQLException{}
    public boolean isCloseOnCompletion() throws SQLException{return true;}
}
