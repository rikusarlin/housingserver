package fi.rikusarlin.housingserver.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;

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

public class HousingBenefitApplicationTest 
{
	private static boolean setUpIsDone = false;
	private static Validator validator;
	private static DateTimeFormatter formatter;
	private static DateTimeFormatter timestampFormatter;
	Set<ConstraintViolation<HousingBenefitCaseEntity>> violations;

	@BeforeAll
	public static void setUp() {
	    if (setUpIsDone) {
	        return;
	    }
    	validator = Validation.buildDefaultValidatorFactory().getValidator();
    	formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    	timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
    	p1.setPersonNumber("170871-0091");
    	p1.setFirstName("Riku");
    	p1.setLastName("Sarlin");
    	p1.setBirthDate(LocalDate.parse("17.08.1971", formatter));
    	hm1.setPerson(p1);
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	HouseholdMemberEntity hm2 = new HouseholdMemberEntity();
    	hm2.setId(2);
    	PersonEntity p2 = new PersonEntity();
    	p2.setId(1);
    	p2.setPersonNumber("130570-216E");
    	p2.setFirstName("Helena");
    	p2.setLastName("Sarlin");
    	p2.setBirthDate(LocalDate.parse("13.05.1970", formatter));
    	hm2.setPerson(p2);
    	hm2.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm2.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	HouseholdMemberEntity hm3 = new HouseholdMemberEntity();
    	hm3.setId(3);
    	PersonEntity p3 = new PersonEntity();
    	p3.setId(1);
    	p3.setPersonNumber("020103A678R");
    	p3.setFirstName("Elsa");
    	p3.setLastName("Sarlin");
    	p3.setBirthDate(LocalDate.parse("02.01.2003", formatter));
    	hm3.setPerson(p3);
    	hm3.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm3.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	HouseholdMemberEntity hm4 = new HouseholdMemberEntity();
    	hm4.setId(4);
    	PersonEntity p4 = new PersonEntity();
    	p4.setId(1);
    	p4.setPersonNumber("270205A515X");
    	p4.setBirthDate(LocalDate.parse("27.02.2005", formatter));
    	p4.setFirstName("Elsa");
    	p4.setLastName("Sarlin");
    	hm4.setPerson(p4);
    	hm4.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm4.setEndDate(LocalDate.parse("01.10.2020", formatter));

    	HousingBenefitApplicationEntity hba = new HousingBenefitApplicationEntity();
    	hba.setId(1);
    	hba.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hba.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hba.setApplicant(p1);
    	hba.setReceived(LocalDate.parse("2020-10-01 09:02:03", timestampFormatter));
    	
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
    	violations = validator.validate(hbc, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    	violations = validator.validate(hbc, AllChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
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
        /* TODO fix later
        Assertions.assertTrue(violations.size() == 7);
    	violations = validator.validate(hbc, ExpenseChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    	violations = validator.validate(hbc, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    	violations = validator.validate(hbc, AllChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 8);
        */
    }
    
    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }

}
