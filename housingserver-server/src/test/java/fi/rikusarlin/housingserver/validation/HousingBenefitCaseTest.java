package fi.rikusarlin.housingserver.validation;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.model.CaseState;
import fi.rikusarlin.housingserver.model.ExpenseType;
import fi.rikusarlin.housingserver.model.IncomeType;

public class HousingBenefitCaseTest 
{
	private static boolean setUpIsDone = false;
	private static Validator validator;
	private static DateTimeFormatter formatter;
	Set<ConstraintViolation<HousingBenefitCaseEntity>> violations;

	private List<String> getMessages(Set<ConstraintViolation<HousingBenefitCaseEntity>> violations){
		return violations
    		.stream()
    		.map(v -> v.getPropertyPath() + ": "+ v.getMessage())
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
    	ExpenseEntity expense1 = new ExpenseEntity();
    	expense1.setId(1);
    	expense1.setExpenseType(ExpenseType.RENT);
    	expense1.setAmount(1520.25);
    	expense1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	expense1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	ExpenseEntity expense2 = new ExpenseEntity();
    	expense2.setId(2);
    	expense2.setExpenseType(ExpenseType.ELECTRICITY);
    	expense2.setAmount(120.5);
    	expense2.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	expense2.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	
    	IncomeEntity income1 = new IncomeEntity();
    	income1.setId(1);
    	income1.setIncomeType(IncomeType.SALARY);
    	income1.setAmount(3400.20);
    	income1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	income1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	IncomeEntity income2 = new IncomeEntity();
    	income2.setId(2);
    	income2.setIncomeType(IncomeType.DIVIDEND);
    	income2.setAmount(120.47);
    	income2.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	income2.setEndDate(LocalDate.parse("01.10.2020", formatter));

    	HouseholdMemberEntity hm1 = new HouseholdMemberEntity();
    	hm1.setId(1);
    	PersonEntity p1 = new PersonEntity();
    	p1.setId(1);
    	p1.setPersonNumber("010170-901K");
    	p1.setFirstName("Rauli");
    	p1.setLastName("Wnape");
    	p1.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	hm1.setPerson(p1);
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	HouseholdMemberEntity hm2 = new HouseholdMemberEntity();
    	hm2.setId(2);
    	PersonEntity p2 = new PersonEntity();
    	p2.setId(2);
    	p2.setPersonNumber("010170-902L");
    	p2.setFirstName("Marke");
    	p2.setLastName("Peerakpe");
    	p2.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	hm2.setPerson(p2);
    	hm2.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm2.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	HouseholdMemberEntity hm3 = new HouseholdMemberEntity();
    	hm3.setId(3);
    	PersonEntity p3 = new PersonEntity();
    	p3.setId(3);
    	p3.setPersonNumber("010170-903M");
    	p3.setFirstName("Walter");
    	p3.setLastName("Nutbekk");
    	p3.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	hm3.setPerson(p3);
    	hm3.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm3.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	HouseholdMemberEntity hm4 = new HouseholdMemberEntity();
    	hm4.setId(4);
    	PersonEntity p4 = new PersonEntity();
    	p4.setId(4);
    	p4.setPersonNumber("010170-904N");
    	p4.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	p4.setFirstName("Suvi-Tuulia");
    	p4.setLastName("Retsetenpe");
    	hm4.setPerson(p4);
    	hm4.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm4.setEndDate(LocalDate.parse("01.10.2020", formatter));

    	HousingBenefitApplicationEntity hba = new HousingBenefitApplicationEntity();
    	hba.setId(1);
    	hba.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hba.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hba.setApplicant(p1);
    	hba.setReceived(OffsetDateTime.parse("2020-10-12T07:29:44.907Z"));
    	
    	HousingBenefitCaseEntity hbc = new HousingBenefitCaseEntity();
    	hbc.setCaseState(CaseState.ACTIVE);
    	hbc.setApplication(hba);
    	hbc.setCustomer(p1);
    	
    	hbc.getHousingExpenses().add(expense1);
    	hbc.getHousingExpenses().add(expense2);
    	hbc.getIncomes().add(income1);
    	hbc.getIncomes().add(income2);
    	hbc.getHouseholdMembers().add(hm1);
    	hbc.getHouseholdMembers().add(hm2);
    	hbc.getHouseholdMembers().add(hm3);
    	hbc.getHouseholdMembers().add(hm4);

    	return hbc;
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
        Assertions.assertTrue(getMessages(violations).contains("incomes[]: start date must be less than end date if both are given, here start date is '2020-11-01' and end date '2020-08-01'"));
    	violations = validator.validate(hbc, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
        Assertions.assertTrue(getMessages(violations).contains("incomes[0].startEndDate: must overlap with application date range, and 01.09.2020-01.10.2020 and 01.11.2020-01.08.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("incomes[]: start date must be less than end date if both are given, here start date is '2020-11-01' and end date '2020-08-01'"));
    	violations = validator.validate(hbc, AllChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
        Assertions.assertTrue(getMessages(violations).contains("incomes[0].startEndDate: must overlap with application date range, and 01.09.2020-01.10.2020 and 01.11.2020-01.08.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("incomes[]: start date must be less than end date if both are given, here start date is '2020-11-01' and end date '2020-08-01'"));
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
        Assertions.assertTrue(getMessages(violations).contains("incomes[]: start date must be less than end date if both are given, here start date is '2020-11-01' and end date '2020-08-01'"));
        Assertions.assertTrue(getMessages(violations).contains("housingExpenses[]: start date must be less than end date if both are given, here start date is '2019-11-01' and end date '2019-08-01'"));
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
        Assertions.assertTrue(getMessages(violations).contains("householdMembers[2].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("housingExpenses[0].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("householdMembers[3].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("householdMembers[1].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("incomes[0].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("incomes[1].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("householdMembers[0].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
        Assertions.assertTrue(getMessages(violations).contains("housingExpenses[1].startEndDate: must overlap with application date range, and 01.01.2019-01.06.2019 and 01.09.2020-01.10.2020 do not overlap"));
    }
    
    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }

}
