package com.fortytwo.opent4c.tools;

import java.util.Calendar;

public class CalendarUtils {
	
	/**
	 * return a String from a timestamp in milliseconds
	 * format is (YEAR/MONTH/DAY,HOUR:MINUTE:SECONDS.MILLISECONDS)
	 * @param stamp
	 * @param micros 
	 * @return
	 */
	public static String getTimeStringFromLongMillis(long stamp, long micros){
		Calendar.getInstance().setTimeInMillis(stamp);
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		sb.append(Calendar.getInstance().get(Calendar.YEAR));
		sb.append("/");
		sb.append(Calendar.getInstance().get(Calendar.MONTH)+1);
		sb.append("/");
		sb.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		sb.append(",");
		sb.append(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		sb.append(":");
		sb.append(Calendar.getInstance().get(Calendar.MINUTE));
		sb.append(":");
		sb.append(Calendar.getInstance().get(Calendar.SECOND));
		sb.append(".");
		sb.append(Calendar.getInstance().get(Calendar.MILLISECOND));
		sb.append(".");
		sb.append(micros);
		sb.append(")");
		
		return sb.toString();
	}
	
	/**
	 * return a String for now
	 * format is (YEAR/MONTH/DAY,HOUR:MINUTE:SECONDS.MILLISECONDS)
	 * @return
	 */
	public static String getTimeStringFromNow(){
		Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		sb.append(Calendar.getInstance().get(Calendar.YEAR));
		sb.append("/");
		sb.append(Calendar.getInstance().get(Calendar.MONTH));
		sb.append("/");
		sb.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		sb.append(",");
		sb.append(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		sb.append(":");
		sb.append(Calendar.getInstance().get(Calendar.MINUTE));
		sb.append(":");
		sb.append(Calendar.getInstance().get(Calendar.SECOND));
		sb.append(".");
		sb.append(Calendar.getInstance().get(Calendar.MILLISECOND));
		sb.append(")");
		
		return sb.toString();
	}
}
