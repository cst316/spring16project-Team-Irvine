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
import net.sf.memoranda.ui.ExceptionDialog;
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
		String projectReportData = "";
		String notesOutput = "";
		
		
		projects = ProjectManager.getAllProjects(); 
		
		//compile project data for output
		for(Project prj : projects ){
			switch(prj.getStatus()){
			case(Project.ACTIVE):
				notes = getNotes(prj);
				todaysEvents = getTodaysEvents();
				overdueTasks = getFailedTasks(prj);
				activeTasks = getActiveTasks(prj);
				completedTasks = getCompletedTasks(prj);
				projectReportData += compileReport(prj, todaysEvents, overdueTasks, activeTasks, completedTasks);
				break;
			case(Project.COMPLETED):
				projectReportData += System.lineSeparator() + prj.getTitle()
						+ System.lineSeparator() + "\tCOMPLETED"
						+ System.lineSeparator() + prj.getEndDate().toString();
				// report project as completed
				break;
			case(Project.FAILED):
				projectReportData += System.lineSeparator() + prj.getTitle()
						+ System.lineSeparator() + "\tFAILED";
				// report project as failed
				break;
			case(Project.FROZEN):
				projectReportData += System.lineSeparator() + prj.getTitle()
						+ System.lineSeparator() + "\tFROZEN";
				// report project as frozen
				break;
			case(Project.SCHEDULED):
				projectReportData += System.lineSeparator() + prj.getTitle()
						+ System.lineSeparator() + "\tSCHEDULED"
						+ System.lineSeparator() + "Begins on " + prj.getStartDate().toString();
				// report project as scheduled at give start date
				break;
			}
			notes = getNotes(prj);
			for(Note nte : notes){
				notesOutput += nte.getTitle()
							+ System.lineSeparator() + nte.getDate()
							+ System.lineSeparator() + nte.getProject().getTitle()
							+ System.lineSeparator();
			}
			projectReportData += System.lineSeparator()
								+ System.lineSeparator() + "Notes:" 
								+ System.lineSeparator() + notesOutput + System.lineSeparator();
		}
		// compile notes for output
		
		output = "Report For " + CalendarDate.today().getFullDateString() + System.lineSeparator()
				+ System.lineSeparator()
				+ projectReportData + System.lineSeparator() 
				+ System.lineSeparator() + "Notes"
				+ System.lineSeparator() + notesOutput;
		//write to file
		try{
			FileWriter fw = new FileWriter(file);
			
			fw.write(output);
			fw.close();
		}catch(Exception ex){
            new ExceptionDialog(ex, "Failed to write to " + file, "");
            return;
		}
		
	}
	private static String compileReport(Project prj, Vector<Event> todaysEvents,
			Vector<Task> overdueTasks, Vector<Task> activeTasks, Vector<Task> completedTasks) {
		String out = "";
		String project = prj.getTitle()
					+ System.lineSeparator() + prj.getStartDate()
					+ System.lineSeparator() + prj.getStatus()
					+ System.lineSeparator() + "Total Progress: " + getProgress((TaskList) CurrentStorage.get().openTaskList(prj).getTopLevelTasks());
		String events, overdue, active, completed;
		//list today's events
		events = "";
		for(int i = 0; i < todaysEvents.size(); i++){
			events += todaysEvents.get(i).getText() + "\t" + todaysEvents.get(i).getTimeString() + System.lineSeparator();
		}
		overdue = "";
		for(int i = 0; i < overdueTasks.size(); i++){
			overdue += overdueTasks.get(i).getText() 
					+ "PRIORITY: " + overdueTasks.get(i).getPriority()
					+ "Was Due On: " + overdueTasks.get(i).getEndDate().getFullDateString() + System.lineSeparator();
		}
		active = "";
		for(int i = 0; i < activeTasks.size(); i++){
			active += activeTasks.get(i).getText() 
					+ "PRIORITY: " + activeTasks.get(i).getPriority()
					+ "Is Due On: " + activeTasks.get(i).getEndDate().getFullDateString()
					+ "Percent Complete: " + activeTasks.get(i).getProgress() + "%" + System.lineSeparator();
		}
		completed = "";
		for(int i = 0; i < completedTasks.size(); i++){
			completed += completedTasks.get(i).getText() + System.lineSeparator();
		}
		out = project + System.lineSeparator() 
			+ System.lineSeparator() + "Today's Events:" 
			+ System.lineSeparator() + events + System.lineSeparator()
			+ System.lineSeparator() + "Overdue Tasks:"
			+ System.lineSeparator() + overdue + System.lineSeparator()
			+ System.lineSeparator() + "Active Tasks:"
			+ System.lineSeparator() + active + System.lineSeparator()
			+ System.lineSeparator() + "Completed Tasks:"
			+ System.lineSeparator() + completed;
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
