import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
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


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * This is the screen where the user adds a new scout
 * to the Scouts table in the database by entering
 * data into a form. The system then adds them to the 
 * Scouts table and creates a personal table for that 
 * scout
 * @author Matthew Brookes
 */
public class AddScout {

	/**
	 * The class' only constructor has a JFrame as an argument which
	 * is passed from the home screen. On this frame the form is drawn
	 * as well as labels directing the user on what data is required.
	 * The back button will be directed towards the Scout Management screen.
	 * @param frame The screen where buttons will be drawn
	 */
	public AddScout(JFrame frame) {
		System.out.println("Add a new scout");
		Container pane = frame.getContentPane();
		pane.removeAll();
		pane.repaint();
		drawScreen(frame);
		frame.setVisible(true);
	}
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
		JLabel title = new JLabel("NEW SCOUT");
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
		body.setLayout(new GridLayout(0, 2));
		body.setBorder(new EmptyBorder(20,20,0,200));
		
		JLabel nameLabel = new JLabel("Name:");
		final JTextField nameField = new JTextField(40);
		
		JLabel emailLabel = new JLabel("Email Address:");
		final JTextField emailField = new JTextField(40);
		
		JLabel phoneLabel = new JLabel("Phone Number:");
		final JTextField phoneField = new JTextField(11);
		
		JLabel dobLabel = new JLabel("Date of Birth:");
		final JTextField dobField = new JTextField(10);
		dobField.setText("DD/MM/YYYY");
		
		JLabel dateJoinedLabel = new JLabel("Date Joined:");
		final JTextField dateJoinedField = new JTextField(10);
		dateJoinedField.setText("DD/MM/YYYY");
		
		JLabel line1Label = new JLabel("Address Line 1:");
		final JTextField line1Field = new JTextField(40);
		
		JLabel line2Label = new JLabel("Address Line 2:");
		final JTextField line2Field = new JTextField(40);
		
		JLabel line3Label = new JLabel("Address Line 3:");
		final JTextField line3Field = new JTextField(40);
		
		JLabel line4Label = new JLabel("Address Line 4:");
		final JTextField line4Field = new JTextField(40);
		
		JLabel patrolLabel = new JLabel("Patrol:");
		
		//Create drop down box for patrol
		String[] patrols = {"Bulldog", "Cobra", "Lion",
				"Panther", "Tiger", "Wolf"};
		final JComboBox<String> patrolBox = new JComboBox<String>(patrols);
		patrolBox.setSelectedIndex(0);
		patrolBox.setBackground(Color.WHITE);
		
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
		
		JButton addButton = new JButton("Add scout");
		addButton.setBackground(new Color(79,129,189));
		addButton.setFont(new Font("Calibri",Font.PLAIN,20));
		addButton.setForeground(Color.WHITE);
		addButton.setFocusable(false);
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
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
					
					//All the details are valid so scout should be added to DB
					Connection db = ScoutManager.connectToDB(); //Connect to the database
					try {
						Statement stmt = db.createStatement();
						String sql = "SELECT * FROM SCOUTS WHERE NAME='" + 
								nameField.getText() + "'"; //If scout already in DB
						ResultSet rs = stmt.executeQuery(sql); //Find in DB
						while(rs.next()){
							//Will reach this point if scout already in DB
							JOptionPane.showMessageDialog(frame,
									"Scout already exists in database",
									"Database error",
									JOptionPane.ERROR_MESSAGE);
							stmt.close();
							db.close();
							return;
						}
						stmt.close();
						//Add new scout to db as it doesn't exist
						sql = "INSERT INTO SCOUTS (NAME, 'EMAIL ADDRESS', " +
								"'PHONE NUMBER', DOB, 'DATE JOINED', ADDRESS1, " +
								"ADDRESS2, ADDRESS3, ADDRESS4, PATROL) " +
								"VALUES ('" + nameField.getText() + "','" +
								emailField.getText()+"','" + phoneField.getText() + "','" +
								dobField.getText()+"','" +dateJoinedField.getText() + "','" +
								line1Field.getText()+"', '"+line2Field.getText()+"', '" +
								line3Field.getText()+"', '"+line4Field.getText()+"', '" +
								patrolBox.getSelectedItem() +"');";
						stmt.executeUpdate(sql);
						stmt.close();
						//Create a new table for the scout
						sql = "CREATE TABLE '" + nameField.getText().toUpperCase() + "'(" +
								"'BADGE NAME'		VARCHAR(20)		NOT NULL, " +
								"'REQUIREMENT 1'	VARCHAR(10)				, " +
								"'REQUIREMENT 2'	VARCHAR(10)				, " +
								"'REQUIREMENT 3'	VARCHAR(10)				, " +
								"'REQUIREMENT 4'	VARCHAR(10)				, " +
								"'REQUIREMENT 5'	VARCHAR(10)				, " +
								"'REQUIREMENT 6'	VARCHAR(10)				, " +
								"'REQUIREMENT 7'	VARCHAR(10)				, " +
								"'REQUIREMENT 8'	VARCHAR(10)				, " +
								"'REQUIREMENT 9'	VARCHAR(10)				, " +
								"'REQUIREMENT 10'	VARCHAR(10)				, " +
								"'DATE AWARDED'		VARCHAR(10)				, " +
								"'DATE PRESENTED'	VARCHAR(10))";
						stmt.executeUpdate(sql);
						stmt.close();
						db.close();
						//Restart screen
						new AddScout(frame);
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
	}
}
