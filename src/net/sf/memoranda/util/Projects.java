/**
 * File: Projects.java
 * Author: Rudi Wever
 * Date: February 10, 2016
 * Description: Describes a project entity
 */
package net.sf.memoranda.util;
import net.sf.memoranda.date.CalendarDate;

/**
 * Class: Projects
 * Description: Describes a project entity
 */
public class Projects {

	public CalendarDate getEndDate() {
		return endDate;
	}
	
	public String getId() {
		return id;
	}
	
	public CalendarDate getStartDate() {
		return startDate;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setEndDate(CalendarDate endDate) {
		this.endDate = endDate;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setStartDate(CalendarDate startDate) {
		this.startDate = startDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	/**
	 * Method: toString
	 * Inputs: none
	 * Returns: Project title, start date, end date
	 * Description: displays project title, start date, end date
	 */
	public String toString() {
		return "Title:" + title + ", Start Date:" + startDate + ", End Date:" + endDate;
	}
	
	private String id;
	private String title;
	private CalendarDate startDate;
	private CalendarDate endDate;
};

