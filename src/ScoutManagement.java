import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * This is the screen which the user visits to select a scout management
 * option. It will be made up of a header which is standard to each screen 
 * and three buttons linking to the relevant sections.
 * @author Matthew Brookes
 */
public class ScoutManagement {

	/**
	 * The class' only constructor has a JFrame as an argument which
	 * is passed from the home screen. On this frame the three buttons
	 * are drawn to replace those from the home screen and the back
	 * button is linked to the home screen.
	 * @param frame The screen where buttons will be drawn
	 */
	public ScoutManagement(JFrame frame) {
		System.out.println("Scout Management");
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
		
		header.setBackground(new Color(139,0,102)); //Set purple color to background
		//Create home and back buttons
		ImageIcon back = new ImageIcon("./res/back.png");
		JLabel backButton = new JLabel(back);
		backButton.addMouseListener(new MouseListener(){
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
		JLabel title = new JLabel("<html><center>SCOUT MANAGEMENT</center></html>");
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
		GridLayout bodyLayout = new GridLayout(2,3);
		body.setLayout(bodyLayout);
		body.setBorder(new EmptyBorder(20,20,80,200));
		
		//Create buttons
		JButton addScout = new JButton("Add a new scout");
		addScout.setBackground(new Color(79,129,189));
		addScout.setFont(new Font("Calibri",Font.PLAIN,20));
		addScout.setForeground(Color.WHITE);
		addScout.setFocusable(false);
		
		//When button is clicked start Add scout screen
		addScout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddScout(frame);
			}	
		});
		
		JButton updateScout = new JButton("Update a scout");
		updateScout.setFont(new Font("Calibri",Font.PLAIN,20));
		updateScout.setForeground(Color.WHITE);
		updateScout.setFocusable(false);
		updateScout.setBackground(new Color(79,129,189));
		
		//When button is clicked start Update scout screen
		updateScout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Start Update scout screen
			}	
		});
				
		JButton deleteScout = new JButton("Delete a scout");
		deleteScout.setBackground(new Color(79,129,189));
		deleteScout.setFont(new Font("Calibri",Font.PLAIN,20));
		deleteScout.setForeground(Color.WHITE);
		deleteScout.setFocusable(false);
		
		//When button is clicked start Delete scout screen
		deleteScout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Start Delete scout screen
			}	
		});
				
		//Set padding between buttons
		bodyLayout.setHgap(50);
		bodyLayout.setVgap(100);
		
		//Add buttons to body panel
		body.add(addScout);
		body.add(updateScout);
		body.add(deleteScout);
		
		pane.add(body, BorderLayout.CENTER);
	}

}
