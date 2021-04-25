package fi.rikusarlin.housingserver.validation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;

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
    		ConstraintValidatorContext ctx,
    		String... variableValues) {
    	addViolation(null,0, propertyName, messageTemplate, ctx, variableValues);
    }

    public static void addViolation(
    		String collectionName, 
    		int collectionIndex, 
    		String propertyName,
    		String messageTemplate,
    		ConstraintValidatorContext ctx,
    		String... variableValues) {
    	String formattedMessage = MessageFormat.format(messageTemplate, (Object[]) variableValues);
    	ctx.disableDefaultConstraintViolation();
    	if(collectionName != null) {
        	ctx.buildConstraintViolationWithTemplate(formattedMessage)
        	.addPropertyNode(collectionName+"["+collectionIndex+"]."+propertyName).addConstraintViolation();
    	} else {
        	ctx.buildConstraintViolationWithTemplate(formattedMessage)
        	.addPropertyNode(propertyName).addConstraintViolation();
    	}
    }

}
