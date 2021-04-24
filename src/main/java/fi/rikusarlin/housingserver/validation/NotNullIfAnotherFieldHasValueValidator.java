package fi.rikusarlin.housingserver.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Implementation of {@link NotNullIfAnotherFieldHasValue} validator.
 **/
public class NotNullIfAnotherFieldHasValueValidator
    implements ConstraintValidator<NotNullIfAnotherFieldHasValue, Object> {

    private String fieldName;
    private String expectedFieldValue;
    private String dependFieldName;

    @Override
    public void initialize(NotNullIfAnotherFieldHasValue annotation) {
        fieldName          = annotation.fieldName();
        expectedFieldValue = annotation.fieldValue();
        dependFieldName    = annotation.dependFieldName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {

        if (value == null) {
            return true;
        }

       	Object fieldValue       = ValidatorUtils.getProperty(value, fieldName);
        Object dependFieldValue = ValidatorUtils.getProperty(value, dependFieldName);
       
        if (expectedFieldValue.equals(fieldValue.toString()) && dependFieldValue == null) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
                .addPropertyNode(dependFieldName)
                .addConstraintViolation();
            return false;
        }

        return true;
    }
    

}
