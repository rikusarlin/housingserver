package fi.rikusarlin.housingserver.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fi.rikusarlin.housingserver.data.Expense;
import fi.rikusarlin.housingserver.data.ExpenseType;
import fi.rikusarlin.housingserver.data.HouseholdMember;
import fi.rikusarlin.housingserver.data.HousingBenefitApplication;
import fi.rikusarlin.housingserver.data.Income;
import fi.rikusarlin.housingserver.data.IncomeType;

public class HousingBenefitApplicationTest 
{
	private static boolean setUpIsDone = false;
	private static Validator validator;
	private static DateTimeFormatter formatter;
	Set<ConstraintViolation<HousingBenefitApplication>> violations;

	@BeforeAll
	public static void setUp() {
	    if (setUpIsDone) {
	        return;
	    }
    	validator = Validation.buildDefaultValidatorFactory().getValidator();
    	formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
   	    setUpIsDone = true;
	}
	
	private HousingBenefitApplication goodHuosingBenefitApplication() {
    	Expense expense1 = new Expense();
    	expense1.setId(1);
    	expense1.setExpenseType(ExpenseType.RENT);
    	expense1.setAmount(1520.25);
    	expense1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	expense1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	Expense expense2 = new Expense();
    	expense2.setId(2);
    	expense2.setExpenseType(ExpenseType.ELECTRICITY);
    	expense2.setAmount(120.5);
    	expense2.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	expense2.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	
    	Income income1 = new Income();
    	income1.setId(1);
    	income1.setIncomeType(IncomeType.SALARY);
    	income1.setAmount(3400.20);
    	income1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	income1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	Income income2 = new Income();
    	income2.setId(2);
    	income2.setIncomeType(IncomeType.DIVIDEND);
    	income2.setAmount(120.47);
    	income2.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	income2.setEndDate(LocalDate.parse("01.10.2020", formatter));

    	HouseholdMember hm1 = new HouseholdMember();
    	hm1.setId(1);
    	hm1.setPersonNumber("170871-0091");
    	hm1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	HouseholdMember hm2 = new HouseholdMember();
    	hm2.setId(2);
    	hm2.setPersonNumber("130570-216E");
    	hm2.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm2.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	HouseholdMember hm3 = new HouseholdMember();
    	hm3.setId(3);
    	hm3.setPersonNumber("020103A678R");
    	hm3.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm3.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	HouseholdMember hm4 = new HouseholdMember();
    	hm4.setId(4);
    	hm4.setPersonNumber("270205A515X");
    	hm4.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm4.setEndDate(LocalDate.parse("01.10.2020", formatter));

    	HousingBenefitApplication hba = new HousingBenefitApplication();
    	hba.setId(1);
    	hba.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hba.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hba.addExpense(expense1);
    	hba.addExpense(expense2);
    	hba.addIncome(income1);
    	hba.addIncome(income2);
    	hba.addHouseholdMember(hm1);
    	hba.addHouseholdMember(hm2);
    	hba.addHouseholdMember(hm3);
    	hba.addHouseholdMember(hm4);

    	return hba;
	}
	
    @Test
    public void testGoodApplication()
    {
    	HousingBenefitApplication hba = goodHuosingBenefitApplication();
    	violations = validator.validate(hba);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testBadApplicaton()
    {
    	HousingBenefitApplication hba = goodHuosingBenefitApplication();
    	hba.getIncomes().get(0).setStartDate(LocalDate.parse("01.11.2020", formatter));
    	hba.getIncomes().get(0).setEndDate(LocalDate.parse("01.08.2020", formatter));
    	violations = validator.validate(hba,ApplicationChecks.class);
        Assertions.assertTrue(violations.size() == 1);
    	violations = validator.validate(hba, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    	violations = validator.validate(hba, AllChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    }

    @Test
    public void testVeryBadApplicaton()
    {
    	HousingBenefitApplication hba = goodHuosingBenefitApplication();
    	hba.getIncomes().get(0).setStartDate(LocalDate.parse("01.11.2020", formatter));
    	hba.getIncomes().get(0).setEndDate(LocalDate.parse("01.08.2020", formatter));

    	hba.getHouseholdMembers().get(1).setStartDate(LocalDate.parse("01.11.2019", formatter));
    	hba.getHouseholdMembers().get(1).setEndDate(LocalDate.parse("01.08.2019", formatter));
    	
    	hba.getHouseholdMembers().get(2).setPersonNumber("020202A002B");
    	
    	violations = validator.validate(hba,AllChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 5);
    }
    
    @Test
    public void testChangeOfApplicationDateRange()
    {
    	HousingBenefitApplication hba = goodHuosingBenefitApplication();
    	violations = validator.validate(hba, AllChecks.class);
        Assertions.assertTrue(violations.isEmpty());
        // This should invalidate really many sub-objects
    	hba.setStartDate(LocalDate.parse("01.01.2019", formatter));
    	hba.setEndDate(LocalDate.parse("01.06.2019", formatter));
    	violations = validator.validate(hba, ApplicationChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    	violations = validator.validate(hba, HouseholdChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 4);
    	violations = validator.validate(hba, ExpenseChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    	violations = validator.validate(hba, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
    	violations = validator.validate(hba, AllChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 8);
    }
    
    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }

}
