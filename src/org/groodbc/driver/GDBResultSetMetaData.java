package org.groodbc.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.TreeSet;
//import java.sql.Types;
import org.groodbc.util.Types;

/**
 * An object that can be used to get information about the types 
 * and properties of the columns in a <code>ResultSet</code> object.
 * The following code fragment creates the <code>ResultSet</code> object rs,
 * creates the <code>ResultSetMetaData</code> object rsmd, and uses rsmd
 * to find out how many columns rs has and whether the first column in rs
 * can be used in a <code>WHERE</code> clause.
 * <PRE>
 *
 *     ResultSet rs = stmt.executeQuery("SELECT a, b, c FROM TABLE2");
 *     ResultSetMetaData rsmd = rs.getMetaData();
 *     int numberOfColumns = rsmd.getColumnCount();
 *     boolean b = rsmd.isSearchable(1);
 *
 * </PRE>
 */

public class GDBResultSetMetaData implements ResultSetMetaData {
	private Map<String,Integer> nameToIndex;
	private List<Class> indexToType;
	private List<String> indexToName;
	
	protected GDBResultSetMetaData(List<Map<String,Object>> rows) {
		if( rows.size()>0 ){
			nameToIndex = new HashMap();
			indexToName = new ArrayList( new TreeSet<String>( rows.get(0).keySet() ) );
			indexToType = new ArrayList(indexToName.size());
			for(int i=0;i<indexToName.size();i++){
				String name=indexToName.get(i);
				Class type = String.class;
				Object v = rows.get(0).get(name);
				if(v!=null)type=v.getClass();
				indexToType.add(type);
				nameToIndex.put(name,new Integer(i));
			}
			
		}else{
			nameToIndex = new HashMap();
			indexToName = new ArrayList();
			indexToType = new ArrayList();
		}
		//System.out.println("GDBResultSetMetaData() :: indexToName="+indexToName);
		//System.out.println("GDBResultSetMetaData() :: indexToType="+indexToType);
		//System.out.println("GDBResultSetMetaData() :: nameToIndex="+nameToIndex);
	}

    /**
     * Returns the number of columns in this <code>ResultSet</code> object.
     *
     * @return the number of columns
     * @exception SQLException if a database access error occurs
     */
    public int getColumnCount() throws SQLException{
    	return indexToName.size();
    }
    
    public String toString(){
    	String s="GDBResultSetMetaData[";
    	for(int i=0;i<indexToName.size();i++){
    		if(i>0)s+=", ";
    		s+=(String)indexToName.get(i);
    	}
    	return s+"]";
    }
    
    public int getColumnIndex(String name) throws SQLException{
    	//if column name maches a number then it's a column index. no check.
    	Integer i=nameToIndex.get(name);
    	if(i==null)throw new GDBException("column name '"+name+"' not found in "+this.toString());
    	return i.intValue()+1;
    }

    /**
     * Indicates whether the designated column is automatically numbered, thus read-only.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isAutoIncrement(int column) throws SQLException{
    	return false;
    }

    /**
     * Indicates whether a column's case matters.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isCaseSensitive(int column) throws SQLException{
    	return true;
    }
    
    /**
     * Indicates whether the designated column can be used in a where clause.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isSearchable(int column) throws SQLException{
    	return false;
    }

    /**
     * Indicates whether the designated column is a cash value.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isCurrency(int column) throws SQLException{
    	return false;
    }

    /**
     * Indicates the nullability of values in the designated column.		
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the nullability status of the given column; one of <code>columnNoNulls</code>,
     *          <code>columnNullable</code> or <code>columnNullableUnknown</code>
     * @exception SQLException if a database access error occurs
     */
    public int isNullable(int column) throws SQLException{
    	return ResultSetMetaData.columnNullable;
    }

    /**
     * Indicates whether values in the designated column are signed numbers.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isSigned(int column) throws SQLException{
    	return Types.isSigned( indexToType.get(column-1) );
    }

    /**
     * Indicates the designated column's normal maximum width in characters.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the normal maximum number of characters allowed as the width
     *          of the designated column
     * @exception SQLException if a database access error occurs
     */
    public int getColumnDisplaySize(int column) throws SQLException{
    	return Types.getPrecision( indexToType.get(column-1) );
    }

