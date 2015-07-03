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
		
		sb.append("[");
		sb.append(Calendar.getInstance().get(Calendar.YEAR));
		sb.append("/");
		int month = Calendar.getInstance().get(Calendar.MONTH)+1;
		if (month < 10) sb.append(0);
		sb.append(month);
		sb.append("/");
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if (day < 10) sb.append(0);
		sb.append(day);
		sb.append(",");
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		if (hour < 10) sb.append(0);
		sb.append(hour);
		sb.append(":");
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		if (min < 10) sb.append(0);
		sb.append(min);
		sb.append(":");
		int sec = Calendar.getInstance().get(Calendar.SECOND);
		if (sec < 10) sb.append(0);
		sb.append(sec);
		sb.append(".");
		int msec = Calendar.getInstance().get(Calendar.MILLISECOND);
		if (msec < 100) sb.append(0);
		if (msec < 10) sb.append(0);
		sb.append(msec);
		sb.append("] ");
		
		return sb.toString();
	}
}
