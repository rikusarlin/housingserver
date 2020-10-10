package fi.rikusarlin.housingserver.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidPersonNumberValidator implements ConstraintValidator<ValidPersonNumber, Object> {
	
	public static final String CONTROL_CHARS = "0123456789ABCDEFHJKLMNPRSTUVWXY";
	public static final String CENTURY_CHARS = "+-A";
	private static DateTimeFormatter shortDateFormat = DateTimeFormatter.ofPattern("ddMMyy");

	@Override
	public void initialize (ValidPersonNumber constraintAnnotation) {
	}

	@Override
	public boolean isValid (Object value, ConstraintValidatorContext context) {

		if(!(value instanceof String)) {
			return false;
		}
		
		String pn = (String) value;
		
		// Person number is 11 chars in length
		if(pn.length() != 11) {
			return false;
		}
		
		String datePart = pn.substring(0, 6);
		String centuryPart = pn.substring(6,7);
		String individualNumber = pn.substring(7,10);
		String controlChar = pn.substring(10,11);
		// The first six characters are date of birth in ddMMyy format
		try {
			LocalDate.parse(datePart, shortDateFormat);
		} catch (DateTimeParseException dtpe) {
			return false;
		}
		// Seventh character tells the century, allowed are "+-A"
		if(CENTURY_CHARS.indexOf(centuryPart)<0) {
			return false;
		}
		
		// Characters 8-10 is an individual number, an integer
		try {
			Integer.parseInt(individualNumber);
		} catch (NumberFormatException nfe) {
			return false;
		}
		
		// Character 11 is a control character
		// See https://dvv.fi/en/personal-identity-code
		long numberToCheck = Long.parseLong(datePart+individualNumber);
		int remainder = (int)(numberToCheck % 31L);
		if(!controlChar.equals(CONTROL_CHARS.substring(remainder,remainder+1))){
			return false;
		}

		return true;
	}
}
