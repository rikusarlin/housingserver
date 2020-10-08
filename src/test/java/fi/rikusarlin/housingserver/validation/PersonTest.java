package fi.rikusarlin.housingserver.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.rikusarlin.housingserver.data.PersonEntity;

public class PersonTest 
{
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	Set<ConstraintViolation<PersonEntity>> violations;

    @Test
    public void testValidPersonNumbers()
    {
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-0091");
    	p1.setFirstName("Riku");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("170871+0091");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("130570-216E");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("020103A678R");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("270205A515X");
    	violations = validator.validate(p1);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidControlChar()
    {
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-0092");
    	p1.setFirstName("Riku");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
    }
    
    @Test
    public void testTooShortPersonNumber()
    {
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-009");
    	p1.setFirstName("Riku");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    }

    @Test
    public void testMissingPersonNumber()
    {
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setFirstName("Riku");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        // Is both invalid and missing (several validators)        
        Assertions.assertTrue(violations.size() == 2);
    }

    @Test
    public void testMissingBirthDate()
    {
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-0091");
    	p1.setFirstName("Riku");
    	p1.setLastName("Sarlin");
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
    }
    
    @Test
    public void testMissingFirstName()
    {
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-0091");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
    }

    
    @Test
    public void testMissingLastName()
    {
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-0091");
    	p1.setFirstName("Riku");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
    }

    @Test
    public void testTooLongFirstName()
    {
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-0091");
    	p1.setFirstName("Riku");
    	p1.setLastName("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
    }

    @Test
    public void testTooLongLastName()
    {
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-0091");
    	p1.setFirstName("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	violations = validator.validate(p1, InputChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
    }

    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }

}
