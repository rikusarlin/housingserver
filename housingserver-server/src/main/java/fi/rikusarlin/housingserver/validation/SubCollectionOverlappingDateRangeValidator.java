package fi.rikusarlin.housingserver.validation;

import java.time.format.DateTimeFormatter;
import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import fi.rikusarlin.housingserver.data.DateRangedEntity;

/**
 * Implementation of {@link SubCollectionOverlappingDateRange} validator.
 **/
public class SubCollectionOverlappingDateRangeValidator
    implements ConstraintValidator<SubCollectionOverlappingDateRange, Object> {

    private String collectionName;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public void initialize(SubCollectionOverlappingDateRange annotation) {
        collectionName = annotation.collectionName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {

        if (value == null) {
            return true;
        }

        boolean isValid = true;
       	DateRangedEntity entity1 = (DateRangedEntity) value;
       	Collection<?> collection = (Collection<?>) ValidatorUtils.getProperty(value, collectionName);

        HibernateConstraintValidatorContext hibernateContext = ctx.unwrap(
                HibernateConstraintValidatorContext.class );
        hibernateContext.disableDefaultConstraintViolation();
       	
        int index=0;
       	for(Object subvalue:collection) {
           	DateRangedEntity entity2 = (DateRangedEntity) subvalue;
           	if(     ((entity1.getEndDate()!=null) && (entity2.getStartDate()!=null) && (entity1.getEndDate().isBefore(entity2.getStartDate()))) 
           	     || ((entity1.getStartDate()!=null) && (entity2.getEndDate()!=null) && (entity1.getStartDate().isAfter(entity2.getEndDate())))) {
           		ValidatorUtils.addViolation(
           	    		collectionName, 
           	    		index, 
           	    		"startEndDate",
           	    		"must overlap with application date range, and {0}-{1} and {2}-{3} do not overlap",
           				hibernateContext,
           				entity1.getStartDate()!=null ? entity1.getStartDate().format(formatter) : "null",
           				entity1.getEndDate()!=null ? entity1.getEndDate().format(formatter) : "null",
           				entity2.getStartDate()!=null ? entity2.getStartDate().format(formatter) : "null",
           				entity2.getEndDate()!=null ? entity2.getEndDate().format(formatter) : "null"
           		);
                isValid = false;
           	}
           	index++;
       	}
       	return isValid;
    }
    

}
