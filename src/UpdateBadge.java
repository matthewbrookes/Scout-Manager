import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


/**
 * This screen will first display a list of badges in the
 * database which can be selected. The badges can be selected
 * and then their information is displayed so that it can be
 * edited and changes saved back into the database.
 * @author Matthew Brookes
 */
public class UpdateBadge {
	/**
	 * The class' only constructor has a JFrame as an argument which
	 * is passed from the home screen. On this frame the badge names
	 * are displayed then the same form as the Add Badge screen is displayed.
	 * The back button will be directed towards the Badge Management screen.
	 * @param frame The screen where buttons will be drawn
	 */
	public UpdateBadge(JFrame frame){
		System.out.println("Update a badge");
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
		
		header.setBackground(new Color(139,0,102)); //Set purple color to background
		//Create home and back buttons
		ImageIcon back = new ImageIcon(ScoutManager.class.getResource("back.png"));
		JLabel backButton = new JLabel(back);
		backButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//Return to main screen
				new BadgeManagement(frame);
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
		JLabel title = new JLabel("BADGES");
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
		ArrayList<String> namesInDb = new ArrayList<String>(); //Holds all badge names
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
	 * badge screen and then fills the fields with the data for the
	 * selected badge thus giving the user an opportunity to make 
	 * changes which will be stored in the database.
	 * @param frame The screen where the layout will be drawn
	 * @param name The name of the badge to be updated
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
				//Return to badge management screen
				new BadgeManagement(frame);
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
		final Badge badge = new Badge(); // Will hold the data about the badge
		try {
			// Retrieve existing information from database
			Statement stmt = db.createStatement();
			String sql = "SELECT * FROM BADGES WHERE NAME=\"" + name + "\"";
			ResultSet rs = stmt.executeQuery(sql); 
			while(rs.next()){
				// Store information in badge object
				badge.setName(name);
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Draw layout of form and populate with information
		JPanel body = new JPanel();
		body.setBackground(Color.WHITE);
		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
		body.setBorder(new EmptyBorder(20,20,0,200));
		
		/*Create nested panels containing labels and fields
		 *which is where the user will input data about the
		 *badge which is being added to the db
		 */
		
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		namePanel.setBackground(Color.WHITE);
		JLabel nameLabel = new JLabel("Name:");
		final JTextField nameField = new JTextField(40);
		nameField.setText(badge.getName());
		nameField.setEditable(false);
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		namePanel.setMaximumSize(new Dimension(800,100));
		body.add(namePanel);
		
		//Using a numeric spinner
		JPanel spinnerPanel = new JPanel();
		spinnerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		spinnerPanel.setBackground(Color.WHITE);
		JLabel requirementsLabel = new JLabel(
				"Number of requirements needed for completion:");
		final SpinnerModel requirementsSpinner =
				new SpinnerNumberModel(1,1,10,1);
		final JSpinner spinner = new JSpinner(requirementsSpinner);
		spinner.setValue(badge.getRequirementsNeeded());
		spinnerPanel.add(requirementsLabel);
		spinnerPanel.add(spinner);
		spinnerPanel.setMaximumSize(new Dimension(800, 100));
		body.add(spinnerPanel);
		
		JPanel requirementsPanel = new JPanel();
		requirementsPanel.setLayout(new GridLayout(0, 2));
		requirementsPanel.setBackground(Color.WHITE);
		
		JLabel requirement1Label = new JLabel("Requirement 1: ");
		final JTextField requirement1Field = new JTextField(200);
		requirement1Field.setText(badge.getRequirements()[0]);
		requirementsPanel.add(requirement1Label);
		requirementsPanel.add(requirement1Field);
		
		JLabel requirement2Label = new JLabel("Requirement 2: ");
		final JTextField requirement2Field = new JTextField(200);
		requirement2Field.setText(badge.getRequirements()[1]);
		requirementsPanel.add(requirement2Label);
		requirementsPanel.add(requirement2Field);
		
		JLabel requirement3Label = new JLabel("Requirement 3: ");
		final JTextField requirement3Field = new JTextField(200);
		requirement3Field.setText(badge.getRequirements()[2]);
		requirementsPanel.add(requirement3Label);
		requirementsPanel.add(requirement3Field);
		
		JLabel requirement4Label = new JLabel("Requirement 4: ");
		final JTextField requirement4Field = new JTextField(200);
		requirement4Field.setText(badge.getRequirements()[3]);
		requirementsPanel.add(requirement4Label);
		requirementsPanel.add(requirement4Field);
		
		JLabel requirement5Label = new JLabel("Requirement 5: ");
		final JTextField requirement5Field = new JTextField(200);
		requirement5Field.setText(badge.getRequirements()[4]);
		requirementsPanel.add(requirement5Label);
		requirementsPanel.add(requirement5Field);
		
		JLabel requirement6Label = new JLabel("Requirement 6: ");
		final JTextField requirement6Field = new JTextField(200);
		requirement6Field.setText(badge.getRequirements()[5]);
		requirementsPanel.add(requirement6Label);
		requirementsPanel.add(requirement6Field);
		
		JLabel requirement7Label = new JLabel("Requirement 7: ");
		final JTextField requirement7Field = new JTextField(200);
		requirement7Field.setText(badge.getRequirements()[6]);
		requirementsPanel.add(requirement7Label);
		requirementsPanel.add(requirement7Field);
		
		JLabel requirement8Label = new JLabel("Requirement 8: ");
		final JTextField requirement8Field = new JTextField(200);
		requirement8Field.setText(badge.getRequirements()[7]);
		requirementsPanel.add(requirement8Label);
		requirementsPanel.add(requirement8Field);
		
		JLabel requirement9Label = new JLabel("Requirement 9: ");
		final JTextField requirement9Field = new JTextField(200);
		requirement9Field.setText(badge.getRequirements()[8]);
		requirementsPanel.add(requirement9Label);
		requirementsPanel.add(requirement9Field);
		
		JLabel requirement10Label = new JLabel("Requirement 10: ");
		final JTextField requirement10Field = new JTextField(200);
		requirement10Field.setText(badge.getRequirements()[9]);
		requirementsPanel.add(requirement10Label);
		requirementsPanel.add(requirement10Field);
		
		body.add(requirementsPanel);
		pane.add(body, BorderLayout.CENTER); //Add to screen
		
		//Create a panel to hold action buttons
		JPanel buttons = new JPanel();
		buttons.setBorder(new EmptyBorder(0, 380, 0, 50));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.setBackground(Color.WHITE);
		Box buttonBox = Box.createHorizontalBox();
		
		JButton addButton = new JButton("Update badge");
		addButton.setBackground(new Color(79,129,189));
		addButton.setFont(new Font("Calibri",Font.PLAIN,20));
		addButton.setForeground(Color.WHITE);
		addButton.setFocusable(false);
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//When button is pressed validate all inputs
				if(requirement1Field.getText().isEmpty()){
					//If first requirement is empty show error and quit
					JOptionPane.showMessageDialog(frame,
							"Requirement 1 must be entered",
							"Requirement error",
							JOptionPane.ERROR_MESSAGE);
						return;
				}
				
				Validator v = new Validator(frame);
				if(v.isValidBadgeName(nameField.getText()) &&
					v.isValidRequirement(requirement1Field.getText()) &&
					v.isValidRequirement(requirement2Field.getText()) &&
					v.isValidRequirement(requirement3Field.getText()) &&
					v.isValidRequirement(requirement4Field.getText()) &&
					v.isValidRequirement(requirement5Field.getText()) &&
					v.isValidRequirement(requirement6Field.getText()) &&
					v.isValidRequirement(requirement7Field.getText()) &&
					v.isValidRequirement(requirement8Field.getText()) &&
					v.isValidRequirement(requirement9Field.getText()) &&
					v.isValidRequirement(requirement10Field.getText())){
					
					//Assign new values to badge object
					badge.setName(nameField.getText());
					badge.setRequirementsNeeded((int) spinner.getValue());
					String[] requirements = new String[10];
					requirements[0] = requirement1Field.getText();
					requirements[1] = requirement2Field.getText();
					requirements[2] = requirement3Field.getText();
					requirements[3] = requirement4Field.getText();
					requirements[4] = requirement5Field.getText();
					requirements[5] = requirement6Field.getText();
					requirements[6] = requirement7Field.getText();
					requirements[7] = requirement8Field.getText();
					requirements[8] = requirement9Field.getText();
					requirements[9] = requirement10Field.getText();
					badge.setRequirements(requirements);
					
					//All the details are valid so badge can be updated in DB
					Connection db = ScoutManager.connectToDB(); //Connect to the database
					try {
						Statement stmt = db.createStatement();
						//Update details in the DB
						String sql = "UPDATE BADGES SET 'NAME'='" + badge.getName() + "'," +
								"'REQUIREMENTS NEEDED'='" + badge.getRequirementsNeeded() +"'," +
								"'REQUIREMENT1'='" + badge.getRequirements()[0] + "'," +
								"'REQUIREMENT2'='" + badge.getRequirements()[1] + "'," +
								"'REQUIREMENT3'='" + badge.getRequirements()[2] + "'," +
								"'REQUIREMENT4'='" + badge.getRequirements()[3] + "'," +
								"'REQUIREMENT5'='" + badge.getRequirements()[4] + "'," +
								"'REQUIREMENT6'='" + badge.getRequirements()[5] + "'," +
								"'REQUIREMENT7'='" + badge.getRequirements()[6] + "'," +
								"'REQUIREMENT8'='" + badge.getRequirements()[7] + "'," +
								"'REQUIREMENT9'='" + badge.getRequirements()[8] + "'," +
								"'REQUIREMENT10'='" +badge.getRequirements()[9] + "'" +
								"WHERE NAME='" + name + "'";
						stmt.executeUpdate(sql);
						stmt.close();
						db.close();
						//Restart screen
						new UpdateBadge(frame);
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
				//Return to the Badge Management screen
				new BadgeManagement(frame);
			}			
		});
		buttonBox.add(cancelButton);
		
		buttons.add(buttonBox);
		pane.add(buttons, BorderLayout.SOUTH);
		
		
		frame.setVisible(true);
	}
}
