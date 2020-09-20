package fi.rikusarlin.housingserver.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fi.rikusarlin.housingserver.data.ControlledDateRangeEntity;
import fi.rikusarlin.housingserver.data.DateRangedEntity;

public class OverlappingDateRangeValidator implements ConstraintValidator<OverlappingDateRanges, Object> {
	
    private String fieldName1;
    private String fieldName2;

    @Override
    public void initialize(OverlappingDateRanges annotation) {
        fieldName1          = annotation.dateRange1();
        fieldName2          = annotation.dateRange2();
    }

	@Override
	public boolean isValid (Object value, ConstraintValidatorContext context) {

		if(!(value instanceof ControlledDateRangeEntity)) {
			return false;
		}
		
		DateRangedEntity dateRange1 = (DateRangedEntity) ValidatorUtils.getProperty(value, fieldName1);
       	DateRangedEntity dateRange2 = (DateRangedEntity) ValidatorUtils.getProperty(value, fieldName2);

       	/**
       	* What should be do if either is not defined?
       	*/
       	if(dateRange1 == null || dateRange2 == null) {
       		return true;
       	}
		
       	if( dateRange1.getEndDate().isBefore(dateRange2.getStartDate()) ||
       		dateRange1.getStartDate().isAfter(dateRange2.getEndDate())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(fieldName1 + " and " + fieldName2)
                .addConstraintViolation();
       		return false;
       	}
       	
       	return true;
			
	}
}
