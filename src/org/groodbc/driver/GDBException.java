package org.groodbc.driver;


import java.sql.SQLException;


class GDBException extends SQLException 
{
	GDBException(){
    	super();
	}

	GDBException(String s){
    	super(s);
	}

	GDBException(String s,Throwable cause){
    	super(s);
    	this.initCause(cause);
	}
	
	public static void support() throws SQLException{
		throw new GDBFeatureNotSupportedException();
	}


} 
