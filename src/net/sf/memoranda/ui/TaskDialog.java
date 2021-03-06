package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.date.DateListener;
import net.sf.memoranda.Task;
import net.sf.memoranda.TaskList;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.date.CurrentDate;
import net.sf.memoranda.date.DateListener;
import net.sf.memoranda.util.Context;
import net.sf.memoranda.util.CurrentStorage;
import net.sf.memoranda.util.Local;
import net.sf.memoranda.util.Util;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
//import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JCheckBox;

import net.sf.memoranda.CurrentProject;
import net.sf.memoranda.date.CalendarDate;
import net.sf.memoranda.util.Local;
import java.util.Vector;
import javax.swing.BoxLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Insets;

/*$Id: TaskDialog.java,v 1.25 2005/12/01 08:12:26 alexeya Exp $*/
public class TaskDialog extends JDialog {
    JPanel mPanel = new JPanel(new BorderLayout());
    JPanel areaPanel = new JPanel(new BorderLayout());
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton cancelB = new JButton();
    JButton okB = new JButton();
    //added jbutton and combo box
    JButton saveTemplate = new JButton();
    JButton load = new JButton();
    JComboBox templateList = new JComboBox((Vector)CurrentProject.getTemplateTaskList().getTopLevelTasks());
    Border border1;
    Border border2;
    JPanel dialogTitlePanel = new JPanel();
    JLabel header = new JLabel();
    public boolean CANCELLED = true;
    JPanel jPanel8 = new JPanel(new GridBagLayout());
    Border border3;
    Border border4;
//    Border border5;
//    Border border6;
    JPanel jPanel2 = new JPanel(new GridLayout(3, 2));
    JTextField todoField = new JTextField();
    
    // added by rawsushi
    JTextField effortField = new JTextField();
    JTextArea descriptionField = new JTextArea();
    JScrollPane descriptionScrollPane = new JScrollPane(descriptionField);
    
//    Border border7;
    Border border8;
    CalendarFrame startCalFrame = new CalendarFrame();
    CalendarFrame endCalFrame = new CalendarFrame();
    String[] priority = {Local.getString("Lowest"), Local.getString("Low"),
        Local.getString("Normal"), Local.getString("High"),
        Local.getString("Highest")};
    boolean ignoreStartChanged = false;
    boolean ignoreEndChanged = false;
    JPanel jPanel4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel jPanel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel jLabel6 = new JLabel();
    JButton setStartDateB = new JButton();
    JPanel jPanel1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JLabel jLabel2 = new JLabel();
    JSpinner startDate;
    JSpinner endDate;
//    JSpinner endDate = new JSpinner(new SpinnerDateModel());
    JButton setEndDateB = new JButton();
    //JPanel jPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel jPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JPanel jPanelEffort = new JPanel(new FlowLayout(FlowLayout.LEFT));
//    JPanel jPanelNotes = new JPanel(new FlowLayout(FlowLayout.LEFT));
    
    JButton setNotifB = new JButton();
    JComboBox priorityCB = new JComboBox(priority);
    JLabel jLabel7 = new JLabel();
    // added by rawsushi
    JLabel jLabelEffort = new JLabel();
    JLabel jLabelDescription = new JLabel();
    JLabel templateLabel = new JLabel();
	JCheckBox chkEndDate = new JCheckBox();
	
	JPanel jPanelProgress = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JLabel jLabelProgress = new JLabel();
	JSpinner progress = new JSpinner(new SpinnerNumberModel(0, 0, 100, 5));
	
	//Forbid to set dates outside the bounds
	CalendarDate startDateMin = CurrentProject.get().getStartDate();
	CalendarDate startDateMax = CurrentProject.get().getEndDate();
	CalendarDate endDateMin = startDateMin;
	CalendarDate endDateMax = startDateMax;
	private final JPanel panel = new JPanel();
    
