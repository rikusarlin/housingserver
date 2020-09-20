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

import fi.rikusarlin.housingserver.data.HouseholdMember;

public class HouseholdMemberTest 
{
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	Set<ConstraintViolation<HouseholdMember>> violations;

    @Test
    public void testValidHousehold()
    {
    	HouseholdMember hm1 = new HouseholdMember();
    	hm1.setId(1);
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm1.setPersonNumber("170871-0091");
    	violations = validator.validate(hm1, HouseholdChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    	hm1.setPersonNumber("170871+0091");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    	hm1.setPersonNumber("130570-216E");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    	hm1.setPersonNumber("020103A678R");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    	hm1.setPersonNumber("270205A515X");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testBadPerson()
    {
    	HouseholdMember hm1 = new HouseholdMember();
    	hm1.setId(1);
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm1.setPersonNumber("170871-0092");
    	violations = validator.validate(hm1, HouseholdChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
    }
    
    @Test
    public void testTooShortPersonNumber()
    {
    	HouseholdMember hm1 = new HouseholdMember();
    	hm1.setId(1);
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm1.setPersonNumber("170871-009");
    	violations = validator.validate(hm1, HouseholdChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    }
    
    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }

}
