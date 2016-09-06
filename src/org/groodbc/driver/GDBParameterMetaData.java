package org.groodbc.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.util.TreeSet;
//import java.sql.Types;
import org.groodbc.util.Types;

public class GDBParameterMetaData implements ParameterMetaData {
	LinkedHashMap<String,Class> params;
	ArrayList<Class> types = new ArrayList();
	ArrayList<String> names = new ArrayList();
	
	protected GDBParameterMetaData(LinkedHashMap<String,Class> map) {
		params = (map==null?new LinkedHashMap():map);
		for(Map.Entry<String,Class> e : params.entrySet()){
			names.add(e.getKey());
			types.add(e.getValue());
		}
		//System.out.println("GDBParameterMetaData() :: "+map);
	}
	public String toString(){
		return params.toString();
	}

    /**
     * Retrieves the number of parameters in the <code>PreparedStatement</code>
     * object for which this <code>ParameterMetaData</code> object contains
     * information.
     *
     * @return the number of parameters
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int getParameterCount() throws SQLException{
    	return params.size();
    }

    /**
     * Retrieves whether null values are allowed in the designated parameter.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return the nullability status of the given parameter; one of
     *        <code>ParameterMetaData.parameterNoNulls</code>,
     *        <code>ParameterMetaData.parameterNullable</code>, or
     *        <code>ParameterMetaData.parameterNullableUnknown</code>
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int isNullable(int param) throws SQLException{
    	return parameterNullable;
    }

    /**
     * Retrieves whether values for the designated parameter can be signed numbers.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return <code>true</code> if so; <code>false</code> otherwise
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public boolean isSigned(int param) throws SQLException{
    	return Types.isSigned( types, param );
    }

    /**
     * Retrieves the designated parameter's specified column size.
     *
     * <P>The returned value represents the maximum column size for the given parameter.
     * For numeric data, this is the maximum precision.  For character data, this is the length in characters.
     * For datetime datatypes, this is the length in characters of the String representation (assuming the
     * maximum allowed precision of the fractional seconds component). For binary data, this is the length in bytes.  For the ROWID datatype,
     * this is the length in bytes. 0 is returned for data types where the
     * column size is not applicable.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return precision
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int getPrecision(int param) throws SQLException{
    	return Types.getPrecision( types, param );
    }

    /**
     * Retrieves the designated parameter's number of digits to right of the decimal point.
     * 0 is returned for data types where the scale is not applicable.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return scale
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int getScale(int param) throws SQLException{
    	return Types.getScale( types, param );
    }

    /**
     * Retrieves the designated parameter's SQL type.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return SQL type from <code>java.sql.Types</code>
     * @exception SQLException if a database access error occurs
     * @since 1.4
     * @see Types
     */
    public int getParameterType(int param) throws SQLException{
    	return Types.getSqlType( types, param );
    }

    /**
     * Retrieves the designated parameter's database-specific type name.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return type the name used by the database. If the parameter type is
     * a user-defined type, then a fully-qualified type name is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public String getParameterTypeName(int param) throws SQLException{
    	return Types.getSqlTypeName( types, param );
    }


    /**
     * Retrieves the fully-qualified name of the Java class whose instances
     * should be passed to the method <code>PreparedStatement.setObject</code>.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return the fully-qualified name of the class in the Java programming
     *         language that would be used by the method
     *         <code>PreparedStatement.setObject</code> to set the value
     *         in the specified parameter. This is the class name used
     *         for custom mapping.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public String getParameterClassName(int param) throws SQLException{
    	return types.get(param-1).getName();
    }

    /**
     * Retrieves the designated parameter's mode.
     *
     * @param param the first parameter is 1, the second is 2, ...
     * @return mode of the parameter; one of
     *        <code>ParameterMetaData.parameterModeIn</code>,
     *        <code>ParameterMetaData.parameterModeOut</code>, or
     *        <code>ParameterMetaData.parameterModeInOut</code>
     *        <code>ParameterMetaData.parameterModeUnknown</code>.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public int getParameterMode(int param) throws SQLException{
    	return parameterModeIn;
    }

    /*Wrapper*/
	public <T> T unwrap(java.lang.Class<T> iface) throws java.sql.SQLException {
		throw new GDBFeatureNotSupportedException();
	}

    public boolean isWrapperFor(java.lang.Class<?> iface) throws java.sql.SQLException{return false;}
    
}

