import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * This screen will first display a list of badges in the
 * database which can be selected. The badges can be selected
 * and then each scout's progress towards the badge is displayed 
 * so that it can be edited and changes saved back into the database.
 * @author Matthew Brookes
 */
public class Badges {
	/**
	 * The class' only constructor has a JFrame as an argument which
	 * is passed from the previous screen. On this frame the badge names
	 * are displayed then the table for progress is displayed.
	 * The back button will be directed towards the Report screen.
	 * @param frame The screen where buttons will be drawn
	 */
	public Badges(JFrame frame){
		System.out.println("Badges");
		Container pane = frame.getContentPane();
		pane.removeAll();
		pane.repaint();
		drawScreen(frame);
		frame.setVisible(true);
	}
	
	/**
	 * This function draws the first screen the user sees when this
	 * section is started. It consists of the names of all badges
	 * in the database in two columns which are ordered by name.
	 * Each name acts as a hyperlink to the main section where details
	 * can be updated.
	 * @param frame The screen of the program
	 */
	private static void drawScreen(final JFrame frame){
		Container pane = frame.getContentPane();
		pane.setBounds(0,0,800,100);
		pane.setBackground(Color.WHITE); //Set the background to white
		JPanel header = new JPanel(); //Header panel
		header.setBounds(0, 0, 800, 100);
		//Set box layout on header
		BoxLayout headerLayout = new BoxLayout(header, BoxLayout.X_AXIS);
		header.setLayout(headerLayout);
		Box box = Box.createHorizontalBox();
		
		//Set purple color to background
		header.setBackground(new Color(139,0,102)); 
		//Create home and back buttons
		ImageIcon back = new ImageIcon("./res/back.png");
		JLabel backButton = new JLabel(back);
		backButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Return to report screen
				new Reports(frame);
			}
			
			//Required methods but useless
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		box.add(backButton);
		
