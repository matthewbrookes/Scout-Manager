import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class contains methods for validating user input
 * from forms throughout the system. Methods return a boolean
 * value depending on the validity of the input.
 * @author Matthew Brookes 
 */
public class Validator {
	private JFrame frame;
	/**
	 * The constructor requires a JFrame as an input so error
	 * messages can be drawn directly to make them relevant to
	 * a specific error
	 * @param frame The current window
	 */
	public Validator(JFrame frame){
		this.frame = frame;
	}
	/**
	 * This method returns true if the name passed is valid
	 * i.e. contains only alpha characters and is less than 41
	 * characters.
	 * @param name The full name of the scout
	 * @return boolean True if name is valid else returns false
	 */
	public boolean isValidName(String name){
		for(char c: name.toCharArray()){
			if(!Character.isAlphabetic(c) && !Character.isWhitespace(c)){
				JOptionPane.showMessageDialog(frame,
						"The name must contain only letters and spaces",
						"Name error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		if(name.length() > 40 || name.length() < 1){
		JOptionPane.showMessageDialog(frame,
				"The name must contain 1-40 characters",
				"Name error",
				JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * This method returns true if the email address is valid
	 * i.e. follows the pattern abc@def.ghi and is less than
	 * 40 characters.
	 * @param email The contact email address of the scout
	 * @return boolean True if email address is valid else returns false
	 */
	public boolean isValidEmail(String email){
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
				"@((\\[[0-9]{1,3}\\.[0-9]{1,3}" +
				"\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|" +
				"(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.
											compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		if(!m.matches()){
			JOptionPane.showMessageDialog(frame,
					"The email address is not valid",
					"Email error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * This method returns true if the phone number is valid i.e. 
	 * is a sequence of 11 digits.
	 * @param phone The contact phone number of the scout
	 * @return boolean True if phone number is valid else returns false
	 */
	public boolean isValidPhone(String phone){
		if(phone.length() != 11){
			JOptionPane.showMessageDialog(frame,
					"The phone number must be 11 digits",
					"Phone error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		for(char c: phone.toCharArray()){
			if(!Character.isDigit(c)){
				JOptionPane.showMessageDialog(frame,
						"The phone number must contain only digits",
						"Phone error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method returns true if the date passed is valid
	 * i.e. it follows the format DD/MM/YYYY
	 * @param date The date to be validated
	 * @return boolean True if date is valid else returns false
	 */
	public boolean isValidDate(String date){
		String pattern = "^(0?[1-9]|[12][0-9]|3[01])[\\/\\-]" +
				"(0?[1-9]|1[012])[\\/\\-]\\d{4}$";
		if(!date.matches(pattern)){
			JOptionPane.showMessageDialog(frame,
					"Dates must be in the format DD/MM/YYYY",
					"Date error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * This method returns true if the address line is valid
	 * i.e. it contains less than 41 characters and is alphanumeric
	 * @param addr The line to be validated
	 * @return boolean True if the line is valid else returns false
	 */
	public boolean isValidAddress(String addr){
		if(addr.length() > 40){
			JOptionPane.showMessageDialog(frame,
					"Address lines must be less than 41 characters",
					"Address error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		for(char c: addr.toCharArray()){
			if(!Character.isAlphabetic(c) && !Character.isWhitespace(c)
					&& !Character.isDigit(c)){
				JOptionPane.showMessageDialog(frame,
						"The address line must contain only letters, " +
						"numbers or spaces",
						"Name error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method returns true if the address line is valid
	 * i.e. it must be filled and satisfy the validation requirements
	 * for a normal address
	 * @param addr The line to be validated
	 * @return boolean True if the line is valid else returns false
	 */
	public boolean isValidCompulsoryAddress(String addr){
		if(!isValidAddress(addr)){
			return false;
		}
		if(addr.length()<1){
			JOptionPane.showMessageDialog(frame,
					"Address Line 1 must be filled",
					"Address error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * This method returns true if the badge name passed is valid
	 * i.e. contains only alpha characters and is less than 21
	 * characters.
	 * @param name The name of the badge
	 * @return boolean True if name is valid else returns false
	 */
	public boolean isValidBadgeName(String name){
		for(char c: name.toCharArray()){
			if(!Character.isAlphabetic(c) && !Character.isWhitespace(c)){
				JOptionPane.showMessageDialog(frame,
						"The badge name must contain only letters " +
						"and spaces",
						"Name error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		if(name.length() > 20 || name.length() < 1){
		JOptionPane.showMessageDialog(frame,
				"The badge name must contain 1-20 characters",
				"Name error",
				JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * This method returns true if the requirement is valid
	 * i.e. it contains less than 201 characters and is alphanumeric
	 * @param requirement The requirement to be validated
	 * @return boolean True if the requirement is valid else returns false
	 */
	public boolean isValidRequirement(String requirement){
		if(requirement.length() > 200){
			JOptionPane.showMessageDialog(frame,
					"Requirement must be less than 201 characters",
					"Requirement error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		for(char c: requirement.toCharArray()){
			if(!Character.isAlphabetic(c) && !Character.isWhitespace(c)
					&& !Character.isDigit(c)){
				JOptionPane.showMessageDialog(frame,
						"The requirement field must contain only " +
						"letters, numbers or spaces",
						"Name error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
}
