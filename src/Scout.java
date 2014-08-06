import java.text.DateFormat;

/**
 * This class represents a record in the Scouts table from the database
 * @author Matthew Brookes
 */
public class Scout {
	//Declare variables which will be used in class
	private String name;
	private String email;
	private String phone;
	private DateFormat dob;
	private DateFormat dateJoined;
	private String[] address = new String[4];
	private String patrol;
	
	/**
	 * Empty constructor as the variables will all be assigned 
	 * using relevant setters
	 */
	public Scout() {}
	
	/**
	 * Sets the full name of the scout
	 * @param name The name to set
	 */
	public void setName(String name){
		this.name = name; 
	}
	/**
	 * Returns the full name of the scout
	 * @return String The full name of the scout
	 */
	public String getName(){
		return this.name;
	}

	
	/**
	 * Returns the email address of the scout
	 * @return String Email Address
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Sets the email address of the scout
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	
	/**
	 * Returns the phone number of the scout
	 * @return String Phone Number
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * Sets the Phone Number of the scout
	 * @param phone The Phone Number to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	/**
	 * Returns the DOB of the scout
	 * @return DateFormat The Date of Birth
	 */
	public DateFormat getDob() {
		return dob;
	}
	/**
	 * Sets the DOB of the scout
	 * @param dob The Date of Birth to set
	 */
	public void setDob(DateFormat dob) {
		this.dob = dob;
	}

	
	/**
	 * Returns the date the scout joined
	 * @return String The date joined
	 */
	public DateFormat getDateJoined() {
		return dateJoined;
	}
	/**
	 * Sets the date the scout joined
	 * @param dateJoined The date the scout joined
	 */
	public void setDateJoined(DateFormat dateJoined) {
		this.dateJoined = dateJoined;
	}

	
	/**
	 * Returns the address of the scout as an array of string objects
	 * @return String[] The address
	 */
	public String[] getAddress() {
		return address;
	}
	/**
	 * Sets the address of the scout as an array with four lines
	 * @param address The address to set
	 */
	public void setAddress(String[] address) {
		this.address = address;
	}

	
	/**
	 * Returns the patrol of the scout
	 * @return String The patrol
	 */
	public String getPatrol() {
		return patrol;
	}
	/**
	 * Sets the patrol of the scout
	 * @param patrol The patrol to set
	 */
	public void setPatrol(String patrol) {
		this.patrol = patrol;
	}

}
