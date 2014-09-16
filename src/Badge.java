/**
 * This class represents a record in the Badges table from the database
 * @author Matthew Brookes
 */
public class Badge {
	//Declare variables which will be used in class
	private String name;
	private int requirementsNeeded;
	private String[] requirements = new String[10];
	/**
	 * Empty constructor as the variables will all be assigned 
	 * using relevant setters
	 */
	public Badge() {}
	
	
	/**
	 * Returns the name of the badge
	 * @return String The badge name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set the name of the badge
	 * @param name The badge name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Returns the number of requirements needed to achieve badge
	 * @return int The number of requirements needed to achieve badge
	 */
	public int getRequirementsNeeded() {
		return requirementsNeeded;
	}
	/**
	 * Sets the number of requirements needed to achieve badge
	 * @param requirementsNeeded The number of requirements needed to achieve badge
	 */
	public void setRequirementsNeeded(int requirementsNeeded) {
		this.requirementsNeeded = requirementsNeeded;
	}
	
	
	/**
	 * Returns the requirements of the badge as an array of strings
	 * @return String[] Requirements
	 */
	public String[] getRequirements() {
		return requirements;
	}
	/**
	 * Sets the requirements of the badge as an array of strings
	 * @param requirements The requirements to set
	 */
	public void setRequirements(String[] requirements) {
		this.requirements = requirements;
	}

}
