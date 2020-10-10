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
@Constraint(validatedBy = ValidPersonNumberValidator.class)
@Documented
public @interface ValidPersonNumber {

    String message () default "invalid person number '${validatedValue}'";

    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}

