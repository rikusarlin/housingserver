package fi.rikusarlin.housingserver.validation;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fi.rikusarlin.housingserver.data.DateRangedEntity;


public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {
	
	@Override
	public void initialize (ValidDateRange constraintAnnotation) {
	}

	@Override
	public boolean isValid (Object value, ConstraintValidatorContext context) {

		if(!(value instanceof DateRangedEntity)) {
			return false;
		}
		
		DateRangedEntity dateRange = (DateRangedEntity) value;
		LocalDate startDate = dateRange.getStartDate();
		LocalDate endDate = dateRange.getEndDate();
		
		// If both are given, startDate must be before endDate
    	if(startDate != null && endDate != null) {
			return (startDate.isBefore(endDate));
		} else {
			return true;
		}
			
	}
}
