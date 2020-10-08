package fi.rikusarlin.housingserver.validation;

import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;

/**
 * Implementation of {@link ValidApplicationDataRanges} validator.
 **/
public class ValidApplicationDataRangesValidator
    implements ConstraintValidator<ValidApplicationDataRanges, Object> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void initialize(ValidApplicationDataRanges annotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {

        if (!(value instanceof HousingBenefitApplicationEntity)) {
            return false;
        }

        boolean isValid = true;
        HousingBenefitApplicationEntity hba = (HousingBenefitApplicationEntity) value;
        
        HibernateConstraintValidatorContext hibernateContext = ctx.unwrap(
                HibernateConstraintValidatorContext.class );
        hibernateContext.disableDefaultConstraintViolation();
        
       	// I guess you could generalize the following a bit...
       	
        int index=0;
       	for(HouseholdMemberEntity hm:hba.getHouseholdMembers()) {
           	if( hba.getEndDate().isBefore(hm.getStartDate()) ||
           		hba.getStartDate().isAfter(hm.getEndDate())) {
           		ValidatorUtils.addViolation(
           	    		"householdMembers", 
           	    		index, 
           	    		"dateRange",
           	    		"must overlap with application date range, and {0}-{1} and {2}-{3} do not overlap",
           				hibernateContext,
                   		hba.getStartDate().format(formatter),
                   		hba.getEndDate().format(formatter),
                   		hm.getStartDate().format(formatter),
                   		hm.getEndDate().format(formatter)
           		);
                isValid = false;
           	}
           	index++;
       	}
       	
        index=0;
       	for(ExpenseEntity expense:hba.getHousingExpenses()) {
           	if( hba.getEndDate().isBefore(expense.getStartDate()) ||
           		hba.getStartDate().isAfter(expense.getEndDate())) {
           		ValidatorUtils.addViolation(
           	    		"housingExpenses", 
           	    		index, 
           	    		"dateRange",
           	    		"must overlap with application date range, and {0}-{1} and {2}-{3} do not overlap",
           				hibernateContext,
                   		hba.getStartDate().format(formatter),
                   		hba.getEndDate().format(formatter),
                   		expense.getStartDate().format(formatter),
                   		expense.getEndDate().format(formatter)
           		);
                isValid = false;
           	}
           	index++;
       	}

        index=0;
       	for(IncomeEntity income:hba.getIncomes()) {
       		if( hba.getEndDate().isBefore(income.getStartDate()) ||
           		hba.getStartDate().isAfter(income.getEndDate())) {
           		ValidatorUtils.addViolation(
           	    		"incomes", 
           	    		index, 
           	    		"dateRange",
           	    		"must overlap with application date range, and {0}-{1} and {2}-{3} do not overlap",
           				hibernateContext,
                   		hba.getStartDate().format(formatter),
                   		hba.getEndDate().format(formatter),
                   		income.getStartDate().format(formatter),
                   		income.getEndDate().format(formatter)
           		);
                isValid = false;
           	}
           	index++;
       	}
       	
       	
       	return isValid;
    }
}
