import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * This is the screen which the user visits to display
 * the email addresses of the scouts in the database. 
 * The user selects the email addresses to be exported
 * as comma separated values through checkboxes in a table.
 * @author Matthew Brookes
 */

/**
* The class' only constructor has a JFrame as an argument which
* is passed from the report screen. On this frame the names and email
* addresses of all scouts in the database are drawn in a table.
* @param frame The screen where table will be drawn
*/
public class EmailAddresses{
	
	EmailAddresses(JFrame frame) {
		System.out.println("Emergency Contact Sheet");
		Container pane = frame.getContentPane();
		pane.removeAll();
		pane.repaint();
		drawScreen(frame);
		frame.setVisible(true);
	}
	/**
	 * This method draws the header and body of the scout
	 * management screen on the frame which is passed
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
				"<center>EMAIL ADDRESSES</center></html>");
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
		JScrollPane scrollBody = new JScrollPane(body);
		body.setBackground(Color.WHITE);
		body.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		//Array to hold all checkboxes
		final ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
		
		/* Create the master checkbox in header of first column
		 * if this box is selected then all boxes will be selected
		 * and if it is deselected then all boxes will be deselected
		 * Show in first row and first column
		 */

		final JCheckBox masterCheckBox = new JCheckBox();
		masterCheckBox.setSelected(true);
		masterCheckBox.setBackground(Color.white);
		masterCheckBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//If button pressed change the values of other checkboxes
				if(!masterCheckBox.isSelected()){ //If it's already active
					//Deactivate all checkboxes and change value to false
					for(int i = 0; i<checkBoxes.size(); i++){
						checkBoxes.get(i).setSelected(false);
						masterCheckBox.setSelected(false);
						body.repaint();
					}
				}
				else{
					//Activate all checkboxes and change value to true
					for(int i = 0; i<checkBoxes.size(); i++){
						checkBoxes.get(i).setSelected(true);
						masterCheckBox.setSelected(true);
						body.repaint();
					}
				}
				
			}
			
		});
		
		constraints.gridx = 0;
		constraints.weightx = 0;
		constraints.gridy = 0;
		constraints.ipady = 20;
		body.add(masterCheckBox, constraints);
		
		//Reset two constraints
		constraints.weightx = 0.5;
		constraints.ipady = 0;
		
		//Add labels for table
		JLabel nameHeader = new JLabel("Name");
		nameHeader.setFont(new Font("Dialog", Font.BOLD, 18));
		constraints.gridx = 1;
		body.add(nameHeader, constraints);
		JLabel emailHeader = new JLabel("Email Address");
		emailHeader.setFont(new Font("Dialog", Font.BOLD, 18));
		constraints.gridx = 2;
		body.add(emailHeader, constraints);
		
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
		final String[][] data = new String[namesInDb.size()][2];
		db = ScoutManager.connectToDB();
		for(final String name: namesInDb){
			//Foreach scout in database retrieve details and add
			//to data array.
			String[] row = new String[2];
			row[0] = name;
			try {
				//Retrieve email address from db for scout
				Statement stmt = db.createStatement();
				String sql = "SELECT "  +
						"\"EMAIL ADDRESS\" FROM SCOUTS WHERE NAME='" +
						name + "'";
				ResultSet rs = stmt.executeQuery(sql);
				//Put email address in row object
				row[1] = rs.getString(1) ;
				
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
		
		for(int i=0; i<data.length; i++){
			//Foreach scout add to screen and add checkbox to array
			final JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(true);
			checkBox.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//If deactivated then change master checkbox value to false
					if(!checkBox.isSelected()){
						masterCheckBox.setSelected(false);
						body.repaint();
					}
					
				}
				
			});
			//Add checkbox, name and email to screen
			checkBoxes.add(checkBox);
			constraints.gridy=(i+1);
			constraints.gridx=0;
			body.add(checkBox, constraints);
			
			JLabel name = new JLabel(data[i][0]);
			constraints.gridx=1;
			body.add(name, constraints);
			
			JLabel email = new JLabel(data[i][1]);
			constraints.gridx=2;
			body.add(email, constraints);
		}
		
		pane.add(scrollBody, BorderLayout.CENTER); //Add body to main screen
		
		//Create a panel to hold the export button
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setBorder(new EmptyBorder(0, 600, 0, 0));
		
		//Create a button for exporting email addresses
		JButton exportButton = new JButton(
				"<html><center>Export Email Addresses</center></html>");
		exportButton.setBackground(new Color(79,129,189));
		exportButton.setFont(new Font("Calibri",Font.PLAIN,20));
		exportButton.setForeground(Color.WHITE);
		exportButton.setFocusable(false);
		
		//When button is clicked export selected email addresses
		exportButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String emailText = ""; //Emails as comma separated values
				for(int i=0; i<checkBoxes.size(); i++){
					if(checkBoxes.get(i).isSelected()){
						ArrayList<String> emails = new ArrayList<String>();
						emails.add(data[i][1]);
						
						for(String email: emails){
							//Foreach email address, concatenate with large
							//email text along with a comma
							emailText += email;
							emailText += ",";
						}
					}
				}
				//Remove last comma if emailText contains email addresses
				if(emailText.length() > 0){
					emailText = emailText.substring(
							0, emailText.length() - 1);
				}
				
				//Create a text area to display email addresses and output
				//on a popup message
				JTextArea ta = new JTextArea(20, 20);
                ta.setText(emailText);
                ta.setWrapStyleWord(true);
                ta.setLineWrap(true);
                ta.setCaretPosition(0);
                ta.setEditable(false);
				JOptionPane.showMessageDialog(frame,
						new JScrollPane(ta),
						"Export Email Addresses",
						JOptionPane.PLAIN_MESSAGE);
			}	
		});
		//Add button to screen
		buttonPanel.add(exportButton);
		pane.add(buttonPanel, BorderLayout.SOUTH);
	}
}

