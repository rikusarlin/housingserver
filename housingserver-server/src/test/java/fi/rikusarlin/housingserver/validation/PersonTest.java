package fi.rikusarlin.housingserver.validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.rikusarlin.housingserver.bottomup.testdata.PersonData;
import fi.rikusarlin.housingserver.data.PersonEntity;

public class PersonTest 
{
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	Set<ConstraintViolation<PersonEntity>> violations;
	
	private List<String> getMessages(Set<ConstraintViolation<PersonEntity>> violations){
		return violations
    		.stream()
    		.map(v -> v.getPropertyPath() + ": "+ v.getMessage())
    		.collect(Collectors.toList());
	}
	
    @Test
    public void testValidPersonNumbers()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("010170+904N");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("010100A900F");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("010170-901K");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidControlChar()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setPersonNumber("010100A900G");
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(getMessages(violations).contains("personNumber: invalid person number '010100A900G'"));
    }

    @Test
    public void testInvalidControlChar2()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setPersonNumber("010170-901L");
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(getMessages(violations).contains("personNumber: invalid person number '010170-901L'"));
    }

    @Test
    public void testTooShortPersonNumber()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setPersonNumber("010100A900");
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
        List<String> messages = getMessages(violations);
        Assertions.assertTrue(messages.contains("personNumber: size must be between 11 and 11"));
        Assertions.assertTrue(messages.contains("personNumber: invalid person number '010100A900'"));
    }

    @Test
    public void testMissingPersonNumber()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setPersonNumber(null);
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        // Is both invalid and missing (several validators)        
        Assertions.assertTrue(violations.size() == 2);
        List<String> messages = getMessages(violations);
        Assertions.assertTrue(messages.contains("personNumber: must not be null"));
        Assertions.assertTrue(messages.contains("personNumber: invalid person number ''"));
    }

    @Test
    public void testMissingBirthDate()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setBirthDate(null);
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("birthDate: must not be null"));
    }
    
    @Test
    public void testMissingFirstName()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setFirstName(null);
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("firstName: must not be null"));
    }

    
    @Test
    public void testMissingLastName()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setLastName(null);
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("lastName: must not be null"));
    }

    @Test
    public void testTooLongFirstName()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setFirstName("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("firstName: size must be between 0 and 80"));
    }

    @Test
    public void testTooLongLastName()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setLastName("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("lastName: size must be between 0 and 80"));
    }

    @Test
    public void testNoGender()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setGender(null);
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testNoEmail()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setEmail(null);
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail()
    {
    	PersonEntity p1 = PersonData.getPerson1();
    	p1.setEmail("username@yahoo..com");
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("email: invalid emailAddress 'username@yahoo..com'"));
    }

    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }

}
