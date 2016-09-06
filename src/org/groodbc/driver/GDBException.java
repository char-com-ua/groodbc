package org.groodbc.driver;


import java.sql.SQLException;


public class GDBException extends SQLException {
	public GDBException(String s){
    	super(s);
		//this.printStackTrace(System.out);
	}

	public GDBException(String s,Throwable cause){
    	super(s);
    	this.initCause(cause);
		//this.printStackTrace(System.out);
	}
} 
