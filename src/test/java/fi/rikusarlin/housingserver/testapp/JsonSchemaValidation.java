package fi.rikusarlin.housingserver.testapp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.ExpenseChecks;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.IncomeChecks;
import fi.rikusarlin.housingserver.validation.ValidatorUtils;

public class JsonSchemaValidation {
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		
	public static void main(String[] args) throws Exception {
	    ObjectMapper objectMapper = new ObjectMapper();
	    JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
	 
	    try (
	            InputStream jsonStream = (new ClassPathResource("householdMember.json")).getInputStream();
	            InputStream schemaStream = (new ClassPathResource("householdMember-schema.json")).getInputStream();
	    ) {
	        JsonNode json = objectMapper.readTree(jsonStream);
	        JsonSchema schema = schemaFactory.getSchema(schemaStream);
	        Set<ValidationMessage> validationResult = schema.validate(json);
	 
	        // print validation errors
	        if (validationResult.isEmpty()) {
	            System.out.println("Household member: no validation errors from JSON schema validation");
	            // Now we can safely populate the Java object
	            HouseholdMemberEntity householdMember =  objectMapper.readValue((new ClassPathResource("householdMember.json")).getInputStream(), HouseholdMemberEntity.class);
	    		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(householdMember, HouseholdChecks.class);
	    		if (violations.isEmpty()) {
		            System.out.println("Household member: no validation errors from Spring validation");
	    		} else {
	    			ValidatorUtils.logValidationErrors(violations);
	    			
	    		}

	        } else {
	            validationResult.forEach(vm -> System.out.println("Household member:"+vm.getMessage()));
	        }
	    } catch (IOException ioe) {
	    	System.out.println(ioe.getMessage());
	    }

	    try (
	            InputStream jsonStream = (new ClassPathResource("expense.json")).getInputStream();
	            InputStream schemaStream = (new ClassPathResource("expense-schema.json")).getInputStream();
	    ) {
	        JsonNode json = objectMapper.readTree(jsonStream);
	        JsonSchema schema = schemaFactory.getSchema(schemaStream);
	        Set<ValidationMessage> validationResult = schema.validate(json);
	 
	        // print validation errors
	        if (validationResult.isEmpty()) {
	            System.out.println("Expense: no validation errors from JSON schema validation");
	            // Now we can safely populate the Java object
	            ExpenseEntity expense =  objectMapper.readValue((new ClassPathResource("expense.json")).getInputStream(), ExpenseEntity.class);
	    		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(expense, ExpenseChecks.class);
	    		if (violations.isEmpty()) {
		            System.out.println("Expense: no validation errors from Spring validation");
	    		} else {
	    			ValidatorUtils.logValidationErrors(violations);
	    			
	    		}

	        } else {
	            validationResult.forEach(vm -> System.out.println("Expense: "+vm.getMessage()));
	        }
	    } catch (IOException ioe) {
	    	System.out.println(ioe.getMessage());
	    }

	    try (
	            InputStream jsonStream = (new ClassPathResource("income.json")).getInputStream();
	            InputStream schemaStream = (new ClassPathResource("income-schema.json")).getInputStream();
	    ) {
	        JsonNode json = objectMapper.readTree(jsonStream);
	        JsonSchema schema = schemaFactory.getSchema(schemaStream);
	        Set<ValidationMessage> validationResult = schema.validate(json);
	 
	        // print validation errors
	        if (validationResult.isEmpty()) {
	            System.out.println("Income: no validation errors from JSON schema validation");
	            // Now we can safely populate the Java object
	            IncomeEntity income =  objectMapper.readValue((new ClassPathResource("income.json")).getInputStream(), IncomeEntity.class);
	    		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(income, IncomeChecks.class);
	    		if (violations.isEmpty()) {
		            System.out.println("Income: no validation errors from Spring validation");
	    		} else {
	    			ValidatorUtils.logValidationErrors(violations);
	    			
	    		}

	        } else {
	            validationResult.forEach(vm -> System.out.println("Income: "+vm.getMessage()));
	        }
	    } catch (IOException ioe) {
	    	System.out.println(ioe.getMessage());
	    }

	    try (
	            InputStream jsonStream = (new ClassPathResource("housingbenefit-application.json")).getInputStream();
	            InputStream schemaStream = (new ClassPathResource("housingbenefit-schema.json")).getInputStream();
	    ) {
	        JsonNode json = objectMapper.readTree(jsonStream);
	        JsonSchema schema = schemaFactory.getSchema(schemaStream);
	        Set<ValidationMessage> validationResult = schema.validate(json);
	 
	        // print validation errors
	        if (validationResult.isEmpty()) {
	            System.out.println("Housingbenefit: no validation errors from JSON schema validation");
	            // Now we can safely populate the Java object
	            HousingBenefitApplicationEntity hba =  objectMapper.readValue((new ClassPathResource("housingbenefit-application.json")).getInputStream(), HousingBenefitApplicationEntity.class);
	    		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hba, AllChecks.class);
	    		if (violations.isEmpty()) {
		            System.out.println("Housingbenefit: no validation errors from Spring validation");
	    		} else {
	    			ValidatorUtils.logValidationErrors(violations);
	    			
	    		}

	        } else {
	            validationResult.forEach(vm -> System.out.println("Housingbenefit: "+vm.getMessage()));
	        }
	    } catch (IOException ioe) {
	    	System.out.println(ioe.getMessage());
	    }

	    try (
	            InputStream jsonStream = (new ClassPathResource("person_ok.json")).getInputStream();
	            InputStream schemaStream = (new ClassPathResource("person-schema.json")).getInputStream();
	    ) {
	        JsonNode json = objectMapper.readTree(jsonStream);
	        JsonSchema schema = schemaFactory.getSchema(schemaStream);
	        Set<ValidationMessage> validationResult = schema.validate(json);
	 
	        // print validation errors
	        if (validationResult.isEmpty()) {
	            System.out.println("Perdon: no validation errors from JSON schema validation");
	            // Now we can safely populate the Java object
	            PersonEntity p =  objectMapper.readValue((new ClassPathResource("person_ok.json")).getInputStream(), PersonEntity.class);
	    		Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(p, AllChecks.class);
	    		if (violations.isEmpty()) {
		            System.out.println("Person: no validation errors from Spring validation");
	    		} else {
	    			ValidatorUtils.logValidationErrors(violations);
	    			
	    		}

	        } else {
	            validationResult.forEach(vm -> System.out.println("Housingbenefit: "+vm.getMessage()));
	        }
	    } catch (IOException ioe) {
	    	System.out.println(ioe.getMessage());
	    }

	}

}

 
