import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
 * This screen will first display a list of scouts in the
 * database which can be selected. The scout can be selected
 * and then the date the scout was awarded and presented each badge 
 * is displayed so that it can be edited and changes saved 
 * back into the database.
 * @author Matthew Brookes
 */
public class Scouts {
	/**
	 * The class' only constructor has a JFrame as an argument which
	 * is passed from the previous screen. On this frame the scout names
	 * are displayed then the table for progress is displayed.
	 * The back button will be directed towards the Report screen.
	 * @param frame The screen where buttons will be drawn
	 */
	public Scouts(JFrame frame){
		System.out.println("Scouts");
		Container pane = frame.getContentPane();
		pane.removeAll();
		pane.repaint();
		drawScreen(frame);
		frame.setVisible(true);
	}
	
	/**
	 * This function draws the first screen the user sees when this
	 * section is started. It consists of the names of all scouts
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
		ImageIcon back = new ImageIcon(ScoutManager.class.getResource("back.png"));
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
		
		ImageIcon home = new ImageIcon(ScoutManager.class.getResource("home.png"));
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
		JLabel title = new JLabel("SCOUTS");
		title.setFont(new Font("Verdana",Font.PLAIN, 40));
		title.setForeground(new Color(237,119,3));
		box.add(Box.createHorizontalGlue());
		box.add(title);
		
		//Add scout logo
		ImageIcon scoutLogo = new ImageIcon(ScoutManager.class.getResource("scout_logo.png"));
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
		//Holds all scout names
		ArrayList<String> namesInDb = new ArrayList<String>();
		try {
			//Retrieve all scouts from db
			Statement stmt = db.createStatement();
			String sql = "SELECT NAME FROM SCOUTS";
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
			//Foreach scout in database draw a label
			JLabel nameLabel = new JLabel(name);
			nameLabel.setFont(new Font("Calibri", Font.PLAIN, 18));
			nameLabel.setHorizontalAlignment(JLabel.CENTER);
			//If label clicked then show data for that scout
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
	 * with the dates that the scout was awarded and presented the badge
	 * and the user has an opportunity to make 
	 * changes which will be stored in the database.
	 * @param frame The screen where the layout will be drawn
	 * @param scoutName The name of the scout to be updated
	 */
	private static void drawProgressTable(
			final JFrame frame, final String scoutName){
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
		ImageIcon back = new ImageIcon(ScoutManager.class.getResource("back.png"));
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
		
		ImageIcon home = new ImageIcon(ScoutManager.class.getResource("home.png"));
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
		JLabel title = new JLabel(scoutName.toUpperCase());
		title.setFont(new Font("Verdana",Font.PLAIN, 30));
		title.setForeground(new Color(237,119,3));
		title.setPreferredSize(new Dimension(400, 100));
		title.setHorizontalAlignment(JLabel.CENTER);
		box.add(Box.createHorizontalGlue());
		box.add(title);
		
		//Add scout logo
		ImageIcon scoutLogo = new ImageIcon(ScoutManager.class.getResource("scout_logo.png"));
		JLabel logo = new JLabel(scoutLogo);
		logo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//Add to header
		box.add(Box.createHorizontalGlue());
		box.add(logo);
		header.add(box);
		
		pane.add(header, BorderLayout.NORTH); //Add header to main screen
		
		Connection db = ScoutManager.connectToDB();
		db = ScoutManager.connectToDB();
		//Holds all badge names
		final ArrayList<String> namesInDb = new ArrayList<String>(); 
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
		
		db = ScoutManager.connectToDB();
		//Array will hold the unfiltered data from db
		final Object[][] originalData = new Object[namesInDb.size()]
				[3];
		for(String badgeName : namesInDb){
			try {
				//Retrieve progress data from db
				Statement stmt = db.createStatement();
				Object[] progress = new Object[3];
				progress[0] = badgeName;
				
				//Initialise rest of array with blank strings
				progress[1] = "";
				progress[2] = "";
				
				String sql = "SELECT \"DATE AWARDED\",\"DATE PRESENTED\""+
						" FROM '" + scoutName + "' " +
						"WHERE \"BADGE NAME\"='" + badgeName + "'";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					//Add dates to array
					progress[1] = rs.getObject(1);
					progress[2] = rs.getObject(2);
				}
				originalData[namesInDb.indexOf(badgeName)] = progress;
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
				"Show all badges");
		showAllButton.setSelected(true);
		showAllButton.setBackground(Color.white);
		JRadioButton showUnpresentedButton = new JRadioButton(
				"Show badges which have not been presented");
		showUnpresentedButton.setBackground(Color.white);
		JRadioButton showUnawardedButton = new JRadioButton(
				"Show badges which have not been awarded");
		showUnawardedButton.setBackground(Color.white);
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
		final RowFilter<Object, Object> showUnpresentedFilter = 
				new RowFilter<Object, Object>(){
			@Override
			public boolean include(
					Entry<? extends Object, ? extends Object> entry) {
				//Iterate through each badge name
				for (int i = entry.getValueCount() - 1; i >= 0; i--) {
					String scoutName = entry.getStringValue(i);
					for(int j=0; j<namesInDb.size(); j++){
						//Find badge in the array
						if(((String)originalData[j][0])
								.equals(scoutName)){
							//If found the badge in array
							if(originalData[j][2] == null 
								||((String)originalData[j][2]).isEmpty()){
								//If not presented return true
								return true;
							}
							else{
								return false;
							}
						}
					}
				}
				return false;
			}	
		};
		final RowFilter<Object, Object> showUnawardedFilter = 
				new RowFilter<Object, Object>(){
			@Override
			public boolean include(
					Entry<? extends Object, ? extends Object> entry) {
				//Iterate through each badge name
				for (int i = entry.getValueCount() - 1; i >= 0; i--) {
					String badgeName = entry.getStringValue(i);
					for(int j=0; j<namesInDb.size(); j++){
						//Find badge in the array
						if(((String)originalData[j][0])
								.equals(badgeName)){
							//If found the badge in array
							if(originalData[j][1] == null 
								||((String)originalData[j][1]).isEmpty()){
								//If not awarded return true
								return true;
							}
							else{
								return false;
							}
						}
					}
				}
				return false;
			}
		};
		
		//Group buttons so only one can be selected at a time
		ButtonGroup group = new ButtonGroup();
		group.add(showAllButton);
		group.add(showUnpresentedButton);
		group.add(showUnawardedButton);
		
		//Add buttons to the body
		body.add(showAllButton);
		body.add(showUnpresentedButton);
		body.add(showUnawardedButton);
		
		//Create array of column names
		Object[] columnNames = new Object[]{"Badge", "Date Awarded",
											"Date Presented"};
		
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
		for(int i=1; i <= 2; i++){
			table.getColumnModel().getColumn(i).setCellEditor(
					new DateCellEditor());
		}
		
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
		showUnpresentedButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sorter.setRowFilter(showUnpresentedFilter);	
			}
		});
		showUnawardedButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sorter.setRowFilter(showUnawardedFilter);	
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
			    			+ scoutName + "' WHERE \"BADGE NAME\"" +
			    					" = '" + tableData[i][0] + "'";
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
							sql = "UPDATE '" + scoutName + "' SET " +
							"\"DATE AWARDED\" = '" + tableData[i][1] +
							"', \"DATE PRESENTED\" = '" + tableData[i][2]
							+"' " + "WHERE \"BADGE NAME\" = '" + 
		    				tableData[i][0] + "'";
							
					    	sql = sql.replaceAll("'null'", "''");
					    	//Execute the sql command to update the db
					    	stmt.executeUpdate(sql);
					    	stmt.close();
						}
						else{
							//Insert new badge record
							sql = "INSERT INTO '" + scoutName +"'("+
					    			"'BADGE NAME', 'DATE AWARDED', " +
					    			"'DATE PRESENTED')" +
					    			"VALUES('" + tableData[i][0] + "', " +
					    			"'" + tableData[i][1] + "', " +
					    			"'" + tableData[i][2] + "')";
					    	
					    	sql = sql.replaceAll("'null'", "''");
					    	//Execute the sql command to update the db
					    	stmt.executeUpdate(sql);
					    	stmt.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
			    }
			}
		});
		
		//Create a print icon
		ImageIcon print = new ImageIcon(ScoutManager.class.getResource("print.png"));
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