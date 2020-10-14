package fi.rikusarlin.housingserver.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	private List<String> getMessages(Set<ConstraintViolation<HouseholdMemberEntity>> violations){
		return violations
    		.stream()
    		.map(v -> v.getPropertyPath() + ": "+ v.getMessage())
    		.collect(Collectors.toList());
	}

    @Test
    public void testValidHouseholdMember()
    {
    	HouseholdMemberEntity hm1 = new HouseholdMemberEntity();
    	hm1.setId(1);
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("010170-901K");
    	p1.setFirstName("Rauli");
    	p1.setLastName("Wnape");
    	p1.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	hm1.setPerson(p1);
    	violations = validator.validate(hm1, HouseholdChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("010170+901K");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("010170-902L");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    	p1.setPersonNumber("010100A900F");
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
    	p1.setPersonNumber("010170-901L");
    	p1.setFirstName("Rauli");
    	p1.setLastName("Wnape");
    	p1.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	hm1.setPerson(p1);
    	violations = validator.validate(hm1, HouseholdChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(getMessages(violations).contains("person.personNumber: invalid person number '010170-901L'"));
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
    	p1.setPersonNumber("010170-901");
    	p1.setFirstName("Rauli");
    	p1.setLastName("Wnape");
    	p1.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	hm1.setPerson(p1);
    	violations = validator.validate(hm1, HouseholdChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
        Assertions.assertTrue(getMessages(violations).contains("person.personNumber: invalid person number '010170-901'"));
        Assertions.assertTrue(getMessages(violations).contains("person.personNumber: size must be between 11 and 11"));
    }

    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }

}
