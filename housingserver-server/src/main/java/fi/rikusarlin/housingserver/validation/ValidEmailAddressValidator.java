package fi.rikusarlin.housingserver.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidEmailAddressValidator implements ConstraintValidator<ValidEmailAddress, Object> {
	
	// Thanks to https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
	static final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
	 
	static Pattern pattern = Pattern.compile(regex);

	@Override
	public void initialize (ValidEmailAddress constraintAnnotation) {
	}

	@Override
	public boolean isValid (Object value, ConstraintValidatorContext context) {

		// Empty email is allowed... mark with @NotNull validator is that's not ok 
		if(value==null) {
			return true;
		}

		if(!(value instanceof String)) {
			return false;
		}

		if(value=="") {
			return true;
		}

		String email = (String) value;
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
}
