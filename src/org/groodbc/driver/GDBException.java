package org.groodbc.driver;


import java.sql.SQLException;


class GDBException extends SQLException 
{
	GDBException(String s){
    	super(s);
		//this.printStackTrace(System.out);
	}

	GDBException(String s,Throwable cause){
    	super(s);
    	this.initCause(cause);
		//this.printStackTrace(System.out);
	}
} 
