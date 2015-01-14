import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 * This screen will first display a list of scouts in the
 * database which can be selected. The scouts can be selected
 * and then their information is displayed so that it can be
 * edited and changes saved back into the database.
 * @author Matthew Brookes
 */
public class UpdateScout {
	/**
	 * The class' only constructor has a JFrame as an argument which
	 * is passed from the home screen. On this frame the scout names
	 * are displayed then the same form as the Add Scout screen is displayed.
	 * The back button will be directed towards the Scout Management screen.
	 * @param frame The screen where buttons will be drawn
	 */
	public UpdateScout(JFrame frame){
		System.out.println("Update a scout");
		Container pane = frame.getContentPane();
		pane.removeAll();
		pane.repaint();
		drawScreen(frame);
		frame.setVisible(true);
	}
	
	/**
	 * This function draws the first screen the user sees when this
	 * section is started. It consists of the names of all scouts
	 * in the database in two columns which are ordered by firstname.
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
		
		header.setBackground(new Color(139,0,102)); //Set purple color to background
		//Create home and back buttons
		ImageIcon back = new ImageIcon(ScoutManager.class.getResource("back.png"));
		JLabel backButton = new JLabel(back);
		backButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Return to main screen
				new ScoutManagement(frame);
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
        scrollBody.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		Connection db = ScoutManager.connectToDB();
		ArrayList<String> namesInDb = new ArrayList<String>(); //Holds all scout names
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
					drawUpdateScreen(frame, name);
				}
				//Useless methods but required
				@Override
				public void mouseEntered(MouseEvent e) {					
				}
				@Override
				public void mouseExited(MouseEvent e) {					
				}
				@Override
				public void mousePressed(MouseEvent e) {					
				}
				@Override
				public void mouseReleased(MouseEvent e) {					
				}
				
			});
			body.add(nameLabel);
		}
		
		pane.add(scrollBody, BorderLayout.CENTER);	
	}
	
	/**
	 * This method changes the layout of the screen to match the add
	 * scout screen and then fills the fields with the data for the
	 * selected scout thus giving the user an opportunity to make 
	 * changes which will be stored in the database.
	 * @param frame The screen where the layout will be drawn
	 * @param name The name of the scout to be updated
	 */
	private static void drawUpdateScreen(final JFrame frame, final String name){
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
		
		header.setBackground(new Color(139,0,102)); //Set purple color to background
		//Create home and back buttons
		ImageIcon back = new ImageIcon(ScoutManager.class.getResource("back.png"));
		JLabel backButton = new JLabel(back);
		backButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Return to main screen
				new ScoutManagement(frame);
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
		JLabel title = new JLabel(name.toUpperCase());
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
		final Scout scout = new Scout(); // Will hold the data about the scout
		try {
			// Retrieve existing information from database
			Statement stmt = db.createStatement();
			String sql = "SELECT * FROM SCOUTS WHERE NAME=\"" + name + "\"";
			ResultSet rs = stmt.executeQuery(sql); 
			while(rs.next()){
				// Store information in scout object
				scout.setName(name);
				scout.setEmail(rs.getString(2));
				scout.setPhone(rs.getString(3));
				scout.setDob(rs.getString(4));
				scout.setDateJoined(rs.getString(5));
				String[] scoutAddress = {"", "", "", ""};
				scoutAddress[0] = rs.getString(6);
				scoutAddress[1] = rs.getString(7);
				scoutAddress[2] = rs.getString(8);
				scoutAddress[3] = rs.getString(9);
				scout.setAddress(scoutAddress);
				scout.setPatrol(rs.getString(10));
			}
			// Tidy up connections
			stmt.close();
			db.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Draw layout of form and populate with information
		JPanel body = new JPanel();
		body.setBackground(Color.WHITE);
		body.setLayout(new GridLayout(0, 2));
		body.setBorder(new EmptyBorder(20,20,0,200));
		
		JLabel nameLabel = new JLabel("Name:");
		final JTextField nameField = new JTextField(40);
		nameField.setText(scout.getName());
		
		JLabel emailLabel = new JLabel("Email Address:");
		final JTextField emailField = new JTextField(40);
		emailField.setText(scout.getEmail());
		
		JLabel phoneLabel = new JLabel("Phone Number:");
		final JTextField phoneField = new JTextField(11);
		phoneField.setText(scout.getPhone());
		
		JLabel dobLabel = new JLabel("Date of Birth:");
		final JTextField dobField = new JTextField(10);
		dobField.setText(scout.getDob());
		
		JLabel dateJoinedLabel = new JLabel("Date Joined:");
		final JTextField dateJoinedField = new JTextField(10);
		dateJoinedField.setText(scout.getDateJoined());
		
		JLabel line1Label = new JLabel("Address Line 1:");
		final JTextField line1Field = new JTextField(40);
		line1Field.setText(scout.getAddress()[0]);
		
		JLabel line2Label = new JLabel("Address Line 2:");
		final JTextField line2Field = new JTextField(40);
		line2Field.setText(scout.getAddress()[1]);
		
		JLabel line3Label = new JLabel("Address Line 3:");
		final JTextField line3Field = new JTextField(40);
		line3Field.setText(scout.getAddress()[2]);
		
		JLabel line4Label = new JLabel("Address Line 4:");
		final JTextField line4Field = new JTextField(40);
		line4Field.setText(scout.getAddress()[3]);
		
		JLabel patrolLabel = new JLabel("Patrol:");
		
		//Create drop down box for patrol
		String[] patrols = {"Bulldog", "Cobra", "Lion",
				"Panther", "Tiger", "Wolf"};
		final JComboBox<String> patrolBox = new JComboBox<String>(patrols);
		patrolBox.setBackground(Color.WHITE);
		patrolBox.setSelectedItem(scout.getPatrol());
		
		//Add buttons and labels to body panel
		body.add(nameLabel);
		body.add(nameField);
		body.add(emailLabel);
		body.add(emailField);
		body.add(phoneLabel);
		body.add(phoneField);
		body.add(dobLabel);
		body.add(dobField);
		body.add(dateJoinedLabel);
		body.add(dateJoinedField);
		body.add(line1Label);
		body.add(line1Field);
		body.add(line2Label);
		body.add(line2Field);
		body.add(line3Label);
		body.add(line3Field);
		body.add(line4Label);
		body.add(line4Field);
		body.add(patrolLabel);
		body.add(patrolBox);
		
		pane.add(body, BorderLayout.CENTER); //Add to screen
		
		//Create a panel to hold action buttons
		JPanel buttons = new JPanel();
		buttons.setBorder(new EmptyBorder(0, 380, 0, 50));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.setBackground(Color.WHITE);
		Box buttonBox = Box.createHorizontalBox();
		
		JButton addButton = new JButton("Update scout");
		addButton.setBackground(new Color(79,129,189));
		addButton.setFont(new Font("Calibri",Font.PLAIN,20));
		addButton.setForeground(Color.WHITE);
		addButton.setFocusable(false);
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Check fields are all valid
				Validator v = new Validator(frame);
				if(v.isValidName(nameField.getText()) &&
					v.isValidEmail(emailField.getText()) &&
					v.isValidPhone(phoneField.getText()) &&
					v.isValidDate(dobField.getText()) &&
					v.isValidDate(dateJoinedField.getText()) &&
					v.isValidCompulsoryAddress(line1Field.getText()) &&
					v.isValidAddress(line2Field.getText()) &&
					v.isValidAddress(line3Field.getText()) &&
					v.isValidAddress(line4Field.getText())){
					
					//Assign new values to scout object
					scout.setName(nameField.getText());
					scout.setEmail(emailField.getText());
					scout.setPhone(phoneField.getText());
					scout.setDob(dobField.getText());
					scout.setDateJoined(dateJoinedField.getText());
					String[] scoutAddress = {"", "", "", ""};
					scoutAddress[0] = line1Field.getText();
					scoutAddress[1] = line2Field.getText();
					scoutAddress[2] = line3Field.getText();
					scoutAddress[3] = line4Field.getText();
					scout.setAddress(scoutAddress);
					scout.setPatrol((String) patrolBox.getSelectedItem());
					
					//All the details are valid so scout can be updated in DB
					Connection db = ScoutManager.connectToDB(); //Connect to the database
					try {
						Statement stmt = db.createStatement();
						//Update details in the DB
						String sql = "UPDATE SCOUTS SET 'NAME'='" + scout.getName() + "'," +
								"'EMAIL ADDRESS'='" + scout.getEmail() +"'," +
								"'PHONE NUMBER'='" + scout.getPhone() + "'," +
								"'DOB'='" + scout.getDob() + "'," +
								"'DATE JOINED'='" + scout.getDateJoined() + "'," +
								"'ADDRESS1'='" + scout.getAddress()[0] + "'," +
								"'ADDRESS2'='" + scout.getAddress()[1] + "'," +
								"'ADDRESS3'='" + scout.getAddress()[2] + "'," +
								"'ADDRESS4'='" + scout.getAddress()[3] + "'," +
								"'PATROL'='" + scout.getPatrol() + "' " +
								"WHERE NAME='" + name + "'";
						stmt.executeUpdate(sql);
						stmt.close();
						
						//Change the name of the table for that scout
						sql = "ALTER TABLE " + name + " RENAME TO " + scout.getName();
						try{
							stmt.executeUpdate(sql);
						}
						catch(Exception error){
							/* Will reach this point if name is unchanged
							 * Closes the database connections and restarts
							 * the screen.
							 */
							stmt.close();
							db.close();
							new UpdateScout(frame);
						}
						stmt.close();
						db.close();
						//Restart screen
						new UpdateScout(frame);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					
				}
			}
			
		});
		buttonBox.add(addButton);
		
		buttonBox.add(Box.createHorizontalGlue());
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBackground(new Color(79,129,189));
		cancelButton.setFont(new Font("Calibri",Font.PLAIN,20));
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Return to the Scout Management screen
				new ScoutManagement(frame);
			}			
		});
		buttonBox.add(cancelButton);
		
		buttons.add(buttonBox);
		pane.add(buttons, BorderLayout.SOUTH);
		
		
		frame.setVisible(true);
	}
}
