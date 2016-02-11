/**
 * File: Ics.java
 * Author: Rudi Wever
 * Date: February 10, 2016
 * Description: builds a VCALENDAR object
 */
package net.sf.memoranda.util;

import java.util.UUID;
import net.sf.memoranda.date.CalendarDate;

/**
 * Class: Ics
 * Description: builds a VCALENDAR object
 */
public class Ics {
	
	/**
	 * Method: Ics
	 * Inputs: none
	 * Returns: BEGIN VCALENDAR, PRODUCT ID, VERSION
	 * Description: Initializes VCALENDAR object with a VCALENDAR header
	 */
	Ics (){
		outputLines = "BEGIN:VCALENDAR\r\n" + "PRODID:-//Memoranda:Rudi Wever//EN\r\n"+
				"VERSION:2.0\r\n";
	}
	
	/**
	 * Method: addEvent
	 * Inputs: String title, CalendarDate startDate
	 * Returns: VCALENDAR EVENT
	 * Description: Adds a VCALENDAR EVENT to a VCALENDAR object
	 */
	void addEvent(String title, CalendarDate startDate){
		String yyyymmdd;
		String uid="";

		outputLines = outputLines.concat("BEGIN:VEVENT\r\n");
		yyyymmdd = convertFormat(startDate);
		outputLines = outputLines.concat("DTSTART;VALUE=DATE:" + yyyymmdd + "\r\n");
		outputLines = outputLines.concat("DTEND;VALUE=DATE:" + yyyymmdd + "\r\n");
		uid = generateUUID();
		outputLines = outputLines.concat("UID:" + uid +"@rwever@cst316.com\r\n");
		outputLines = outputLines.concat("SUMMARY:" + title + "\r\n");
		outputLines = outputLines.concat("END:VEVENT" + "\r\n");
	}//close addEvent
	
	/**
	 * Method: addEvent
	 * Inputs: String title, CalendarDate startDate, Calendar endDate
	 * Returns: VCALENDAR EVENT
	 * Description: Adds a VCALENDAR EVENT to a VCALENDAR object
	 */	
	void addEvent(String title, CalendarDate startDate, CalendarDate endDate){
		String yyyymmdd;
		String uid="";
		outputLines = outputLines.concat("BEGIN:VEVENT\r\n");
		yyyymmdd = convertFormat(startDate);
		outputLines = outputLines.concat("DTSTART;VALUE=DATE:" + yyyymmdd + "\r\n");
		yyyymmdd = convertFormat(endDate);
		outputLines = outputLines.concat("DTEND;VALUE=DATE:" + yyyymmdd + "\r\n");
		uid = generateUUID();
		outputLines = outputLines.concat("UID:" + uid +"@rwever@cst316.com\r\n");
		outputLines = outputLines.concat("SUMMARY:" + title + "\r\n");
		outputLines = outputLines.concat("END:VEVENT" + "\r\n");
	}//close addEvent
	
	/**
	 * Method: generateIcs
	 * Inputs: none
	 * Returns: the current VCALENDAR object
	 * Description: concatenates the VCALENDAR header, event(s), and footer
	 */
	String generateIcs(){
		return (outputLines = outputLines + "END:VCALENDAR\r\n");
	}
	
	private String convertFormat(CalendarDate date){
		String yyyymmdd;
		int month;
		int year;
		int day;
		year = date.getYear();
		yyyymmdd = "" + year;
		month = date.getMonth();
		month = month + 1;
		if (month <= 9){
			yyyymmdd += "0" + month;
		}
		else{
			yyyymmdd += "" + month;
		}
		day = date.getDay();
		if (day <= 9){
			yyyymmdd += "0" + day;
		}
		else{
			yyyymmdd += "" + day;
		}		

		return yyyymmdd;
	}
	
	private String generateUUID(){
		UUID id = UUID.randomUUID();
		return(String.valueOf(id));
	}
	
	private String outputLines;	
}