		ImageIcon home = new ImageIcon("./res/home.png");
		JLabel homeButton = new JLabel(home);
		homeButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Return to main screen
				new ScoutManager(frame);
			}
			
			//Required methods but useless
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		box.add(homeButton);
		
		//Add title
		JLabel title = new JLabel("BADGES");
		title.setFont(new Font("Verdana",Font.PLAIN, 40));
		title.setForeground(new Color(237,119,3));
		box.add(Box.createHorizontalGlue());
		box.add(title);
		
		//Add scout logo
		ImageIcon scoutLogo = new ImageIcon("./res/scout_logo.png");
		JLabel logo = new JLabel(scoutLogo);
		logo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//Add to header
		box.add(Box.createHorizontalGlue());
		box.add(logo);
		header.add(box);
		
		pane.add(header, BorderLayout.NORTH); //Add header to main screen
		
		//Create main body
		JPanel body = new JPanel();
		body.setBackground(Color.WHITE);
		body.setLayout(new ListLayout(2, 0));
		body.setBorder(new EmptyBorder(20, 0, 0, 0));
		//Make list scrollable if exceeds screen size
		JScrollPane scrollBody = new JScrollPane(body);
        scrollBody.setVerticalScrollBarPolicy(
        		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		Connection db = ScoutManager.connectToDB();
		//Holds all badge names
		ArrayList<String> namesInDb = new ArrayList<String>();
		try {
			//Retrieve all badges from db
			Statement stmt = db.createStatement();
			String sql = "SELECT NAME FROM BADGES";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				//Foreach badge add to array
				namesInDb.add((String) rs.getObject(1));
			}
			//Close connections
			stmt.close();
			db.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		//Sort names alphabetically
		Collections.sort(namesInDb, new Comparator<String>() {
	        @Override
	        public int compare(String s1, String s2) {
	            return s1.compareToIgnoreCase(s2);
	        }
	    });
		
		for(final String name: namesInDb){
			//Foreach badge in database draw a label
			JLabel nameLabel = new JLabel(name);
			nameLabel.setFont(new Font("Calibri", Font.PLAIN, 18));
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			//If label clicked then show data for that badge
			nameLabel.addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent e) {
					// Draw the update screen over current screen
					drawProgressTable(frame, name);
				}
				//Useless methods but required
				@Override
				public void mouseEntered(MouseEvent e){					
				}
				@Override
				public void mouseExited(MouseEvent e){					
				}
				@Override
				public void mousePressed(MouseEvent e){					
				}
				@Override
				public void mouseReleased(
						MouseEvent e){}
				
			});
			body.add(nameLabel);
		}
		
		pane.add(scrollBody, BorderLayout.CENTER);	
	}
	
	/**
	 * This method changes the layout of the screen to show a table
	 * with the dates that scouts achieved requirements
	 * for the badges and the user has an opportunity to make 
	 * changes which will be stored in the database.
	 * @param frame The screen where the layout will be drawn
	 * @param name The name of the badge to be updated
	 */
	private static void drawProgressTable(
			final JFrame frame, final String badgeName){
		Container pane = frame.getContentPane();
		pane.removeAll();
		pane.repaint();
		pane = frame.getContentPane();
		pane.setBounds(0,0,800,100);
		pane.setBackground(Color.WHITE); //Set the background to white
		JPanel header = new JPanel(); //Header panel
		header.setBounds(0, 0, 800, 100);
		//Set box layout on header
		BoxLayout headerLayout = new BoxLayout(header, BoxLayout.X_AXIS);
		header.setLayout(headerLayout);
		Box box = Box.createHorizontalBox();
		
		//Set purple color to background
		header.setBackground(new Color(139,0,102)); 
		//Create home and back buttons
		ImageIcon back = new ImageIcon("./res/back.png");
		JLabel backButton = new JLabel(back);
		backButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Return to report screen
				new Reports(frame);
			}
			
			//Required methods but useless
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		box.add(backButton);
		
		ImageIcon home = new ImageIcon("./res/home.png");
		JLabel homeButton = new JLabel(home);
		homeButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Return to main screen
				new ScoutManager(frame);
			}
			
			//Required methods but useless
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		box.add(homeButton);
		
		//Add title
		JLabel title = new JLabel(badgeName.toUpperCase());
		title.setFont(new Font("Verdana",Font.PLAIN, 30));
		title.setForeground(new Color(237,119,3));
		title.setPreferredSize(new Dimension(400, 100));
		title.setHorizontalAlignment(JLabel.CENTER);
		box.add(Box.createHorizontalGlue());
		box.add(title);
		
		//Add scout logo
		ImageIcon scoutLogo = new ImageIcon("./res/scout_logo.png");
		JLabel logo = new JLabel(scoutLogo);
		logo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//Add to header
		box.add(Box.createHorizontalGlue());
		box.add(logo);
		header.add(box);
		
		pane.add(header, BorderLayout.NORTH); //Add header to main screen
		
		Connection db = ScoutManager.connectToDB();
		// Will hold the data about the badge
		final Badge badge = new Badge(); 
		
		try {
			// Retrieve existing information from database
			Statement stmt = db.createStatement();
			String sql = "SELECT * FROM BADGES WHERE NAME=\"" + 
					badgeName + "\"";
			ResultSet rs = stmt.executeQuery(sql); 
			while(rs.next()){
				// Store information in badge object
				badge.setName(badgeName);
				badge.setRequirementsNeeded(rs.getByte(2));
				String[] requirements = new String[10];
				requirements[0] = rs.getString(3);
				requirements[1] = rs.getString(4);
				requirements[2] = rs.getString(5);
				requirements[3] = rs.getString(6);
				requirements[4] = rs.getString(7);
				requirements[5] = rs.getString(8);
				requirements[6] = rs.getString(9);
				requirements[7] = rs.getString(10);
				requirements[8] = rs.getString(11);
				requirements[9] = rs.getString(12);
				badge.setRequirements(requirements);
			}
			// Tidy up connections
			stmt.close();
			db.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		db = ScoutManager.connectToDB();
		//Holds all scout names
		final ArrayList<String> namesInDb = new ArrayList<String>(); 
		try {
			//Retrieve all scouts from db
			Statement stmt = db.createStatement();
			String sql = "SELECT NAME FROM SCOUTS";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				//Foreach scout add to array
				namesInDb.add((String) rs.getObject(1));
			}
			//Close connections
			stmt.close();
			db.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		//Sort names alphabetically
		Collections.sort(namesInDb, new Comparator<String>() {
	        @Override
	        public int compare(String s1, String s2) {
	            return s1.compareToIgnoreCase(s2);
	        }
	    });
		
		db = ScoutManager.connectToDB();
		//Array will hold the unfiltered data from db
		final Object[][] originalData = new Object[namesInDb.size()]
				[badge.getRequirements().length + 1];
		for(String scoutName : namesInDb){
			try {
				//Retrieve progress data from db
				Statement stmt = db.createStatement();
				Object[] progress = new Object[
				                    badge.getRequirements().length + 1];
				progress[0] = scoutName;
				//Initialise rest of array with blank strings
				for(int i=1; i<= badge.getRequirements().length; i++){
					progress[i] = "";
				}
				
				String sql = "SELECT * FROM '" + scoutName + "' " +
						"WHERE \"BADGE NAME\"='" + badgeName + "'";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					//Foreach date add to array
					for(int i=1; i<= badge.getRequirements().length; i++){
						progress[i] = rs.getString(i+1); 
					}
				}
				originalData[namesInDb.indexOf(scoutName)] = progress;
				//Close connections
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		//Close database connection
		try {
			db.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		//Draw body panel
		JPanel body = new JPanel();
		body.setBackground(Color.WHITE);
		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
		body.setBorder(new EmptyBorder(20,20,0,0));
				
		//Create radio buttons for filter options
		JRadioButton showAllButton = new JRadioButton(
				"Show all scouts");
		showAllButton.setSelected(true);
		showAllButton.setBackground(Color.white);
		JRadioButton showCompletedButton = new JRadioButton(
				"Show scouts who have completed badge");
		showCompletedButton.setBackground(Color.white);
		JRadioButton showUncompletedButton = new JRadioButton(
				"Show scouts who have not completed badge");
		showUncompletedButton.setBackground(Color.white);
		frame.setVisible(true);
		
		//Create row filters for each option
		final RowFilter<Object, Object> showAllFilter = 
				new RowFilter<Object, Object>(){
			@Override
			public boolean include(
					Entry<? extends Object, ? extends Object> entry) {
				//Show every item
				return true;
			}
		};
		final RowFilter<Object, Object> showCompletedFilter = 
				new RowFilter<Object, Object>(){
			@Override
			public boolean include(
					Entry<? extends Object, ? extends Object> entry) {
				//Iterate through each scout name
				for (int i = entry.getValueCount() - 1; i >= 0; i--) {
					String scoutName = entry.getStringValue(i);
					for(int j=0; j<namesInDb.size(); j++){
						//Find scout in the array
						if(((String)originalData[j][0])
								.equals(scoutName)){
							//If found the scout in array
							//Number of requirements completed
							int counter = 0; 
							for(int k=1; k<originalData[j].length - 1; 
																	k++){
								//Iterate over each date
								if(!((String)originalData[j][k])
														.isEmpty()){
									//If requirement has been met
									counter += 1; //Increment counter
								}
							}
							if(counter >= badge.getRequirementsNeeded()){
								//Show scout if achieved enough 
								return true;
							}
						}
					}
				}
				//If not achieved enough requirements, don't show name
				return false;
			}	
		};
		final RowFilter<Object, Object> showUncompletedFilter = 
				new RowFilter<Object, Object>(){
			@Override
			public boolean include(
					Entry<? extends Object, ? extends Object> entry) {
				//Iterate through each scout name
				for (int i = entry.getValueCount() - 1; i >= 0; i--) {
					String scoutName = entry.getStringValue(i);
					for(int j=0; j<namesInDb.size(); j++){
						//Find scout in the array
						if(((String)originalData[j][0])
								.equals(scoutName)){
							//If found the scout in array
							//Number of requirements completed
							int counter = 0; 
							for(int k=1; k<originalData[j].length - 1; 
																	k++){
								//Iterate over each date
								if(!((String)originalData[j][k])
															.isEmpty()){
									//If requirement has been met
									counter += 1; //Increment counter
								}
							}
							if(counter < badge.getRequirementsNeeded()){
								//Show scout if not achieved enough 
								return true;
							}
						}
					}
				}
				//If achieved enough requirements, don't show name
				return false;
			}
		};
		
		//Group buttons so only one can be selected at a time
		ButtonGroup group = new ButtonGroup();
		group.add(showAllButton);
		group.add(showCompletedButton);
		group.add(showUncompletedButton);
		
		//Add buttons to the body
		body.add(showAllButton);
		body.add(showCompletedButton);
		body.add(showUncompletedButton);
		
		//Create array of column names
		Object[] columnNames = new Object[
	                                  badge.getRequirements().length + 1];
		columnNames[0] = "Name";
		for(int i=1; i<=badge.getRequirements().length; i++){
			//Add each requirement as a column name
			columnNames[i] = badge.getRequirements()[i-1];
		}
		
		//Create table to display information
		TableModel model = new DefaultTableModel(originalData, 
													columnNames);
		final JTable table = new JTable(model){
			//Make the first column uneditable
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column){
				return(column != 0);
			}
			
		};
		//Make table scrollable if exceeds screen size
		final JScrollPane scrollTable = new JScrollPane(table);
		scrollTable.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollTable.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		body.add(scrollTable);
		
		class DateCellEditor extends AbstractCellEditor 
								implements TableCellEditor {
			private static final long serialVersionUID = 1L;
			//This is the component that will handle the editing of 
			//the cell value
		    JTextArea component = new JTextArea();		    
		    String originalText = "";

		    //This method is called when a cell value is edited
		    public Component getTableCellEditorComponent(JTable table, 
		    		Object value, boolean isSelected, 
		    		int rowIndex, int vColIndex) {
		    	originalText = (String) value;

		        //Configure the component with the specified value
		        ((JTextArea)component).setText((String)value);

		        //Return the configured component
		        return component;
		    }

		    /*This method is called when editing is completed.
		     *It must return the new value to be stored in the
		     *cell if it is valid
		     */
		    public Object getCellEditorValue() {
		    	String date = ((JTextArea)component).getText();
		    	if(date.isEmpty() || 
		    			new Validator(frame).isValidDate(date)){
		    		//If date is valid then allow it to be stored in table
		    		return date;
		    	}
		    	else{
		    		//Don't show anything
		    		return originalText;
		    	}
		    }
		   
		}
		
		//Set custom editor to each date column
		for(int i=1; i <= badge.getRequirements().length; i++){
			table.getColumnModel().getColumn(i).setCellEditor(
					new DateCellEditor());
		}
		//Turn off auto sizing
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		//Create table sorter for filter
		final TableRowSorter<TableModel> sorter = 
				new TableRowSorter<TableModel>(model);	    
		table.setRowSorter(sorter);
		sorter.setRowFilter(showAllFilter);
		
		//Assign filters to radio buttons
		showAllButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sorter.setRowFilter(showAllFilter);	
			}
		});
		showCompletedButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sorter.setRowFilter(showCompletedFilter);	
			}
		});
		showUncompletedButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sorter.setRowFilter(showUncompletedFilter);	
			}
		});
		
		pane.add(body, BorderLayout.CENTER); //Add to screen
		
		JPanel buttonPanel = new JPanel(); //Button panel
		buttonPanel.setBackground(Color.white);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(
														0, 20, 0, 20));
		//Set box layout on panel
		BoxLayout buttonLayout = new BoxLayout(buttonPanel, 
												BoxLayout.X_AXIS);
		buttonPanel.setLayout(buttonLayout);
		Box buttonBox = Box.createHorizontalBox();
		
		//Create a save button
		JButton save = new JButton("Save progress");
		save.setBackground(new Color(79,129,189));
		save.setFont(new Font("Calibri",Font.PLAIN,20));
		save.setForeground(Color.WHITE);
		save.setFocusable(false);
		
		//Set an on-click listener
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				table.editCellAt(-1, -1); //Change the focus 
				
				//Change flag if the data has been changed
				boolean unchanged = true;
				
				//Store table contents in an array
				DefaultTableModel dtm =(DefaultTableModel)
											table.getModel();
			    int nRow = dtm.getRowCount();
			    int nCol = dtm.getColumnCount();
			    Object[][] tableData = new Object[nRow][nCol];
			    for (int i=0;i<nRow;i++){
			        for (int j=0;j<nCol;j++){
			            tableData[i][j] = dtm.getValueAt(i,j);
			            if(tableData[i][j] != originalData[i][j]){
			            	//If the cell has changed set flag to false
			            	unchanged = false;
			            }
			        }
			    }
			    unchanged = false;
			    if(unchanged){ //If data hasn't been changed then quit 
			    	return;
			    }
			    //Inform user that changes are being saved
			    Thread t = new Thread(new Runnable(){
			        public void run(){
			        	JOptionPane.showMessageDialog(frame,
								"Changes have been saved to the database",
								"Saving",
								JOptionPane.INFORMATION_MESSAGE);
			        }
			    });
			    t.start();
			    
			    Connection db = ScoutManager.connectToDB();
			    for(int i=0;i<nRow;i++){
			    	//Set to true if badge exists
			    	boolean existsFlag = false; 
			    	String sql = "SELECT \"BADGE NAME\" FROM '" 
			    			+ tableData[i][0] + "' WHERE \"BADGE NAME\"" +
			    					" = '" + badge.getName() + "'";
			    	try { 
			    		//Check to see if the badge already has a record
						Statement stmt = db.createStatement();
						ResultSet rs = stmt.executeQuery(sql);
						while(rs.next()){
							//Badge record already in db so change flag
							existsFlag = true;
						}
						if(existsFlag){
							//Generate the sql code required to update db
							sql = "UPDATE '" + tableData[i][0] + "' SET ";
	
					    	for(int j=1; j<=badge.getRequirements()
					    					.length; j++){
					    		//Foreach requirement update the record
					    		sql += "'REQUIREMENT " + j + "' = ";
					    		sql += "'" + tableData[i][j] +"', " ;
					    	}
					    	sql = sql.substring(0, sql.length()-2);
					    	sql += "WHERE \"BADGE NAME\" = '" + 
					    				badge.getName() + "'";
					    	sql = sql.replaceAll("'null'", "''");
					    	//Execute the sql command to update the db
					    	stmt.executeUpdate(sql);
					    	stmt.close();
						}
						else{
							//Insert new badge record
							sql = "INSERT INTO '" + tableData[i][0] +"'("+
					    			"'BADGE NAME', 'REQUIREMENT 1', " +
					    			"'REQUIREMENT 2'," +
					    			" 'REQUIREMENT 3', 'REQUIREMENT 4'," +
					    			" 'REQUIREMENT 5', 'REQUIREMENT 6'," +
					    			" 'REQUIREMENT 7', 'REQUIREMENT 8'," +
					    			" 'REQUIREMENT 9', 'REQUIREMENT 10'" +
					    			") " +
					    			"VALUES('" + badge.getName() + "', ";
					    	
					    	for(int j=1; j<=badge.
					    				getRequirements().length;j++){
					    		sql += "'" + tableData[i][j] +"', " ;
					    	}
					    	sql = sql.substring(0, sql.length()-2);
					    	sql += ")";
					    	sql = sql.replaceAll("'null'", "''");
					    	//Execute the sql command to update the db
					    	stmt.executeUpdate(sql);
					    	stmt.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
			    	
			    	//Set date awarded for anybody who completes badge
			    	final String scoutName = (String) tableData[i][0];
			    	int counter = 0; //Number of requirements completed
					for(int j=1; j<badge.getRequirements().length; j++){
						//Iterate over each date
						if(!((String)tableData[i][j]).isEmpty()){
							//If requirement has been met
							counter += 1; //Increment counter
						}
					}
					//Date badge was first achieved
			        String dateAchieved = "";
					if(counter >= badge.getRequirementsNeeded()){
						/*
						 * If enough requirements have been met for the 
						 * badge to be awarded and no date awarded 
						 * currently exists in the database then the 
						 * current date will be stored as the data 
						 * awarded for this badge.
						 */
						DateFormat formatter = new SimpleDateFormat(
								"dd/MM/yyyy");
						Date date = Calendar.getInstance().getTime();
				        String today = formatter.format(date);
				        
				        sql = "SELECT \"DATE AWARDED\" FROM '" +scoutName+
				        		"' WHERE \"BADGE NAME\"='"+
				        		badge.getName()+ "'";
				        
				        try{
				        	Statement stmt = db.createStatement();
				        	ResultSet rs = stmt.executeQuery(sql);
				        	while(rs.next()){
				        		dateAchieved = rs.getString(1);
				        	}
				        	stmt.close();
				        } catch (SQLException e) {
							e.printStackTrace();
						}
				        if(dateAchieved == null){
				        	//If this is date when badge is achieved
				        	//make changes in the db
							sql = "Update '" + scoutName + "' SET" +
									"\"DATE AWARDED\" = COALESCE(\"" +
									"DATE AWARDED\",'" + today + "')" +
									"WHERE \"BADGE NAME\"=\"" + 
									badge.getName() + "\"";
							try {
								Statement stmt = db.createStatement();
								stmt.executeUpdate(sql);
								stmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							//Inform user that scout has achieved badge
							EventQueue.invokeLater(new Runnable(){
		                        @Override
		                        public void run() {
		                        	 //Create popup for each scout
				                     JOptionPane op = new JOptionPane(
				                    		 scoutName+
				                    		 " has achieved " + 
		                    				 badge.getName(),
				                    		 JOptionPane.
				                    		 	INFORMATION_MESSAGE);
				                     JDialog dialog = op.createDialog("");
				                     dialog.setAlwaysOnTop(true);
				                     dialog.setModal(true);
				                     dialog.setDefaultCloseOperation(
			                    		 JDialog.DISPOSE_ON_CLOSE);      
				                     dialog.setVisible(true);
		                        }
		                    });
				        }
					}
			    }
				try {
					db.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		//Create a print icon
		ImageIcon print = new ImageIcon("./res/print.png");
		JLabel printButton = new JLabel(print);
		//If button pressed then print table
		printButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					table.print();
				} catch (PrinterException e) {
					e.printStackTrace();
				}	
			}
			
			//Required methods but useless
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		
		//Add to print panel
		buttonBox.add(save);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(printButton);
		buttonPanel.add(buttonBox);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
}