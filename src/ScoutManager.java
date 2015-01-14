import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This is the main program for the Scout Manager project and creates the home
 * screen which is first encountered by the user when application is started
 * @author Matthew Brookes
 */
public class ScoutManager {
	/**
	 * Main routine of program which establishes a database if one does not
	 * already exist and creates the Scouts and Badges tables if they do not
	 * exist. Then draws the home screen which the user sees when the program
	 * is started.
	 * @param args
	 */
	public static void main(String[] args) {
		Connection db = connectToDB(); //Stores the connection to the database
		try { //Create the scout and badge tables if they do not exist
			Statement stmt = db.createStatement(); //Stores sql statement to be executed
			//Create scout table
			String sql = "CREATE TABLE IF NOT EXISTS SCOUTS " +
	                   "(NAME           VARCHAR(40)    NOT NULL, " + 
	                   "'EMAIL ADDRESS' VARCHAR(40)    NOT NULL, " + 
	                   "'PHONE NUMBER'  VARCHAR(11)	   NOT NULL, " + 
	                   " DOB            DATE           NOT NULL, " + 
	                   "'DATE JOINED'   DATE           NOT NULL, " +
	                   " ADDRESS1       VARCHAR(40)    NOT NULL, " +
	                   " ADDRESS2       VARCHAR(40),             " +
	                   " ADDRESS3       VARCHAR(40),             " +
	                   " ADDRESS4       VARCHAR(40),             " +
	                   " PATROL         VARCHAR(7)     NOT NULL) ";
			stmt.executeUpdate(sql);
			
			//Create badge table
			sql = "CREATE TABLE IF NOT EXISTS BADGES " +
	              "(NAME                 VARCHAR(20)    NOT NULL, " + 
	              "'REQUIREMENTS NEEDED' INTEGER        NOT NULL, " + 
	              " REQUIREMENT1         VARCHAR(200)	NOT NULL, " + 
	              " REQUIREMENT2         VARCHAR(200),	          " +
	              " REQUIREMENT3         VARCHAR(200),	          " +
	              " REQUIREMENT4         VARCHAR(200),	          " +
	              " REQUIREMENT5         VARCHAR(200),	          " +
	              " REQUIREMENT6         VARCHAR(200),	          " +
	              " REQUIREMENT7         VARCHAR(200),	          " +
	              " REQUIREMENT8         VARCHAR(200),	          " +
	              " REQUIREMENT9         VARCHAR(200),	          " +
	              " REQUIREMENT10        VARCHAR(200)) ";
			stmt.executeUpdate(sql);
			
			//Clean up connections
			stmt.close();
			db.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		final JFrame frame = new JFrame("Scout Manager"); //Root container
		frame.setBounds(0, 0, 800, 600);
		drawScreen(frame);
		frame.setVisible(true); //Show window
		
	}
	
	/**
	 * Attempts to connect to the database otherwise creates a database.
	 * If encounters a problem then quits system
	 * @return Connection The SQLite connection to the database
	 */
	static Connection connectToDB(){
		try {
		    Class.forName("org.sqlite.JDBC");
		    Connection db = DriverManager.getConnection("jdbc:sqlite:system.db");
		    System.out.println("Opened database successfully");
		    return db;
	    } 
		catch ( Exception e ) {
		    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
	    }
		return null;
	}
	
	
	/**
	 * This constructor will be called when the home button is pressed at any stage during
	 * the system. It will redraw the home screen on the existing frame.
	 * @param frame The current window when home screen pressed
	 */
	ScoutManager(JFrame frame){
		System.out.println("Home");
		Container pane = frame.getContentPane();
		pane.removeAll();
		pane.repaint();
		drawScreen(frame);
		frame.setVisible(true);
	}
	/**
	 * This method draws the header and body of the home screen
	 * on the frame which is passed
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
		
		header.setBackground(new Color(139,0,102)); //Set purple color to background
		//Create home and back buttons
		ImageIcon back = new ImageIcon(ScoutManager.class.getResource("back.png"));
		JLabel backButton = new JLabel(back);
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
		JLabel title = new JLabel("HOME");
		title.setFont(new Font("Verdana",Font.PLAIN, 50));
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
		GridLayout bodyLayout = new GridLayout(2,3);
		body.setLayout(bodyLayout);
		body.setBorder(new EmptyBorder(20,20,80,200));
		
		//Create buttons
		JButton reports = new JButton("Reports");
		reports.setBackground(new Color(79,129,189));
		reports.setFont(new Font("Calibri",Font.PLAIN,20));
		reports.setForeground(Color.WHITE);
		reports.setFocusable(false);
		
		//When button is clicked start Reports screen
		reports.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Start Reports screen
				new Reports(frame);
			}	
		});
		
		JButton bManagement = new JButton("Badge Management");
		bManagement.setFont(new Font("Calibri",Font.PLAIN,20));
		bManagement.setForeground(Color.WHITE);
		bManagement.setFocusable(false);
		bManagement.setBackground(new Color(79,129,189));
		
		//When button is clicked start Badge Management screen
		bManagement.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Start Badge Management screen
				new BadgeManagement(frame);
			}	
		});
				
		JButton sManagement = new JButton("Scout Management");
		sManagement.setBackground(new Color(79,129,189));
		sManagement.setFont(new Font("Calibri",Font.PLAIN,20));
		sManagement.setForeground(Color.WHITE);
		sManagement.setFocusable(false);
		
		//When button is clicked start Scout Management screen
		sManagement.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Start Scout Management screen
				new ScoutManagement(frame);
			}	
		});
				
		//Set padding between buttons
		bodyLayout.setHgap(50);
		bodyLayout.setVgap(100);
		
		//Add buttons to body panel
		body.add(reports);
		body.add(bManagement);
		body.add(sManagement);
		
		pane.add(body, BorderLayout.CENTER);
	}
}
