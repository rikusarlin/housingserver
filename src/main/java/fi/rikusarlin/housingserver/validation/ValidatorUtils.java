package fi.rikusarlin.housingserver.validation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

public class ValidatorUtils {
	
    public static Object getProperty(Object bean, String propertyName) {
        BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor propertyDescriptor = Arrays
				.stream(beanInfo.getPropertyDescriptors())
                .filter(pd -> pd.getName().equals(propertyName)).findFirst()
                .get();
			return propertyDescriptor.getReadMethod().invoke(bean);
		} catch (NoSuchElementException | IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
    }
    
    public static <T> void logValidationErrors(Set<ConstraintViolation<T>> violations) {
        for(ConstraintViolation<T> violation:violations) {
        	Set<Class<? extends Payload>> payloads =
                    violation.getConstraintDescriptor().getPayload();
        	boolean payloadFound = false;
        	for (Class<? extends Payload> payload : payloads) {
        		payloadFound = true;
        		if (payload == Severity.Error.class) {
        			System.out.println("ERROR: " + violation.getPropertyPath() + ": " +
                            violation.getMessage());
        		} else if (payload == Severity.Info.class) {
        			System.out.println("INFO: " + violation.getPropertyPath() + ": " +
                            violation.getMessage());
        		}
        	}
        	if(!payloadFound) {
        		System.out.println("UNKNOWN:" + violation.getPropertyPath() + ": " + violation.getMessage());
        	}
        }
    }

    public static void addViolation(
    		String propertyName,
    		String messageTemplate,
    		HibernateConstraintValidatorContext ctx,
    		String... variableValues) {
    	addViolation(null,0, propertyName, messageTemplate, ctx, variableValues);
    }

    public static void addViolation(
    		String collectionName, 
    		int collectionIndex, 
    		String propertyName,
    		String messageTemplate,
    		HibernateConstraintValidatorContext ctx,
    		String... variableValues) {
    	int index = 0;
    	for(String variableValue:variableValues) {
    		ctx.addMessageParameter(index+"", variableValue);
    		index++;
    	}
    	ConstraintViolationBuilder cvb = ctx.buildConstraintViolationWithTemplate(messageTemplate);
    	NodeBuilderCustomizableContext nbcc;
    	if(collectionName != null) {
    		nbcc = cvb.addPropertyNode(collectionName+"["+collectionIndex+"]."+propertyName);
    	} else {
   			nbcc = cvb.addPropertyNode(propertyName);
    	}
    	nbcc.addConstraintViolation();
    }

}
