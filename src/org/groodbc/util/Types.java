/** */
package org.groodbc.util; 

public class Types{
	
	
	public static int getSqlType(Class c) {
		if(c==null)return java.sql.Types.VARCHAR;
		if(String.class.isAssignableFrom(c))return java.sql.Types.VARCHAR;
		if(java.util.Date.class.isAssignableFrom(c))return java.sql.Types.DATE;
		if(Boolean.class.isAssignableFrom(c))return java.sql.Types.BOOLEAN;
		if(Number.class.isAssignableFrom(c))return java.sql.Types.DECIMAL;
		return java.sql.Types.JAVA_OBJECT;
	}
	
	public static String getSqlTypeName(Class c) {
		if(c==null)return "VARCHAR";
		if(String.class.isAssignableFrom(c))return "VARCHAR";
		if(java.util.Date.class.isAssignableFrom(c))return "DATE";
		if(Boolean.class.isAssignableFrom(c))return "BOOLEAN";
		if(Number.class.isAssignableFrom(c))return "DECIMAL";
		return "JAVA_OBJECT";
	}
	
	
	
	public static boolean isSigned(Class c) {
		return Number.class.isAssignableFrom(c);
	}
	
		
    public static int getPrecision(Class c) {
    	if(Number.class.isAssignableFrom(c))return 48;
    	return 255;
    }

    public static int getScale(Class c) {
    	if(Number.class.isAssignableFrom(c))return 8;
    	return 0;
    }
	
}