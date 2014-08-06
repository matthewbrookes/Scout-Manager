import java.sql.*;

/**
 * This is the main program for the Scout Manager project and creates the home
 * screen which is first encountered by the user when application is started
 * @author Matthew Brookes
 */
public class ScoutManager {
	/**
	 * Main routine of program
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
		
		
	}
	
	/**
	 * Attempts to connect to the database otherwise creates a database.
	 * If encounters a problem then quits system
	 * @return Connection The SQLite connection to the database
	 */
	private static Connection connectToDB(){
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
}
