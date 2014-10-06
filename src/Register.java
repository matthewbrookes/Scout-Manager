import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * This is the screen which the user visits to generate a register from 
 * the database. It will be made up of a series of panels displaying the names
 * of all the scouts under a heading for the relevant patrol.
 * @author Matthew Brookes
 */
public class Register {
	/**
	 * The class' only constructor has a JFrame as an argument which
	 * is passed from the home screen. On this frame the five buttons
	 * are drawn to replace those from the home screen and the back
	 * button is linked to the home screen.
	 * @param frame The screen where buttons will be drawn
	 */
	public Register(JFrame frame) {
		System.out.println("Reports");
		Container pane = frame.getContentPane();
		pane.removeAll();
		pane.repaint();
		drawScreen(frame);
		frame.setVisible(true);
	}
	/**
	 * This method draws the header and body of the report screen
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
		JLabel title = new JLabel("REGISTER");
		title.setFont(new Font("Verdana",Font.PLAIN, 50));
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
		final JPanel body = new JPanel();
		final JScrollPane scrollBody = new JScrollPane(body);
		body.setBackground(Color.WHITE);
		body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
		body.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50));
		
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
		
		//Arrays to store scouts in patrols
		ArrayList<String> bulldogs = new ArrayList<String>();
		ArrayList<String> panthers = new ArrayList<String>();
		ArrayList<String> wolves = new ArrayList<String>();
		ArrayList<String> lions = new ArrayList<String>();
		ArrayList<String> cobras = new ArrayList<String>();
		ArrayList<String> tigers = new ArrayList<String>();
		
		db = ScoutManager.connectToDB();
		for(int i=0; i<namesInDb.size(); i++){
			try {
				//Retrieve patrol for scout from db
				Statement stmt = db.createStatement();
				String name = namesInDb.get(i);
				String sql = "SELECT PATROL FROM SCOUTS WHERE " +
						"NAME='" + name + "'";
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					//Add scout name to the relevant array
					String patrol = rs.getString(1);
					if(patrol.equals("Bulldog")){
						bulldogs.add(name);
					}
					else if(patrol.equals("Wolf")){
						wolves.add(name);
					}
					else if(patrol.equals("Cobra")){
						cobras.add(name);
					}
					else if(patrol.equals("Lion")){
						lions.add(name);
					}
					else if(patrol.equals("Tiger")){
						tigers.add(name);
					}
					else if(patrol.equals("Panther")){
						panthers.add(name);
					}
				}
				//Close connections
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		//Clean up connection to db
		try {
			db.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		//Create panel for each patrol to hold names
		JPanel bulldogPanel = new JPanel();
		bulldogPanel.setLayout(new BoxLayout(bulldogPanel, BoxLayout.Y_AXIS));
		bulldogPanel.setBackground(Color.WHITE);
		
		JPanel lionPanel = new JPanel();
		lionPanel.setLayout(new BoxLayout(lionPanel, BoxLayout.Y_AXIS));
		lionPanel.setBackground(Color.WHITE);
		
		JPanel tigerPanel = new JPanel();
		tigerPanel.setLayout(new BoxLayout(tigerPanel, BoxLayout.Y_AXIS));
		tigerPanel.setBackground(Color.WHITE);
		
		JPanel wolfPanel = new JPanel();
		wolfPanel.setLayout(new BoxLayout(wolfPanel, BoxLayout.Y_AXIS));
		wolfPanel.setBackground(Color.WHITE);
		
		JPanel cobraPanel = new JPanel();
		cobraPanel.setLayout(new BoxLayout(cobraPanel, BoxLayout.Y_AXIS));
		cobraPanel.setBackground(Color.WHITE);
		
		JPanel pantherPanel = new JPanel();
		pantherPanel.setLayout(new BoxLayout(pantherPanel, BoxLayout.Y_AXIS));
		pantherPanel.setBackground(Color.WHITE);
		
		//Create header for each panel and add to panel	
		JLabel bulldogHeader = new JLabel("Bulldog");
		bulldogHeader.setFont(new Font("Dialog", Font.BOLD, 18));
		bulldogPanel.add(bulldogHeader);
		
		JLabel lionHeader = new JLabel("Lion");
		lionHeader.setFont(new Font("Dialog", Font.BOLD, 18));
		lionPanel.add(lionHeader);
		
		JLabel tigerHeader = new JLabel("Tiger");
		tigerHeader.setFont(new Font("Dialog", Font.BOLD, 18));
		tigerPanel.add(tigerHeader);
		
		JLabel wolfHeader = new JLabel("Wolf");
		wolfHeader.setFont(new Font("Dialog", Font.BOLD, 18));
		wolfPanel.add(wolfHeader);
		
		JLabel cobraHeader = new JLabel("Cobra");
		cobraHeader.setFont(new Font("Dialog", Font.BOLD, 18));
		cobraPanel.add(cobraHeader);
		
		JLabel pantherHeader = new JLabel("Panther");
		pantherHeader.setFont(new Font("Dialog", Font.BOLD, 18));
		pantherPanel.add(pantherHeader);
		
		//Add wolves to panel
		for(String name: wolves){
			JLabel label = new JLabel(name);
			wolfPanel.add(label);
		}
		
		//Add lions to panel
		for(String name: lions){
			JLabel label = new JLabel(name);
			lionPanel.add(label);
		}
		
		//Add tigers to panel
		for(String name: tigers){
			JLabel label = new JLabel(name);
			tigerPanel.add(label);
		}
		
		//Add bulldogs to panel
		for(String name: bulldogs){
			JLabel label = new JLabel(name);
			bulldogPanel.add(label);
		}
		
		//Add cobras to panel
		for(String name: cobras){
			JLabel label = new JLabel(name);
			cobraPanel.add(label);
		}
		
		//Add panthers to panel
		for(String name: panthers){
			JLabel label = new JLabel(name);
			pantherPanel.add(label);
		}
		
	
		//Create panels to display the names in columns
		JPanel pane1 = new JPanel();
		pane1.setLayout(new GridLayout(1,2));
		pane1.add(bulldogPanel);
		pane1.add(wolfPanel);
		
		JPanel pane2 = new JPanel();
		pane2.setLayout(new GridLayout(1,2));
		pane2.add(lionPanel);
		pane2.add(cobraPanel);
		
		JPanel pane3 = new JPanel();
		pane3.setLayout(new GridLayout(1,2));
		pane3.add(tigerPanel);
		pane3.add(pantherPanel);
		
		//Add panels to the body
		body.add(pane1);
		body.add(pane2);
		body.add(pane3);
		
		//Add body to main screen
		pane.add(scrollBody, BorderLayout.CENTER);
		
		//Create a panel to hold the print button
		JPanel printPanel = new JPanel();
		printPanel.setBackground(Color.WHITE);
		printPanel.setBorder(new EmptyBorder(0, 600, 0, 0));
		
		//Create a print icon
		ImageIcon print = new ImageIcon("./res/print.png");
		JLabel printButton = new JLabel(print);
		//If button pressed then print page
		printButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				print(body);	
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
	public static void print(final Component comp){
	    final float SCALE = 0.5f; //Scale width to fit A4
	    //Create a new print job
	    PrinterJob job = PrinterJob.getPrinterJob();
	    job.setPrintable(new Printable() {
	        public int print(Graphics g, PageFormat pf, int page)
	            throws PrinterException
	        {
	            //If unprintable, exit early
	        	if (page * pf.getImageableHeight() >= SCALE * comp.getHeight())
	                return NO_SUCH_PAGE;
	        	//Create a printable job
	            ((Graphics2D)g).translate(pf.getImageableX(), pf.getImageableY()
	               - page * pf.getImageableHeight());
	            ((Graphics2D)g).scale(SCALE, SCALE);
	            comp.printAll(g);
	            return PAGE_EXISTS;
	        }
	    });
	    if (job.printDialog())
	        try { job.print(); }
	        catch (PrinterException ex) {}
	}
}
