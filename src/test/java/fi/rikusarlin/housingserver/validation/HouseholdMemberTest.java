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
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.testdata.HouseholdMemberData;

public class HouseholdMemberTest 
{
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	Set<ConstraintViolation<HouseholdMemberEntity>> violations;
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


	private List<String> getMessages(Set<ConstraintViolation<HouseholdMemberEntity>> violations){
		return violations
    		.stream()
    		.map(v -> v.getPropertyPath() + ": "+ v.getMessage())
    		.collect(Collectors.toList());
	}

    @Test
    public void testValidHouseholdMember()
    {
    	HouseholdMemberEntity hm1 = MappingUtil.modelMapper.map(
    			HouseholdMemberData.getHouseholdMember1(),HouseholdMemberEntity.class);
    	violations = validator.validate(hm1, HouseholdChecks.class);
    	Assertions.assertTrue(violations.isEmpty());
    	hm1.getPerson().setPersonNumber("010170+901K");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
        hm1.getPerson().setPersonNumber("010170-902L");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
        hm1.getPerson().setPersonNumber("010100A900F");
    	violations = validator.validate(hm1);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testBadDateRange()
    {
    	HouseholdMemberEntity hm1 = MappingUtil.modelMapper.map(
    			HouseholdMemberData.getHouseholdMember2(),HouseholdMemberEntity.class);
    	hm1.setEndDate(LocalDate.parse("01.01.2019", formatter));
    	violations = validator.validate(hm1, HouseholdChecks.class);
        List<String> violationList = getMessages(violations);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(violationList.contains("startEndDate: start date must be less than end date if both are given, here start date is '01.09.2020' and end date '01.01.2019'"));
    }
    
    @Test
    public void testTooShortPersonNumber()
    {
    	HouseholdMemberEntity hm1 = MappingUtil.modelMapper.map(
    			HouseholdMemberData.getHouseholdMember1(),HouseholdMemberEntity.class);
    	hm1.getPerson().setPersonNumber("010170-901");
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
