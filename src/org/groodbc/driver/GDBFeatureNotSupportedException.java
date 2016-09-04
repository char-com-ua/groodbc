package org.groodbc.driver;

import java.sql.SQLFeatureNotSupportedException;


class GDBFeatureNotSupportedException extends SQLFeatureNotSupportedException{
	public GDBFeatureNotSupportedException(){
		super("The rquested feature is not supported by current driver.");
	}
}
