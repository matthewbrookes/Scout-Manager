import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 * This is the screen which the user visits to display
 * the emergency contact sheet. The details for each scout
 * are rendered in a table which can be printed.
 * @author Matthew Brookes
 */

/**
* The class' only constructor has a JFrame as an argument which
* is passed from the report screen. On this frame the details for
* the scouts are displayed in a table along with a print button.
* @param frame The screen where table will be drawn
*/
public class EmergencyContactSheet{
	
	EmergencyContactSheet(JFrame frame) {
		System.out.println("Emergency Contact Sheet");
		Container pane = frame.getContentPane();
		pane.removeAll();
		pane.repaint();
		drawScreen(frame);
		frame.setVisible(true);
	}
	/**
	 * This method draws the header and body of the emergency contact 
	 * sheet screen on the frame which is passed
	 * @param frame The current window
	 */
	private static void drawScreen(final JFrame frame){
		Container pane = frame.getContentPane(); //Main screen
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
				//Return to reports
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
		JLabel title = new JLabel("<html>" +
				"<center>EMERGENCY CONTACT SHEET</center></html>");
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
		final JPanel body = new JPanel();
		body.setBackground(Color.WHITE);
		body.setLayout(new BorderLayout());
		
		//Column names for table
		Object[] columnNames = {"Name",
                "Phone Number",
                "Home Address",
                "Email Address"};
		
		Connection db = ScoutManager.connectToDB();
		//Holds all scout names
		ArrayList<String> namesInDb = new ArrayList<String>(); 
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
		
		//Will hold the data for the scouts
		Object[][] data = new Object[namesInDb.size()][4];
		db = ScoutManager.connectToDB();
		for(final String name: namesInDb){
			//Foreach scout in database retrieve details and add
			//to data array.
			Object[] row = new Object[4];
			row[0] = name;
			try {
				//Retrieve data from db for scout
				Statement stmt = db.createStatement();
				String sql = "SELECT \"PHONE NUMBER\",\"ADDRESS1\"," +
						"\"ADDRESS2\",\"ADDRESS3\",\"ADDRESS4\"," +
						"\"EMAIL ADDRESS\" FROM SCOUTS WHERE NAME='" +
						name + "'";
				ResultSet rs = stmt.executeQuery(sql);
				//Put scout data in row object
				row[1] = rs.getObject(1) ;
				
				//Add address from database
				String address = "";
				for(int i=2; i<6; i++){ //Iterate over each line of address
					if(!rs.getString(i).isEmpty()){ //If string isn't empty
						//Add with comma and line break
						address += rs.getString(i) + ",\n"; 
					}
					else{
						address += "\n"; //Just add line break
					}
				}
				
				row[2] = address;
				row[3] = rs.getString(6) ;
				data[namesInDb.indexOf(name)] = row;
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		//Close connections
		try {
			db.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		//Create table to display data. Make it non-editable.
		final JTable table = new JTable(data, columnNames) {
	        private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {                
	                return false;               
	        };
	    };
		
		//Create a renderer for long text so it wraps inside cell
		class TableCellLongTextRenderer extends JTextArea 
										implements TableCellRenderer{  
			private static final long serialVersionUID = 1L;

			@Override  
		    public Component getTableCellRendererComponent(JTable table,
		    		Object value, boolean isSelected, boolean hasFocus, 
		    		int row, int column) {  
		        this.setText((String)value);  
		        this.setWrapStyleWord(true);                      
		        this.setLineWrap(true); 
		  
		        //set the JTextArea to the width of the table column  
		        setSize(table.getColumnModel().getColumn(column).getWidth(),
		        		getPreferredSize().height);  
		        
		          
		        return this;  
		    }  
		} 
		
		//Create a renderer for text that goes over multiple 
		//lines i.e. the address
		class TableCellLargeRenderer extends JTextArea 
									 implements TableCellRenderer{  
			private static final long serialVersionUID = 1L;

			@Override  
		    public Component getTableCellRendererComponent(JTable table, 
		    		Object value, boolean isSelected, boolean hasFocus, 
		    		int row, int column) {  
		        this.setText((String)value);  
		        this.setWrapStyleWord(true);                      
		        this.setLineWrap(true); 
		  
		        //Set the JTextArea to the width of the table column  
		        setSize(table.getColumnModel().getColumn(column).getWidth(),
		        		getPreferredSize().height);
            	
		        //Set the row height to this height
		        table.setRowHeight(row, getPreferredSize().height);
		        
		        return this;  
		    }  
		} 
		
		//Assign a renderer to each column
		table.getColumnModel().getColumn(0).setCellRenderer(
				new TableCellLongTextRenderer());
		table.getColumnModel().getColumn(1).setCellRenderer(
				new TableCellLongTextRenderer());
		table.getColumnModel().getColumn(1).setPreferredWidth(0);
		table.getColumnModel().getColumn(2).setCellRenderer(
				new TableCellLargeRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(
				new TableCellLongTextRenderer());
		
		//Make table scrollable if exceeds screen size
		final JScrollPane scrollTable = new JScrollPane(table);
		scrollTable.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		body.add(scrollTable);
		
		pane.add(body, BorderLayout.CENTER); //Add body to main screen
		
		//Create a panel to hold the print button
		JPanel printPanel = new JPanel();
		printPanel.setBackground(Color.WHITE);
		printPanel.setBorder(new EmptyBorder(0, 600, 0, 0));
		
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
		printPanel.add(printButton);
		pane.add(printPanel, BorderLayout.SOUTH);
	}
}