    public TaskDialog(Frame frame, String title) {
        super(frame, title, true);
        try {
            jbInit();            
            pack();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
    }
    
    void jbInit() throws Exception {
	this.setResizable(false);
	this.setSize(new Dimension(430,300));
        border1 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        border2 = BorderFactory.createEtchedBorder(Color.white, 
            new Color(142, 142, 142));
        border3 = new TitledBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0), 
        Local.getString("To Do"), TitledBorder.LEFT, TitledBorder.BELOW_TOP);
        border4 = BorderFactory.createEmptyBorder(0, 5, 0, 5);
//        border5 = BorderFactory.createEmptyBorder();
//        border6 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
//            Color.white, Color.white, new Color(178, 178, 178),
//            new Color(124, 124, 124));
//        border7 = BorderFactory.createLineBorder(Color.white, 2);
        border8 = BorderFactory.createEtchedBorder(Color.white, 
            new Color(178, 178, 178));
        cancelB.setMaximumSize(new Dimension(100, 26));
        cancelB.setMinimumSize(new Dimension(100, 26));
        cancelB.setPreferredSize(new Dimension(100, 26));
        cancelB.setText(Local.getString("Cancel"));
        cancelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelB_actionPerformed(e);
            }
        });

        startDate = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.DAY_OF_WEEK));
        endDate = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.DAY_OF_WEEK));
		
        chkEndDate.setSelected(false);
		chkEndDate_actionPerformed(null);
		chkEndDate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chkEndDate_actionPerformed(e);
			}
		});

        /**
        * Save Template button
        * added by cavitia316
        */
        saveTemplate.setMaximumSize(new Dimension(180, 26));
        saveTemplate.setMinimumSize(new Dimension(180, 26));
        saveTemplate.setPreferredSize(new Dimension(180, 26));
        saveTemplate.setText(Local.getString("Save as Template"));
        saveTemplate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveTemplate_actionPerformed(e);
            }
        });

        /**
        * Load Template Button
        * added by cavitia316
        */

        okB.setMaximumSize(new Dimension(100, 26));
        okB.setMinimumSize(new Dimension(100, 26));
        okB.setPreferredSize(new Dimension(100, 26));
        okB.setText(Local.getString("Ok"));
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okB_actionPerformed(e);
            }
        });
        
        this.getRootPane().setDefaultButton(okB);
        mPanel.setBorder(border1);
        areaPanel.setBorder(border2);
        dialogTitlePanel.setBackground(Color.WHITE);
        dialogTitlePanel.setBorder(border4);
        
        GridBagLayout gbLayout = (GridBagLayout) jPanel8.getLayout();
        jPanel8.setBorder(border3);
				
        todoField.setBorder(border8);
        todoField.setPreferredSize(new Dimension(375, 24));
        GridBagConstraints gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 1;
        gbLayout.setConstraints(todoField,gbCon);
        
        jLabelDescription.setMaximumSize(new Dimension(100, 16));
        jLabelDescription.setMinimumSize(new Dimension(60, 16));
        jLabelDescription.setText(Local.getString("Description"));
        gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 1;
        gbCon.anchor = GridBagConstraints.WEST;
        gbLayout.setConstraints(jLabelDescription,gbCon);

        descriptionField.setBorder(border8);
        descriptionField.setPreferredSize(new Dimension(375, 387)); // 3 additional pixels from 384 so that the last line is not cut off
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        gbCon = new GridBagConstraints();
        gbCon.gridwidth = GridBagConstraints.REMAINDER;
        gbCon.weighty = 3;
        descriptionScrollPane.setPreferredSize(new Dimension(375,96));
        gbLayout.setConstraints(descriptionScrollPane,gbCon);

        jLabelEffort.setMaximumSize(new Dimension(100, 16));
        jLabelEffort.setMinimumSize(new Dimension(60, 16));
        jLabelEffort.setText(Local.getString("Est Effort(hrs)"));
        effortField.setBorder(border8);
        effortField.setPreferredSize(new Dimension(30, 24));

        startDate.setBorder(border8);
        startDate.setPreferredSize(new Dimension(80, 24));                
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf = (SimpleDateFormat)DateFormat.getDateInstance(DateFormat.SHORT);
		// //Added by (jcscoobyrs) on 14-Nov-2003 at 10:45:16 PM
		startDate.setEditor(new JSpinner.DateEditor(startDate, sdf.toPattern()));

        startDate.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	// it's an ugly hack so that the spinner can increase day by day
            	SpinnerDateModel sdm = new SpinnerDateModel((Date)startDate.getModel().getValue(),null,null,Calendar.DAY_OF_WEEK);
            	startDate.setModel(sdm);

                if (ignoreStartChanged)
                    return;
                ignoreStartChanged = true;
                Date sd = (Date) startDate.getModel().getValue();
                Date ed = (Date) endDate.getModel().getValue();
                if (sd.after(ed) && chkEndDate.isSelected()) {
                    startDate.getModel().setValue(ed);
                    sd = ed;
                }
				if ((startDateMax != null) && sd.after(startDateMax.getDate())) {
					startDate.getModel().setValue(startDateMax.getDate());
                    sd = startDateMax.getDate();
				}
                if ((startDateMin != null) && sd.before(startDateMin.getDate())) {
                    startDate.getModel().setValue(startDateMin.getDate());
                    sd = startDateMin.getDate();
                }
                startCalFrame.cal.set(new CalendarDate(sd));
                ignoreStartChanged = false;
            }
        });

        jLabel6.setText(Local.getString("Start date"));
        //jLabel6.setPreferredSize(new Dimension(60, 16));
        jLabel6.setMinimumSize(new Dimension(60, 16));
        jLabel6.setMaximumSize(new Dimension(100, 16));
        setStartDateB.setMinimumSize(new Dimension(24, 24));
        setStartDateB.setPreferredSize(new Dimension(24, 24));
        setStartDateB.setText("");
        setStartDateB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/calendar.png")));
        setStartDateB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setStartDateB_actionPerformed(e);
            }
        });
        jLabel2.setMaximumSize(new Dimension(270, 16));
        //jLabel2.setPreferredSize(new Dimension(60, 16));
        jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel2.setText(Local.getString("End date"));
        endDate.setBorder(border8);
        endDate.setPreferredSize(new Dimension(80, 24));
        
		endDate.setEditor(new JSpinner.DateEditor(endDate, sdf.toPattern())); //Added by (jcscoobyrs) on
		//14-Nov-2003 at 10:45:16PM
        
        endDate.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	// it's an ugly hack so that the spinner can increase day by day
            	SpinnerDateModel sdm = new SpinnerDateModel((Date)endDate.getModel().getValue(),null,null,Calendar.DAY_OF_WEEK);
            	endDate.setModel(sdm);
            	
                if (ignoreEndChanged)
                    return;
                ignoreEndChanged = true;
                Date sd = (Date) startDate.getModel().getValue();
                Date ed = (Date) endDate.getModel().getValue();				
				if (ed.before(sd)) {
                    endDate.getModel().setValue(ed);
                    ed = sd;
                }
				if ((endDateMax != null) && ed.after(endDateMax.getDate())) {
					endDate.getModel().setValue(endDateMax.getDate());
                    ed = endDateMax.getDate();
				}
                if ((endDateMin != null) && ed.before(endDateMin.getDate())) {
                    endDate.getModel().setValue(endDateMin.getDate());
                    ed = endDateMin.getDate();
                }
				endCalFrame.cal.set(new CalendarDate(ed));
                ignoreEndChanged = false;
            }
        });
        setEndDateB.setMinimumSize(new Dimension(24, 24));
        setEndDateB.setPreferredSize(new Dimension(24, 24));
        setEndDateB.setText("");
        setEndDateB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/calendar.png")));
        setEndDateB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setEndDateB_actionPerformed(e);
            }
        });
        
        setNotifB.setText(Local.getString("Set notification"));
        setNotifB.setIcon(
            new ImageIcon(net.sf.memoranda.ui.AppFrame.class.getResource("resources/icons/notify.png")));
        setNotifB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setNotifB_actionPerformed(e);
            }
        });
        jLabel7.setMaximumSize(new Dimension(100, 16));
        jLabel7.setMinimumSize(new Dimension(60, 16));
        //jLabel7.setPreferredSize(new Dimension(60, 16));
        jLabel7.setText(Local.getString("Priority"));

        priorityCB.setFont(new java.awt.Font("Dialog", 0, 11));
        jPanel4.add(jLabel7, null);
        getContentPane().add(mPanel);
        mPanel.add(areaPanel, BorderLayout.CENTER);
        mPanel.add(buttonsPanel, BorderLayout.SOUTH);
        //Save template button added by cavitia316
        buttonsPanel.add(saveTemplate, null);
        buttonsPanel.add(okB, null);
        buttonsPanel.add(cancelB, null);
        this.getContentPane().add(dialogTitlePanel, BorderLayout.NORTH);
        GridBagLayout gbl_dialogTitlePanel = new GridBagLayout();
        gbl_dialogTitlePanel.columnWidths = new int[]{103, 297, 0};
        gbl_dialogTitlePanel.rowHeights = new int[]{48, 0};
        gbl_dialogTitlePanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gbl_dialogTitlePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        dialogTitlePanel.setLayout(gbl_dialogTitlePanel);
        //dialogTitlePanel.setMinimumSize(new Dimension(159, 52));
        //dialogTitlePanel.setPreferredSize(new Dimension(159, 52));
        header.setFont(new java.awt.Font("Dialog", 0, 20));
        header.setForeground(new Color(0, 0, 124));
        header.setText(Local.getString("To do"));
        header.setIcon(new ImageIcon(net.sf.memoranda.ui.TaskDialog.class.getResource(
            "resources/icons/task48.png")));
        GridBagConstraints gbc_header = new GridBagConstraints();
        gbc_header.anchor = GridBagConstraints.NORTHWEST;
        gbc_header.insets = new Insets(0, 0, 0, 5);
        gbc_header.gridx = 0;
        gbc_header.gridy = 0;
        dialogTitlePanel.add(header, gbc_header);
        areaPanel.add(jPanel8, BorderLayout.NORTH);
        /** end debug task #57 **/

        jPanel8.add(todoField, null);
        jPanel8.add(jLabelDescription);
        jPanel8.add(descriptionScrollPane, null);
        areaPanel.add(jPanel2, BorderLayout.CENTER);
        jPanel2.add(jPanel6, null);
        jPanel6.add(jLabel6, null);
        jPanel6.add(startDate, null);
        jPanel6.add(setStartDateB, null);
        jPanel2.add(jPanel1, null);
		jPanel1.add(chkEndDate, null);
        jPanel1.add(jLabel2, null);
        jPanel1.add(endDate, null);
        jPanel1.add(setEndDateB, null);
        // added by rawsushi
        jPanel2.add(jPanelEffort, null);
        jPanelEffort.add(jLabelEffort, null);
        jPanelEffort.add(effortField, null);

        jPanel2.add(jPanel4, null);
        jPanel4.add(priorityCB, null);
        jPanel2.add(jPanel3, null);
        
        jPanel3.add(setNotifB, null);
        
        jLabelProgress.setText(Local.getString("Progress"));
        jPanelProgress.add(jLabelProgress, null);
        jPanelProgress.add(progress, null);
        jPanel2.add(jPanelProgress);
        
        priorityCB.setSelectedItem(Local.getString("Normal"));
        mPanel.add(panel, BorderLayout.NORTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.add(templateLabel);
        templateLabel.setMaximumSize(new Dimension(100, 16));
        templateLabel.setMinimumSize(new Dimension(60, 16));
        templateLabel.setText(Local.getString("Choose a Template: "));
        panel.add(templateList);
        panel.add(load);
        load.setMaximumSize(new Dimension(100, 26));
        load.setMinimumSize(new Dimension(100, 26));
        load.setPreferredSize(new Dimension(100, 26));
        load.setText(Local.getString("Load"));
        load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                load_actionPerformed(e);
            }
        });
        startCalFrame.cal.addSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignoreStartChanged)
                    return;
                startDate.getModel().setValue(startCalFrame.cal.get().getCalendar().getTime());
            }
        });
        
        endCalFrame.cal.addSelectionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ignoreEndChanged)
                    return;
                endDate.getModel().setValue(endCalFrame.cal.get().getCalendar().getTime());
            }
        });
    }

	public void setStartDate(CalendarDate d) {
		this.startDate.getModel().setValue(d.getDate());
	}
	
	public void setEndDate(CalendarDate d) {		
		if (d != null) 
			this.endDate.getModel().setValue(d.getDate());
	}
	
	public void setStartDateLimit(CalendarDate min, CalendarDate max) {
		this.startDateMin = min;
		this.startDateMax = max;
	}
	
	public void setEndDateLimit(CalendarDate min, CalendarDate max) {
		this.endDateMin = min;
		this.endDateMax = max;
	}
	
    void okB_actionPerformed(ActionEvent e) {
        CANCELLED = false;
        this.dispose();
    }

    /**
    * Method: saveTemplate_actionPerformed
    * Inputs: ActionEvent e button being pressed
    * returns: void
    *
    * Saves the template settings to the template task list
    * cavitia316
    */
    void saveTemplate_actionPerformed(ActionEvent e){
        CANCELLED = true;
        Point loc = App.getFrame().getLocation();
        Dimension frmSize = App.getFrame().getSize();
        this.startDate.getModel().setValue(CurrentDate.get().getDate());
        this.endDate.getModel().setValue(CurrentDate.get().getDate());
        this.setLocation((frmSize.width - this.getSize().width) / 2 + loc.x, (frmSize.height - this.getSize().height) / 2 + loc.y);
        this.setVisible(true);

        CalendarDate sd = new CalendarDate((Date) this.startDate.getModel().getValue());
        CalendarDate ed;
        if(this.chkEndDate.isSelected()){
            ed = new CalendarDate((Date) this.endDate.getModel().getValue());
        }else{
            ed = null;
        }
        long effort = Util.getMillisFromHours(this.effortField.getText());
        Task newTask = CurrentProject.getTemplateTaskList().createTask(sd, ed, this.todoField.getText(), this.priorityCB.getSelectedIndex(),effort, this.descriptionField.getText(),null);
        newTask.setProgress(((Integer)this.progress.getValue()).intValue());
        CurrentStorage.get().storeTemplateTaskList(CurrentProject.getTemplateTaskList(), CurrentProject.get());
    }

    /**
    * Method: load_actionPerformed
    * Inputs: ActionEvent e button being pressed
    * returns: void
    *
    * Populates the taskDialog panel
    * cavitia316
    */
    void load_actionPerformed(ActionEvent e){
        Task selectedTemplate = (Task)templateList.getSelectedItem();
        this.todoField.setText(selectedTemplate.getText());
        this.descriptionField.setText(selectedTemplate.getDescription());
        this.startDate.getModel().setValue(selectedTemplate.getStartDate().getDate());
        this.endDate.getModel().setValue(selectedTemplate.getEndDate().getDate());
        this.priorityCB.setSelectedIndex(selectedTemplate.getPriority());                
        this.effortField.setText(Util.getHoursFromMillis(selectedTemplate.getEffort()));
    }

    void cancelB_actionPerformed(ActionEvent e) {
        this.dispose();
    }
	
	void chkEndDate_actionPerformed(ActionEvent e) {
		endDate.setEnabled(chkEndDate.isSelected());
		setEndDateB.setEnabled(chkEndDate.isSelected());
		jLabel2.setEnabled(chkEndDate.isSelected());
		if(chkEndDate.isSelected()) {
			Date currentEndDate = (Date) endDate.getModel().getValue();
			Date currentStartDate = (Date) startDate.getModel().getValue();
			if(currentEndDate.getTime() < currentStartDate.getTime()) {
				endDate.getModel().setValue(currentStartDate);
			}
		}
	}

    void setStartDateB_actionPerformed(ActionEvent e) {
        startCalFrame.setLocation(setStartDateB.getLocation());
        startCalFrame.setSize(200, 200);
        this.getLayeredPane().add(startCalFrame);
        startCalFrame.show();

    }

    void setEndDateB_actionPerformed(ActionEvent e) {
        endCalFrame.setLocation(setEndDateB.getLocation());
        endCalFrame.setSize(200, 200);
        this.getLayeredPane().add(endCalFrame);
        endCalFrame.show();
    }
    
    void setNotifB_actionPerformed(ActionEvent e) {
    	((AppFrame)App.getFrame()).workPanel.dailyItemsPanel.eventsPanel.newEventB_actionPerformed(e, 
			this.todoField.getText(), (Date)startDate.getModel().getValue(),(Date)endDate.getModel().getValue());
    }

}