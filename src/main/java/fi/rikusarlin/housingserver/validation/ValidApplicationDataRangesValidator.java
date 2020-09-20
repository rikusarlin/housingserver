package fi.rikusarlin.housingserver.validation;

import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import fi.rikusarlin.housingserver.data.Expense;
import fi.rikusarlin.housingserver.data.HouseholdMember;
import fi.rikusarlin.housingserver.data.HousingBenefitApplication;
import fi.rikusarlin.housingserver.data.Income;

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

        if (!(value instanceof HousingBenefitApplication)) {
            return false;
        }

        boolean isValid = true;
        HousingBenefitApplication hba = (HousingBenefitApplication) value;
        
        HibernateConstraintValidatorContext hibernateContext = ctx.unwrap(
                HibernateConstraintValidatorContext.class );
        hibernateContext.disableDefaultConstraintViolation();
        
       	// I guess you could generalize the following a bit...
       	
        int index=0;
       	for(HouseholdMember hm:hba.getHouseholdMembers()) {
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
       	for(Expense expense:hba.getHousingExpenses()) {
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
       	for(Income income:hba.getIncomes()) {
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
