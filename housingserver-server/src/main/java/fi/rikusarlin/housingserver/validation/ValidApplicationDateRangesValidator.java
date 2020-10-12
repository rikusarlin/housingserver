package fi.rikusarlin.housingserver.validation;

import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;

/**
 * Implementation of {@link ValidApplicationDateRanges} validator.
 **/
public class ValidApplicationDateRangesValidator
    implements ConstraintValidator<ValidApplicationDateRanges, Object> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void initialize(ValidApplicationDateRanges annotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {

        if (!(value instanceof HousingBenefitCaseEntity)) {
            return false;
        }

        boolean isValid = true;
        HousingBenefitCaseEntity hbce = (HousingBenefitCaseEntity) value;
        
        HibernateConstraintValidatorContext hibernateContext = ctx.unwrap(
                HibernateConstraintValidatorContext.class );
        hibernateContext.disableDefaultConstraintViolation();
        
       	// I guess you could generalize the following a bit...
       	
        int index=0;
       	for(HouseholdMemberEntity hm:hbce.getHouseholdMembers()) {
           	if( hbce.getApplication().getEndDate().isBefore(hm.getStartDate()) ||
           		hbce.getApplication().getStartDate().isAfter(hm.getEndDate())) {
           		ValidatorUtils.addViolation(
           	    		"householdMembers", 
           	    		index, 
           	    		"dateRange",
           	    		"must overlap with application date range, and {0}-{1} and {2}-{3} do not overlap",
           				hibernateContext,
           				hbce.getApplication().getStartDate().format(formatter),
           				hbce.getApplication().getEndDate().format(formatter),
                   		hm.getStartDate().format(formatter),
                   		hm.getEndDate().format(formatter)
           		);
                isValid = false;
           	}
           	index++;
       	}
       	
        index=0;
       	for(ExpenseEntity expense:hbce.getHousingExpenses()) {
           	if( hbce.getApplication().getEndDate().isBefore(expense.getStartDate()) ||
           		hbce.getApplication().getStartDate().isAfter(expense.getEndDate())) {
           		ValidatorUtils.addViolation(
           	    		"housingExpenses", 
           	    		index, 
           	    		"dateRange",
           	    		"must overlap with application date range, and {0}-{1} and {2}-{3} do not overlap",
           				hibernateContext,
           				hbce.getApplication().getStartDate().format(formatter),
           				hbce.getApplication().getEndDate().format(formatter),
                   		expense.getStartDate().format(formatter),
                   		expense.getEndDate().format(formatter)
           		);
                isValid = false;
           	}
           	index++;
       	}

        index=0;
       	for(IncomeEntity income:hbce.getIncomes()) {
       		if( hbce.getApplication().getEndDate().isBefore(income.getStartDate()) ||
       			hbce.getApplication().getStartDate().isAfter(income.getEndDate())) {
           		ValidatorUtils.addViolation(
           	    		"incomes", 
           	    		index, 
           	    		"dateRange",
           	    		"must overlap with application date range, and {0}-{1} and {2}-{3} do not overlap",
           				hibernateContext,
           				hbce.getApplication().getStartDate().format(formatter),
           				hbce.getApplication().getEndDate().format(formatter),
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
