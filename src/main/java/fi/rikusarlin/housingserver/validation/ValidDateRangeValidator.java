package fi.rikusarlin.housingserver.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import fi.rikusarlin.housingserver.data.DateRangedEntity;


public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	@Override
	public void initialize (ValidDateRange constraintAnnotation) {
	}

	@Override
	public boolean isValid (Object value, ConstraintValidatorContext context) {
		boolean isValid = true;

        HibernateConstraintValidatorContext hibernateContext = context.unwrap(
                HibernateConstraintValidatorContext.class );
        hibernateContext.disableDefaultConstraintViolation();

		if(!(value instanceof DateRangedEntity)) {
			return false;
		}

		DateRangedEntity dateRange = (DateRangedEntity) value;
		LocalDate startDate = dateRange.getStartDate();
		LocalDate endDate = dateRange.getEndDate();
		
		// If both are given, startDate must be before endDate
    	if(startDate != null && endDate != null) {
			isValid = startDate.isBefore(endDate);
			if(!isValid) {
				ValidatorUtils.addViolation(
           	    	null, 
           	    	0, 
           	    	"startEndDate",
           	    	"start date must be less than end date if both are given, here start date is '{0}' and end date '{1}'",
           			hibernateContext,
           			startDate !=null ? startDate.format(formatter) : "null",
           			endDate != null ? endDate.format(formatter) : "null"
           		);
			}
		}
    	
    	return isValid;
			
	}
}
