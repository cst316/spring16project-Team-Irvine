/**
 * File: ProjectExporter.java
 * Author: Memoranda Project & ASU Spring 2016 CST316 Team Irvine
 * Date: 2/6/2016
 * 
 * Description: Handles the export of projects and notes
 */
/*
 * ProjectExporter.java Package: net.sf.memoranda.util Created on 19.01.2004
 * 16:44:05 @author Alex
 */
package net.sf.memoranda.util;

import net.sf.memoranda.*;
import net.sf.memoranda.ui.*;
import net.sf.memoranda.ui.htmleditor.AltHTMLWriter;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.Collections;

import javax.swing.text.html.HTMLDocument;

/**
 * Class: ProjectExporter
 * 
 * Description: This is class allows the export of projects and notes from Memoranda
 */
/* $Id: ProjectExporter.java,v 1.7 2005/07/05 08:17:28 alexeya Exp $ */
public class ProjectExporter {

    static boolean _chunked = false;
    static boolean _num = false;
    static boolean _xhtml = false;
    static boolean _copyImages = false;
    static File output = null;
    static String _charset = null;
    static boolean _titlesAsHeaders = false;
    static boolean _navigation = false;
    
    static String charsetString = "\n";

/**
 * Method: export
 * Inputs: 
 * @param prj 				The project being exported
 * @param f					The file being exported to
 * @param charset 			The charset being used (usually UTF-8)
 * @param xhtml				Using xhtml formatting?
 * @param chunked			Is the output already chunked?
 * @param nagigation		Generate Navigation?
 * @param num				??
 * @param titlesAsHeaders	Project titles are the headers
 * @param copyImages		Are images included in the export?
 * Returns: N/A
 * 
 * Description: This method controls the export and writing to a file of projects and batches of notes
 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void export(Project prj, File f, String charset, boolean xhtml, boolean chunked, 
			boolean navigation, boolean num, boolean titlesAsHeaders, boolean copyImages) {

        _num = num;
        _chunked = chunked;
        _charset = charset;
        _xhtml = xhtml;
        _titlesAsHeaders = titlesAsHeaders;
        _copyImages = copyImages;
        _navigation = navigation;
        if (f.isDirectory()){
            output = new File(f.getPath() + "/index.html");
        }else{
            output = f;
        }
        NoteList nl = CurrentStorage.get().openNoteList(prj);
        Vector notes = (Vector) nl.getAllNotes();
        //NotesVectorSorter.sort(notes);
        Collections.sort(notes);

        Writer fw;

        if (output.getName().indexOf(".htm") == -1) {
            String dir = output.getPath();
            String ext = ".html";

            String nfile = dir + ext;

            output = new File(nfile);
        }        
        try {
            if (charset != null) {
                fw = new OutputStreamWriter(new FileOutputStream(output),
                        charset);
                charsetString = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset="
                        + charset + "\" />";
            }else{
                fw = new FileWriter(output);
            }
        }catch (Exception ex) {
            new ExceptionDialog(ex, "Failed to write to " + output, "");
            return;
        }
        write(fw, "<html>\n<head>\n" + charsetString + "<title>" + prj.getTitle() 
        		+ "</title>\n</head>\n<body>\n<h1 class=\"projecttitle\">" + prj.getTitle() + "</h1><br></br>\n");
        generateToc(fw, notes);
        generateChunks(fw, notes);
        write(fw, "\n<hr></hr><a href=\"http://memoranda.sf.net\">Memoranda</a> " + App.VERSION_INFO 
        		+ "\n<br></br>\n" + new Date().toString() + "\n</body>\n</html>");
        try {
            fw.flush();
            fw.close();
        }catch (Exception ex) {
            new ExceptionDialog(ex, "Failed to write to " + output, "");
        }
    }
    
/**
 * Method: exportNote
 * Inputs:
 *  * Inputs: 
 * @param prj 				The project being exported
 * @param f					The file being exported to
 * @param charset 			The charset being used (usually UTF-8)
 * @param xhtml				Using xhtml formatting?
 * @param chunked			Is the output already chunked?
 * @param nagigation		Generate Navigation?
 * @param num				??
 * @param titlesAsHeaders	Project titles are the headers
 * @param copyImages		Are images included in the export?
 * Returns: N/A
 * 
 * Description: Exports the currently selected note to a file
 */
public static void exportNote(Project prj, File f, String charset, boolean xhtml, boolean chunked, 
		boolean num, boolean titlesAsHeaders, boolean copyImages){
	
    _num = num;
    _chunked = chunked;
    _charset = charset;
    _xhtml = xhtml;
    _titlesAsHeaders = titlesAsHeaders;
    _copyImages = copyImages;
	
	output = f;
	
	NoteList nl = CurrentStorage.get().openNoteList(prj);
    Note note = nl.getActiveNote();
    
	if (output.getName().indexOf(".htm") == -1) {
        String dir = output.getPath();
        String ext = ".html";

        String nfile = dir + ext;

        output = new File(nfile);
    } 
	
	Writer fw;
	
	try {
        if (charset != null) {
            fw = new OutputStreamWriter(new FileOutputStream(output),
                    charset);
            charsetString = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset="
                    + charset + "\" />";
        }else{
            fw = new FileWriter(output);
        }
    }catch (Exception ex) {
        new ExceptionDialog(ex, "Failed to write to " + output, "");
        return;
    }
	write(fw, "<html>\n<head>\n" + charsetString + "<title>" + prj.getTitle()  
			+ "</title>\n</head>\n<body>\n<h1 class=\"projecttitle\">" + prj.getTitle() + "</h1><br></br>\n");
	generateChunks(fw, note);
	write(fw, "\n<hr></hr><a href=\"http://memoranda.sf.net\">Memoranda</a> " + App.VERSION_INFO 
			+ "\n<br></br>\n" + new Date().toString() + "\n</body>\n</html>");
	try {
		fw.flush();
		fw.close();
	}catch (Exception ex) {
		new ExceptionDialog(ex, "Failed to write to " + output, "");
	}
}
/**
 * Method: generateToc
 * Inputs: 
 * @param w		writer containing output data
 * @param notes	vector containing notes data
 * Returns:	N/A
 * 
 * Description: Creates a table of contents from notes data and puts it on the writer
 */
    @SuppressWarnings("rawtypes")
	private static void generateToc(Writer w, Vector notes) {
        write(w, "<div class=\"toc\"><ul>\n");
        for (Iterator i = notes.iterator(); i.hasNext(); ) {
            Note note = (Note) i.next();
            String link = "";
//            CalendarDate d = note.getDate(); // not used
            String id = note.getId();
            if (!_chunked){
                link = "#" + id;
            }else{
                link = id + ".html";
            }
            write(w, "<li><a href=\"" + link + "\">"
                    + note.getDate().getMediumDateString() + " "
                    + note.getTitle() + "</a></li>\n");
        }
        write(w, "</ul></div>\n");
    }

/**
 * Method: getNoteHTML
 * Inputs:
 * @param note	Note to be converted to HTML
 * Returns:
 * @return Note contents as a string with HTML tags
 * 
 * Description: converts notes to HTML format
 */
    private static String getNoteHTML(Note note) {
        String text = "";
        StringWriter sw = new StringWriter();
        AltHTMLWriter writer = new AltHTMLWriter(sw, (HTMLDocument) CurrentStorage.get().openNote(note), _charset, _num);
        try {
            writer.write();
            sw.flush();
            sw.close();
        }catch (Exception ex) {
            new ExceptionDialog(ex);
        }
        text = sw.toString();
        if (_xhtml){
            text = HTMLFileExport.convertToXHTML(text);
        }
        text = Pattern.compile("<body(.*?)>", java.util.regex.Pattern.DOTALL 
        			+ java.util.regex.Pattern.CASE_INSENSITIVE).split(text)[1];
        text = Pattern.compile("</body>", java.util.regex.Pattern.DOTALL 
        			+ java.util.regex.Pattern.CASE_INSENSITIVE).split(text)[0];
        /*
                 * if (_copyImages) { ?)\"" + java.util.regex.Pattern.DOTALL +
                 * java.util.regex.Pattern.CASE_INSENSITIVE); Matcher m =
                 * p.matcher(text); for (int i = 1; i < m.groupCount(); i++) { String g =
                 * m.group(i); String url = g.split("\"")[1];
                 *  }
                 */
        text = "<div class=\"note\">" + text + "</div>";

        if (_titlesAsHeaders){
            text = "\n\n<div class=\"date\">" + note.getDate().getFullDateString() + ":</div>\n<h1 class=\"title\">" 
            		+ note.getTitle()  + "</h1>\n" + text;
        }
        return text;
    }

/**
 * Method: generateNAV
 * Inputs:
 * @param prev	the previous note
 * @param next 	the next note
 * Returns:
 * @return	HTML links to both the prior and next notes
 * 
 * Description: Creates hyperlinks to the next and prior notes
 */
    private static String generateNav(Note prev, Note next) {
        String s = "<hr></hr><div class=\"navigation\"><table border=\"0\" width=\"100%\" cellpadding=\"2\"><tr><td width=\"33%\">";
        if (prev != null){
            s += "<div class=\"navitem\"><a href=\"" + prev.getId() + ".html\">" + Local.getString("Previous") + "</a><br></br>" 
            		+ prev.getDate().getMediumDateString() + " " + prev.getTitle() + "</div>";
        }else{
            s += "&nbsp;";
                s += "</td><td width=\"34%\" align=\"center\"><a href=\""
                + output.getName()
                + "\">Up</a></td><td width=\"33%\" align=\"right\">";
        }
        if (next != null){ 
            s += "<div class=\"navitem\"><a href=\"" + next.getId() + ".html\">" + Local.getString("Next") + "</a><br></br>" 
            		+ next.getDate().getMediumDateString() + " " + next.getTitle() + "</div>";
        }else{
            s += "&nbsp;";
        }
        s += "</td></tr></table></div>\n";
        return s;
    }

/**
 * Method: generateChunks
 * Inputs:
 * @param w		Writer for output
 * @param notes	Notes to be written out
 * Returns:	N/A
 * 
 * Description: Generates chunks of notes
 */
    @SuppressWarnings("rawtypes")
	private static void generateChunks(Writer w, Vector notes) {
        Object[] n = notes.toArray();
        for (int i = 0; i < n.length; i++) {
            Note note = (Note) n[i];
//            CalendarDate d = note.getDate(); // not used
            if (_chunked) {
                File f = new File(output.getParentFile().getPath() + "/" + note.getId() + ".html");
                Writer fw = null;
                try {
                    if (_charset != null){
                        fw = new OutputStreamWriter(new FileOutputStream(f),_charset);
                    }else{
                        fw = new FileWriter(f);
                    }
                    String s = "<html>\n<head>\n"+charsetString+"<title>" + note.getTitle()
                            + "</title>\n</head>\n<body>\n" + getNoteHTML(note);
                    if (_navigation) {
                        Note nprev = null;
                        if (i > 0){
                            nprev = (Note) n[i - 1];
                        }
                        Note nnext = null;
                        if (i < n.length - 1){
                            nnext = (Note) n[i + 1];
                        }
                        s += generateNav(nprev, nnext);
                    }
                    s += "\n</body>\n</html>";
                    fw.write(s);
                    fw.flush();
                    fw.close();
                }
                catch (Exception ex) {
                    new ExceptionDialog(ex, "Failed to write to " + output, "");
                }
            }else{
            	write(w, "<a name=\"" + note.getId() + "\">" + getNoteHTML(note) + "</a>\n");
            }
        }
    }
/**
 * Method: generateChunks
 * Inputs:
 * @param w		Writer for output
 * @param note	Note to be written out
 * Returns:	N/A
 * 
 * Description: Generates chunks of notes
 */
	private static void generateChunks(Writer w, Note noteOut) {
        Note note = noteOut;
//                CalendarDate d = note.getDate(); // not used
        if (_chunked) {
            File f = new File(output.getParentFile().getPath() + "/" + note.getId() + ".html");
            Writer fw = null;
            try {
                if (_charset != null){
                    fw = new OutputStreamWriter(new FileOutputStream(f),_charset);
                }else{
                    fw = new FileWriter(f);
                }
                String s = "<html>\n<head>\n"+charsetString+"<title>" + note.getTitle()
                        + "</title>\n</head>\n<body>\n" + getNoteHTML(note);
                s += "\n</body>\n</html>";
                fw.write(s);
                fw.flush();
                fw.close();
            }
            catch (Exception ex) {
                new ExceptionDialog(ex, "Failed to write to " + output, "");
            }
        }else{
        	write(w, "<a name=\"" + note.getId() + "\">" + getNoteHTML(note) + "</a>\n");
        }
    }
/**
 * Method: write
 * Inputs:
 * @param w	Writer for output
 * @param s	String to write out
 * Returns: N/A
 * 
 * Description: Writes the file
 */
    private static void write(Writer w, String s) {
        try {
            w.write(s);
        }catch(Exception ex) {
            new ExceptionDialog(ex, "Failed to write to " + output, "");
        }
    }
}