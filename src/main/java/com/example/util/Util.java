package com.example.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/*
 * @author  Pavan PC
 * @Date 	27-Sep-2015
 * 
 * Util class
 */
public class Util {

	public static Date getTimeAfterNSeconds(int seconds) {
		Calendar calender = Calendar.getInstance();
		calender.add(Calendar.SECOND, seconds);
		return calender.getTime();
	}

	public static Boolean isGreaterThanCurrentTime(Timestamp timestamp) {
		return timestamp.before(new Date());

	}

}
