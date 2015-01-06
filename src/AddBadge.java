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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * This is the screen where the user adds a new badge
 * to the Badge table in the database by entering
 * data into a form.
 * @author Matthew Brookes 
 */
public class AddBadge {
	/**
	 * The class' only constructor has a JFrame as an argument which
	 * is passed from the home screen. On this frame the form is drawn
	 * as well as labels directing the user on what data is required.
	 * The back button will be directed towards the Badge Management screen.
	 * @param frame The screen where buttons will be drawn
	 */
	public AddBadge(JFrame frame) {
		System.out.println("Add a new badge");
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
		ImageIcon back = new ImageIcon("./res/back.png");
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
		JLabel title = new JLabel("NEW BADGE");
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
		spinnerPanel.add(requirementsLabel);
		spinnerPanel.add(spinner);
		spinnerPanel.setMaximumSize(new Dimension(800, 100));
		body.add(spinnerPanel);
		
		JPanel requirementsPanel = new JPanel();
		requirementsPanel.setLayout(new GridLayout(0, 2));
		requirementsPanel.setBackground(Color.WHITE);
		
		JLabel requirement1Label = new JLabel("Requirement 1: ");
		final JTextField requirement1Field = new JTextField(200);
		requirementsPanel.add(requirement1Label);
		requirementsPanel.add(requirement1Field);
		
		JLabel requirement2Label = new JLabel("Requirement 2: ");
		final JTextField requirement2Field = new JTextField(200);
		requirementsPanel.add(requirement2Label);
		requirementsPanel.add(requirement2Field);
		
		JLabel requirement3Label = new JLabel("Requirement 3: ");
		final JTextField requirement3Field = new JTextField(200);
		requirementsPanel.add(requirement3Label);
		requirementsPanel.add(requirement3Field);
		
		JLabel requirement4Label = new JLabel("Requirement 4: ");
		final JTextField requirement4Field = new JTextField(200);
		requirementsPanel.add(requirement4Label);
		requirementsPanel.add(requirement4Field);
		
		JLabel requirement5Label = new JLabel("Requirement 5: ");
		final JTextField requirement5Field = new JTextField(200);
		requirementsPanel.add(requirement5Label);
		requirementsPanel.add(requirement5Field);
		
		JLabel requirement6Label = new JLabel("Requirement 6: ");
		final JTextField requirement6Field = new JTextField(200);
		requirementsPanel.add(requirement6Label);
		requirementsPanel.add(requirement6Field);
		
		JLabel requirement7Label = new JLabel("Requirement 7: ");
		final JTextField requirement7Field = new JTextField(200);
		requirementsPanel.add(requirement7Label);
		requirementsPanel.add(requirement7Field);
		
		JLabel requirement8Label = new JLabel("Requirement 8: ");
		final JTextField requirement8Field = new JTextField(200);
		requirementsPanel.add(requirement8Label);
		requirementsPanel.add(requirement8Field);
		
		JLabel requirement9Label = new JLabel("Requirement 9: ");
		final JTextField requirement9Field = new JTextField(200);
		requirementsPanel.add(requirement9Label);
		requirementsPanel.add(requirement9Field);
		
		JLabel requirement10Label = new JLabel("Requirement 10: ");
		final JTextField requirement10Field = new JTextField(200);
		requirementsPanel.add(requirement10Label);
		requirementsPanel.add(requirement10Field);
		
		body.add(requirementsPanel);
		pane.add(body, BorderLayout.CENTER); //Add to screen
		
		//Create a panel to hold action buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.setBackground(Color.WHITE);
		buttons.setBorder(new EmptyBorder(0, 380, 0, 50));
		Box buttonBox = Box.createHorizontalBox();
		
		JButton addButton = new JButton("Save new badge");
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
					
					//All the details are valid so badge should be added to DB
					Connection db = ScoutManager.connectToDB(); //Connect to the database
					try {
						Statement stmt = db.createStatement();
						String sql = "SELECT * FROM BADGES WHERE NAME='" + 
								nameField.getText() + "'"; //If badge already in DB
						ResultSet rs = stmt.executeQuery(sql); //Find in DB
						while(rs.next()){
							//Will reach this point if badge already in DB
							JOptionPane.showMessageDialog(frame,
									"Badge already exists in database",
									"Database error",
									JOptionPane.ERROR_MESSAGE);
							stmt.close();
							db.close();
							return;
						}
						stmt.close();
						//Add new badge to db as it doesn't exist
						sql = "INSERT INTO BADGES " +
								"VALUES ('" + nameField.getText() + "','" + 
								spinner.getValue()+"','" +
								requirement1Field.getText()+"','" + 
								requirement2Field.getText() + "','" +
								requirement3Field.getText()+"','" + 
								requirement4Field.getText() + "','" +
								requirement5Field.getText()+"','" + 
								requirement6Field.getText() + "','" +
								requirement7Field.getText()+"', '"+ 
								requirement8Field.getText()+"', '" +
								requirement9Field.getText()+"', '"+ 
								requirement10Field.getText()+"')";
						stmt.executeUpdate(sql);
						stmt.close();
						db.close();
						//Restart screen
						new AddBadge(frame);
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
	}
}
