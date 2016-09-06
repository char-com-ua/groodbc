/** */
package org.groodbc.util; 

import org.groodbc.driver.GDBException; 
import java.sql.SQLException;
import java.util.List;

public class Types{
	
	public static Class getClass(List<Class> c, int param) throws SQLException {
		if(c==null)throw new GDBException("the class list is null. contact the driver developer.");
		if(param<1 || param>c.size())throw new GDBException("The requested parameter id="+param+" out of bound list.size="+c.size());
		return c.get( param-1 );
	}
	
	
	public static int getSqlType(List<Class> c, int param) throws SQLException {
		return getSqlType( getClass(c,param) );
	}
	public static int getSqlType(Class c) {
		if(c==null)return java.sql.Types.VARCHAR;
		if(String.class.isAssignableFrom(c))return java.sql.Types.VARCHAR;
		if(java.util.Date.class.isAssignableFrom(c))return java.sql.Types.DATE;
		if(Boolean.class.isAssignableFrom(c))return java.sql.Types.BOOLEAN;
		if(Number.class.isAssignableFrom(c))return java.sql.Types.DECIMAL;
		return java.sql.Types.JAVA_OBJECT;
	}
	
	
	public static String getSqlTypeName(List<Class> c, int param) throws SQLException {
		return getSqlTypeName( getClass(c,param) );
	}
	public static String getSqlTypeName(Class c) {
		if(c==null)return "VARCHAR";
		if(String.class.isAssignableFrom(c))return "VARCHAR";
		if(java.util.Date.class.isAssignableFrom(c))return "DATE";
		if(Boolean.class.isAssignableFrom(c))return "BOOLEAN";
		if(Number.class.isAssignableFrom(c))return "DECIMAL";
		return "JAVA_OBJECT";
	}
	
	
	public static boolean isSigned(List<Class> c, int param) throws SQLException {
		return isSigned( getClass(c,param) );
	}
	public static boolean isSigned(Class c) {
		return Number.class.isAssignableFrom(c);
	}
	
		
	public static int getPrecision(List<Class> c, int param) throws SQLException {
		return getPrecision( getClass(c,param) );
	}
    public static int getPrecision(Class c) {
    	if(Number.class.isAssignableFrom(c))return 48;
    	return 255;
    }

	public static int getScale(List<Class> c, int param) throws SQLException {
		return getScale( getClass(c,param) );
	}
    public static int getScale(Class c) {
    	if(Number.class.isAssignableFrom(c))return 8;
    	return 0;
    }
	
}
