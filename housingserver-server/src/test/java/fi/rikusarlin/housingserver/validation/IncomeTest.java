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
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.testdata.IncomeData;

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
	
    @Test
    public void testIncomeWithGoodDateRange()
    {
    	IncomeEntity income1 = 
    			MappingUtil.modelMapper.map(IncomeData.getIncome1(),IncomeEntity.class);
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testIncomeWithGoodDateRangeNegativeAmount()
    {
    	IncomeEntity income1 = 
    			MappingUtil.modelMapper.map(IncomeData.getIncome1(),IncomeEntity.class);
    	income1.setAmount(-1520.25);
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(getMessages(violations).contains("amount: Amount must be greater than zero"));

    }

    @Test
    public void testOpenEndedRange()
    {
    	IncomeEntity income1 = 
    			MappingUtil.modelMapper.map(IncomeData.getIncome1(),IncomeEntity.class);
    	income1.setEndDate(null);
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }
    
    @Test
    public void testOpenStartDate()
    {
    	IncomeEntity income1 = 
    			MappingUtil.modelMapper.map(IncomeData.getIncome1(),IncomeEntity.class);
    	income1.setStartDate(null);
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testIncomeWithBadDateRange()
    {
    	IncomeEntity income1 = 
    			MappingUtil.modelMapper.map(IncomeData.getIncome1(),IncomeEntity.class);
    	income1.setStartDate(LocalDate.parse("01.10.2020", formatter));
    	income1.setEndDate(LocalDate.parse("01.09.2020", formatter));
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(!violations.isEmpty());
        Assertions.assertTrue(violations.size() == 1);
        Assertions.assertTrue(getMessages(violations).contains("startEndDate: start date must be less than end date if both are given, here start date is '01.10.2020' and end date '01.09.2020'"));
    }
    
    @Test
    public void testOtherIncomeWithDescription()
    {
    	IncomeEntity income1 = 
    			MappingUtil.modelMapper.map(IncomeData.getIncome2(),IncomeEntity.class);
    	violations = validator.validate(income1, IncomeChecks.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    @Test
    public void testOtherIncomeWithoutDescription()
    {
    	IncomeEntity income1 = 
    			MappingUtil.modelMapper.map(IncomeData.getIncome2(),IncomeEntity.class);
    	income1.setOtherIncomeDescription(null);
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
