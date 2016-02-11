/**
 * File: WriteIcsFile.java
 * Author: Rudi Wever
 * Date: February 10, 2016
 * Description: Writes the current content of the VCALENDAR object to a file
 */
package net.sf.memoranda.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Class: WriteIcsFile
 * Description: Writes the current content of the VCALENDAR object to a file
 */
public class WriteIcsFile {
	
	/**
	 * Method: WriteIcsFile
	 * Inputs: filename
	 * Returns: ics file containing the VCALENDAR object
	 * Description: generates output file
	 */
	public WriteIcsFile(java.io.File f) throws Exception {
		ProjectsExport prjExp = new ProjectsExport();

		allProj = prjExp.getProjects();
		Ics calendarData = new Ics ();
		for (int idx = 0; idx < allProj.size(); idx++) {
			Projects individualProj = allProj.get(idx);
			if ((individualProj.getEndDate())!= null){
			calendarData.addEvent(individualProj.getTitle(), individualProj.getStartDate(), individualProj.getEndDate());
			} 
			else{
				calendarData.addEvent(individualProj.getTitle(), individualProj.getStartDate());
			}
		}
			Writer writer = null;
			try {
				String fName = f.toString();
				if (fName.toLowerCase().endsWith(".ics")){
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf-8"));
				}
				else {
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f+".ics"),"utf-8"));
				}
				writer.write(calendarData.generateIcs());

			} catch (IOException e){
				
			} finally{
				try{
					writer.close();
				} catch (Exception ex){
					
				}
			}

			calendarData.generateIcs();
	}
	
	private ArrayList<Projects> allProj;

};
