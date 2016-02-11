/**
 * File: ProjectsExport.java
 * Author: Rudi Wever
 * Date: February 10, 2016
 * Description: Handles a list of the current projects
 */
package net.sf.memoranda.util;

import java.util.ArrayList;
import java.util.Vector;
import net.sf.memoranda.Project;
import net.sf.memoranda.ProjectManager;

/**
 * Class: ProjectsExport
 * Description: Handles a list of the current projects
 */
public class ProjectsExport {

	/**
	 * Method: getProjects
	 * Inputs: none
	 * Returns: current list of projects
	 * Description: the current list of projects is obtained
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Projects> getProjects() throws Exception {
		Vector<Project> allProjs = ProjectManager.getAllProjects();
		ArrayList<Projects> projList = new ArrayList<>();

		for (int idx=0; idx <allProjs.size(); idx++){
			Project singleProj = allProjs.get(idx);
			Projects proj = new Projects();
			proj.setId(singleProj.getID());
			proj.setTitle(singleProj.getTitle());
			proj.setStartDate(singleProj.getStartDate());
			proj.setEndDate(singleProj.getEndDate());
			projList.add(proj);
		}
		return projList;
	}
};

