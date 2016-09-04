/** */
package org.groodbc.util; 

import java.util.Date;
import java.text.SimpleDateFormat;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.lang.reflect.InvocationTargetException;

public class Strings{
	
	public static java.util.Date toDate(String value, String format) {
		SimpleDateFormat sdf;
		
		if(value == null || value.trim().length() == 0)return null;
		if(format==null)return toDate(value);
	    try{
	    	sdf = new SimpleDateFormat(format);
	    	return sdf.parse(value);
		}catch(Exception e){}
		return null;
	}
	
	public static java.util.Date toDate(String value) {
		SimpleDateFormat sdf;
		
		if(value == null || value.trim().length() == 0)return null;
		
		//value=value.replaceAll("[\\-\\+]\\d{2}:\\d{2}$","");  //TODO: in the future it's required to parse timezone and not to skip it...
		int len=value.length();
		if(len>11 && value.charAt(10)=='T'){
			value=value.substring(0,10)+" "+value.substring(11);
		}
		
		if(len==19){
			try{
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return sdf.parse(value);
			}catch(Exception e){}
			try{
				sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				return sdf.parse(value);
			} catch (Exception e) {}
		}else if(len==10){
			try{
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.parse(value);
			}catch(Exception e){}
			try{
				sdf = new SimpleDateFormat("dd.MM.yyyy");
				return sdf.parse(value);
			} catch (Exception e) {}
		}else if(len==16){
			try{
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				return sdf.parse(value);
			}catch(Exception e){}
			try{
				sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				return sdf.parse(value);
			} catch (Exception e) {}
		}else if(len>19){
			try {
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				return sdf.parse(value);
			} catch (Exception e) {}
			
			return toDate(value.substring(0,19));
		}
		throw new RuntimeException("Can't parse date `"+value+"`");
	}
	
}