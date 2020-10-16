package fi.rikusarlin.housingserver.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.testdata.HousingBenefitCaseData;

public class HousingBenefitCaseTest 
{
	private static boolean setUpIsDone = false;
	private static Validator validator;
	private static DateTimeFormatter formatter;
	Set<ConstraintViolation<HousingBenefitCaseEntity>> violations;

	private List<String> getMessages(Set<ConstraintViolation<HousingBenefitCaseEntity>> violations){
		return violations
    		.stream()
    		.sequential()
    		.map(v -> v.getPropertyPath() + ": "+ v.getMessage())
    		.sorted()
    		.collect(Collectors.toList());
	}

	@BeforeAll
	public static void setUp() {
	    if (setUpIsDone) {
	        return;
	    }
    	validator = Validation.buildDefaultValidatorFactory().getValidator();
    	formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
   	    setUpIsDone = true;
	}
	
	private HousingBenefitCaseEntity goodHousingBenefitCase() {
    	return MappingUtil.modelMapper.map(
    			HousingBenefitCaseData.getCase1(), HousingBenefitCaseEntity.class);
	}
	
    @Test
    public void testGoodApplication()
    {
    	HousingBenefitCaseEntity hbc = goodHousingBenefitCase();
    	violations = validator.validate(hbc);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testBadApplicaton()
    {
    	HousingBenefitCaseEntity hbc = goodHousingBenefitCase();
    	hbc.getIncomes().iterator().next().setStartDate(LocalDate.parse("01.11.2020", formatter));
    	hbc.getIncomes().iterator().next().setEndDate(LocalDate.parse("01.08.2020", formatter));
    	violations = validator.validate(hbc,ApplicationChecks.class);
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("incomes[].startEndDate: start date must be less than end date if both are given, here start date is '01.11.2020' and end date '01.08.2020'"));
    	violations = validator.validate(hbc, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
        Assertions.assertTrue(getMessages(violations).contains("incomes[].startEndDate: start date must be less than end date if both are given, here start date is '01.11.2020' and end date '01.08.2020'"));
        Assertions.assertTrue(getMessages(violations).contains("incomes[0].startEndDate: must overlap with application date range, and 01.09.2020-01.10.2020 and 01.11.2020-01.08.2020 do not overlap"));
    	violations = validator.validate(hbc, AllChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
        Assertions.assertTrue(getMessages(violations).contains("incomes[].startEndDate: start date must be less than end date if both are given, here start date is '01.11.2020' and end date '01.08.2020'"));
        Assertions.assertTrue(getMessages(violations).contains("incomes[0].startEndDate: must overlap with application date range, and 01.09.2020-01.10.2020 and 01.11.2020-01.08.2020 do not overlap"));
    }

    @Test
    public void testVeryBadApplicaton()
    {
    	HousingBenefitCaseEntity hbc = goodHousingBenefitCase();
    	hbc.getIncomes().iterator().next().setStartDate(LocalDate.parse("01.11.2020", formatter));
    	hbc.getIncomes().iterator().next().setEndDate(LocalDate.parse("01.08.2020", formatter));

    	Iterator<ExpenseEntity> i1 = hbc.getHousingExpenses().iterator();
    	i1.next();
    	ExpenseEntity e = i1.next();
    	e.setStartDate(LocalDate.parse("01.11.2019", formatter));
    	e.setEndDate(LocalDate.parse("01.08.2019", formatter));
    	
    	Iterator<HouseholdMemberEntity> i2 = hbc.getHouseholdMembers().iterator();
    	i2.next();
    	i2.next();
    	HouseholdMemberEntity hm = i2.next();
    	hm.getPerson().setPersonNumber("020202A002B");
    	
    	violations = validator.validate(hbc,AllChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 5);
        Assertions.assertTrue(getMessages(violations).contains("incomes[0].startEndDate: must overlap with application date range, and 01.09.2020-01.10.2020 and 01.11.2020-01.08.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("householdMembers[].person.personNumber: invalid person number '020202A002B'"));
        Assertions.assertTrue(getMessages(violations).contains("incomes[].startEndDate: start date must be less than end date if both are given, here start date is '01.11.2020' and end date '01.08.2020'"));
        Assertions.assertTrue(getMessages(violations).contains("housingExpenses[].startEndDate: start date must be less than end date if both are given, here start date is '01.11.2019' and end date '01.08.2019'"));
        Assertions.assertTrue(getMessages(violations).contains("housingExpenses[1].startEndDate: must overlap with application date range, and 01.09.2020-01.10.2020 and 01.11.2019-01.08.2019 do not overlap"));
     }
    
    @Test
    public void testChangeOfApplicationDateRange()
    {
    	HousingBenefitCaseEntity hbc = goodHousingBenefitCase();
    	violations = validator.validate(hbc, AllChecks.class);
        Assertions.assertTrue(violations.isEmpty());
        // This should invalidate really many sub-objects
    	hbc.getApplication().setStartDate(LocalDate.parse("01.01.2019", formatter));
    	hbc.getApplication().setEndDate(LocalDate.parse("01.06.2019", formatter));
    	violations = validator.validate(hbc, ApplicationChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    	violations = validator.validate(hbc, HouseholdChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 4);
    	violations = validator.validate(hbc, ExpenseChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    	violations = validator.validate(hbc, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    	violations = validator.validate(hbc, AllChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 8);
        List<String> messages = getMessages(violations);
        Assertions.assertTrue(messages.contains("householdMembers[0].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(messages.contains("householdMembers[1].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(messages.contains("householdMembers[2].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(messages.contains("householdMembers[3].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(messages.contains("incomes[0].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.08.2020-15.10.2020 do not overlap"));
        Assertions.assertTrue(messages.contains("incomes[1].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.08.2020-15.10.2020 do not overlap"));
        Assertions.assertTrue(messages.contains("housingExpenses[0].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(messages.contains("housingExpenses[1].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
    }
    
    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }

}