    /**
     * Gets the designated column's suggested title for use in printouts and
     * displays.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the suggested column title
     * @exception SQLException if a database access error occurs
     */
    public String getColumnLabel(int column) throws SQLException{
    	return indexToName.get(column-1);
    }

    /**
     * Get the designated column's name.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return column name
     * @exception SQLException if a database access error occurs
     */
    public String getColumnName(int column) throws SQLException{
    	return indexToName.get(column-1);
    }

    /**
     * Get the designated column's table's schema.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return schema name or "" if not applicable
     * @exception SQLException if a database access error occurs
     */
    public String getSchemaName(int column) throws SQLException{
    	return "";
    }

    /**
     * Get the designated column's number of decimal digits.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return precision
     * @exception SQLException if a database access error occurs
     */
    public int getPrecision(int column) throws SQLException{
    	return Types.getPrecision( indexToType.get(column-1) );
    }

    /**
     * Gets the designated column's number of digits to right of the decimal point.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return scale
     * @exception SQLException if a database access error occurs
     */
    public int getScale(int column) throws SQLException{
    	return Types.getScale( indexToType.get(column-1) );
    }

    /**
     * Gets the designated column's table name. 
     *
     * @param column the first column is 1, the second is 2, ...
     * @return table name or "" if not applicable
     * @exception SQLException if a database access error occurs
     */
    public String getTableName(int column) throws SQLException{
    	return "";
    }

    /**
     * Gets the designated column's table's catalog name.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the name of the catalog for the table in which the given column
     *          appears or "" if not applicable
     * @exception SQLException if a database access error occurs
     */
    public String getCatalogName(int column) throws SQLException{
    	return "";
    }

    /**
     * Retrieves the designated column's SQL type.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return SQL type from java.sql.Types
     * @exception SQLException if a database access error occurs
     * @see Types
     */
    public int getColumnType(int column) throws SQLException{
    	return Types.getSqlType( indexToType.get(column-1) );
    }

    /**
     * Retrieves the designated column's database-specific type name.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return type name used by the database. If the column type is
     * a user-defined type, then a fully-qualified type name is returned.
     * @exception SQLException if a database access error occurs
     */
    public String getColumnTypeName(int column) throws SQLException{
    	return Types.getSqlTypeName( indexToType.get(column-1) );
    }

    /**
     * Indicates whether the designated column is definitely not writable.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isReadOnly(int column) throws SQLException{
    	return true;
    }

    /**
     * Indicates whether it is possible for a write on the designated column to succeed.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isWritable(int column) throws SQLException{
    	return false;
    }

    /**
     * Indicates whether a write on the designated column will definitely succeed.	
     *
     * @param column the first column is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     */
    public boolean isDefinitelyWritable(int column) throws SQLException{
    	return false;
    }

    //--------------------------JDBC 2.0-----------------------------------

    /**
     * <p>Returns the fully-qualified name of the Java class whose instances 
     * are manufactured if the method <code>ResultSet.getObject</code>
     * is called to retrieve a value 
     * from the column.  <code>ResultSet.getObject</code> may return a subclass of the
     * class returned by this method.
     *
     * @param column the first column is 1, the second is 2, ...
     * @return the fully-qualified name of the class in the Java programming
     *         language that would be used by the method 
     * <code>ResultSet.getObject</code> to retrieve the value in the specified
     * column. This is the class name used for custom mapping.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public String getColumnClassName(int column) throws SQLException{
    	return indexToType.get(column-1).getName();
    }

    /*Wrapper*/
	public <T> T unwrap(java.lang.Class<T> iface) throws java.sql.SQLException {
		throw new GDBFeatureNotSupportedException();
	}

    public boolean isWrapperFor(java.lang.Class<?> iface) throws java.sql.SQLException{return false;}
    
}

