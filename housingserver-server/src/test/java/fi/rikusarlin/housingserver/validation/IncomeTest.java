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

	private List<String> getMessages(Set<ConstraintViolation<IncomeEntity>> violations){
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
        Assertions.assertTrue(getMessages(violations).contains("amount: Amount must be greater than zero"));

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
        Assertions.assertTrue(getMessages(violations).contains(": start date must be less than end date if both are given, here start date is '2020-10-01' and end date '2020-09-01'"));
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
        Assertions.assertTrue(getMessages(violations).contains("otherIncomeDescription: Field otherIncomeDescription cannot be empty since field incomeType has value OTHER"));
    }
    
    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }
}
