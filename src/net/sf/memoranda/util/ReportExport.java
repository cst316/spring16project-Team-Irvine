/*
 * File: 	ReportExport.java	
 * Author:	Eric Mann
 * Date:	February 13, 2016
 * 
 * Description: Contains the class which manages the report export to file
 */

package net.sf.memoranda.util;

import java.io.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.Event;
import net.sf.memoranda.EventImpl;
import net.sf.memoranda.EventsManager;
import net.sf.memoranda.Note;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectManager;
import net.sf.memoranda.Task;
import net.sf.memoranda.TaskImpl;
import net.sf.memoranda.TaskList;
import net.sf.memoranda.TaskListImpl;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.ui.ProjectsPanel;
import net.sf.memoranda.ui.ProjectsTablePanel;
/**
 * Class:	ReportExport
 * 
 * Description: Contains methods which enable the export of all projects, events, tasks, and notes as a report
 */
public class ReportExport {

/**
 * Method: generateReport
 * Inputs:
 * @param file		Output file for report
 * Returns:			N/A
 *
 * Description: Controls the flow of report exporting operation
 */
	@SuppressWarnings("unchecked")
	public static void generateReport(File file){
		Vector<Project> projects;
		Vector<Event> todaysEvents;
		Vector<Task> activeTasks, completedTasks, overdueTasks;
		Vector<Note> notes;
		String output = "";
		
		projects = ProjectManager.getAllProjects();
		for(Project prj : projects ){
			switch(prj.getStatus()){
			case(Project.ACTIVE):
				notes = getNotes(prj);
				todaysEvents = getTodaysEvents();
				overdueTasks = getFailedTasks(prj);
				activeTasks = getActiveTasks(prj);
				completedTasks = getCompletedTasks(prj);
				output += compileReport(prj, notes, todaysEvents, overdueTasks, activeTasks, completedTasks);
				break;
			case(Project.COMPLETED):
				output += System.lineSeparator() + prj.getTitle()
						+ System.lineSeparator() + "\tCOMPLETED"
						+ System.lineSeparator() + prj.getEndDate().toString();
				// report project as completed
				break;
			case(Project.FAILED):
				output += System.lineSeparator() + prj.getTitle()
						+ System.lineSeparator() + "\tFAILED";
				// report project as failed
				break;
			case(Project.FROZEN):
				output += System.lineSeparator() + prj.getTitle()
						+ System.lineSeparator() + "\tFROZEN";
				// report project as frozen
				break;
			case(Project.SCHEDULED):
				output += System.lineSeparator() + prj.getTitle()
						+ System.lineSeparator() + "\tSCHEDULED"
						+ System.lineSeparator() + "Begins on " + prj.getStartDate().toString();
				// report project as scheduled at give start date
				break;
			}
			
		}
	}
	private static String compileReport(Project prj, Vector<Note> notes, Vector<Event> todaysEvents,
			Vector<Task> overdueTasks, Vector<Task> activeTasks, Vector<Task> completedTasks) {
		String out = prj.getTitle()
					+ System.lineSeparator() + prj.getStartDate()
					+ System.lineSeparator() + prj.getStatus()
					+ System.lineSeparator() + "Total Progress: " + getProgress((TaskList) CurrentStorage.get().openTaskList(prj).getTopLevelTasks());
		
		
		return out;
	}
	private static Vector getActiveTasks(Project prj){
		Vector<TaskImpl> ret = new Vector<TaskImpl>();
		Vector<TaskImpl> tasks;
		
		tasks = (Vector<TaskImpl>) CurrentStorage.get().openTaskList(prj).getTopLevelTasks();
		for(int i = 0; i < tasks.size(); i++){
			if(tasks.get(i).getStatus(CalendarDate.today()) == Task.ACTIVE){
				ret.add(tasks.get(i));
			}
		}	
		return ret;
	}
	private static Vector getCompletedTasks(Project prj){
		Vector<TaskImpl> ret = new Vector<TaskImpl>();
		Vector<TaskImpl> tasks;
		
		tasks = (Vector<TaskImpl>) CurrentStorage.get().openTaskList(prj).getTopLevelTasks();
		for(int i = 0; i < tasks.size(); i++){
			if(tasks.get(i).getStatus(CalendarDate.today()) == Task.COMPLETED){
				ret.add(tasks.get(i));
			}
		}	
		return ret;
	}

	private static Vector getFailedTasks(Project prj){
		Vector<TaskImpl> ret = new Vector<TaskImpl>();
		Vector<TaskImpl> tasks;
		
		tasks = (Vector<TaskImpl>) CurrentStorage.get().openTaskList(prj).getTopLevelTasks();
		for(int i = 0; i < tasks.size(); i++){
			if(tasks.get(i).getStatus(CalendarDate.today()) == Task.FAILED){
				ret.add(tasks.get(i));
			}
		}	
		return ret;
	}

	private static Vector<Note> getNotes(Project prj){
		Vector<Note> ret = new Vector<Note>();
		CurrentStorage.get().openNoteList(prj);
		
		return ret;
	}

	private static int getProgress(TaskList tl) {
		Vector v = (Vector) tl.getAllSubTasks(null);
		if (v.size() == 0)
			return -1;
		int p = 0;
		for (Enumeration en = v.elements(); en.hasMoreElements();) {
			Task t = (Task) en.nextElement();
			p += t.getProgress();
		}
		return (p * 100) / (v.size() * 100);
	}
	private static Vector<Event> getTodaysEvents(){
		Vector ret;
		ret = (Vector<Event>) EventsManager.getEventsForDate(CalendarDate.today());
		
		return ret;
	}

		private File fname;
		private String reportContents;
		
}
