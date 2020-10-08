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

import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;

public class HouseholdMemberTest 
{
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	Set<ConstraintViolation<HouseholdMemberEntity>> violations;

    @Test
    public void testValidHouseholdMember()
    {
    	HouseholdMemberEntity hm1 = new HouseholdMemberEntity();
    	hm1.setId(1);
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-0091");
    	p1.setFirstName("Riku");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	hm1.setPerson(p1);
    	violations = validator.validate(hm1, HouseholdChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("170871+0091");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("130570-216E");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("020103A678R");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("270205A515X");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testBadPerson()
    {
    	HouseholdMemberEntity hm1 = new HouseholdMemberEntity();
    	hm1.setId(1);
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-0092");
    	p1.setFirstName("Riku");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	hm1.setPerson(p1);
    	violations = validator.validate(hm1, HouseholdChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
    }
    
    @Test
    public void testTooShortPersonNumber()
    {
    	HouseholdMemberEntity hm1 = new HouseholdMemberEntity();
    	hm1.setId(1);
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("170871-009");
    	p1.setFirstName("Riku");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	hm1.setPerson(p1);
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
