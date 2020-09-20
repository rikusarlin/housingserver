package fi.rikusarlin.housingserver.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OverlappingDateRangeValidator.class)
@Documented
public @interface OverlappingDateRanges {
    String dateRange1();
    String dateRange2();

    String message () default "date ranges must overlap";

    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}

