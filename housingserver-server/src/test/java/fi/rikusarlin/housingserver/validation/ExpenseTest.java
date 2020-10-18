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

import fi.rikusarlin.housingserver.bottomup.testdata.ExpenseData;
import fi.rikusarlin.housingserver.data.ExpenseEntity;

public class ExpenseTest 
{
	private static boolean setUpIsDone = false;
	private static Validator validator;
	private static DateTimeFormatter formatter;
	Set<ConstraintViolation<ExpenseEntity>> violations;

	private List<String> getMessages(Set<ConstraintViolation<ExpenseEntity>> violations){
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
	
    @Test
    public void testExpenseWithGoodDateRange()
    {
    	ExpenseEntity expense1 = ExpenseData.getExpense1();
    	violations = validator.validate(expense1, ExpenseChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testExpenseWithBadDateRange()
    {
    	ExpenseEntity expense1 = ExpenseData.getExpense1();
    	expense1.setStartDate(LocalDate.parse("01.11.2020", formatter));
    	expense1.setEndDate(LocalDate.parse("01.08.2020", formatter));
    	violations = validator.validate(expense1, ExpenseChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("startEndDate: start date must be less than end date if both are given, here start date is '01.11.2020' and end date '01.08.2020'"));
    }

    @Test
    public void testExpenseWithBadDateRangeAndNegativeAmount()
    {
    	ExpenseEntity expense1 = ExpenseData.getExpense1();
    	expense1.setAmount(-1520.25);
    	expense1.setStartDate(LocalDate.parse("01.11.2020", formatter));
    	expense1.setEndDate(LocalDate.parse("01.08.2020", formatter));
    	violations = validator.validate(expense1, ExpenseChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 2);
        Assertions.assertTrue(getMessages(violations).contains("startEndDate: start date must be less than end date if both are given, here start date is '01.11.2020' and end date '01.08.2020'"));
        Assertions.assertTrue(getMessages(violations).contains("amount: Amount must be greater than zero"));
    }

    @Test
    public void testExpenseWithOtherDescription()
    {
    	ExpenseEntity expense1 = ExpenseData.getExpense2();
    	violations = validator.validate(expense1, ExpenseChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testExpenseWithoutOtherDescription()
    {
    	ExpenseEntity expense1 = ExpenseData.getExpense2();
    	expense1.setOtherExpenseDescription(null);
    	violations = validator.validate(expense1, ExpenseChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("otherExpenseDescription: Field otherExpenseDescription cannot be empty since field expenseType has value OTHER"));
    }

    @AfterEach
    public void logValdiationErrorMessages()
    {
        ValidatorUtils.logValidationErrors(violations);
    }
}
