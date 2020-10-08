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

import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.model.IncomeType;

public class IncomeTest 
{
	private static boolean setUpIsDone = false;
	private static Validator validator;
	private static DateTimeFormatter formatter;
	Set<ConstraintViolation<IncomeEntity>> violations;

	@BeforeAll
	public static void setUp() {
	    if (setUpIsDone) {
	        return;
	    }
    	validator = Validation.buildDefaultValidatorFactory().getValidator();
    	formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
   	    setUpIsDone = true;
	}
	
    /**
     * Test that income with start and end dates in sensible order is ok
     */
    @Test
    public void testIncomeWithGoodDateRange()
    {
    	IncomeEntity income1 = new IncomeEntity();
    	income1.setId(1);
    	income1.setIncomeType(IncomeType.SALARY);
    	income1.setAmount(1520.25);
    	income1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	income1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    /**
     * Test that income with start and end dates in sensible order is ok
     */
    @Test
    public void testIncomeWithGoodDateRangeNegativeAmount()
    {
    	IncomeEntity income1 = new IncomeEntity();
    	income1.setId(1);
    	income1.setIncomeType(IncomeType.SALARY);
    	income1.setAmount(-1520.25);
    	income1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	income1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
    }

    /**
     * Test that income with only start date is ok
     */
    @Test
    public void testOpenEndedRange()
    {
    	IncomeEntity income1 = new IncomeEntity();
    	income1.setId(1);
    	income1.setIncomeType(IncomeType.SALARY);
    	income1.setAmount(1520.25);
    	income1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	income1.setEndDate(null);
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }
    
    /**
     * Test that income with only start date is ok
     */
    @Test
    public void testOpenStartRange()
    {
    	IncomeEntity income1 = new IncomeEntity();
    	income1.setId(1);
    	income1.setIncomeType(IncomeType.SALARY);
    	income1.setAmount(1520.25);
    	income1.setStartDate(null);
    	income1.setEndDate(LocalDate.parse("01.09.2020", formatter));
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    /**
     * Test that income with start and end dates in wrong order is NOT ok
     */
    @Test
    public void testIncomeWithBadDateRange()
    {
    	IncomeEntity income1 = new IncomeEntity();
    	income1.setId(1);
    	income1.setIncomeType(IncomeType.SALARY);
    	income1.setAmount(1520.25);
    	income1.setStartDate(LocalDate.parse("01.10.2020", formatter));
    	income1.setEndDate(LocalDate.parse("01.09.2020", formatter));
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
    }
    
    @Test
    public void testOtherIncomeWithDescription()
    {
    	IncomeEntity income1 = new IncomeEntity();
    	income1.setId(1);
    	income1.setIncomeType(IncomeType.OTHER);
    	income1.setAmount(1520.25);
    	income1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	income1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	income1.setOtherIncomeDescription("Heitin lestiä Hagiksessa yhden perjantai-illan");
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testOtherIncomeWithoutDescription()
    {
    	IncomeEntity income1 = new IncomeEntity();
    	income1.setId(1);
    	income1.setIncomeType(IncomeType.OTHER);
    	income1.setAmount(1520.25);
    	income1.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	income1.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
    }
    
    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }
}
