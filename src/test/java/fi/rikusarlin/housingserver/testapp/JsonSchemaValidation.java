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

import fi.rikusarlin.housingserver.data.Expense;
import fi.rikusarlin.housingserver.data.HouseholdMember;
import fi.rikusarlin.housingserver.data.HousingBenefitApplication;
import fi.rikusarlin.housingserver.data.Income;
import fi.rikusarlin.housingserver.data.Person;
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
	            HouseholdMember householdMember =  objectMapper.readValue((new ClassPathResource("householdMember.json")).getInputStream(), HouseholdMember.class);
	    		Set<ConstraintViolation<HouseholdMember>> violations =  validator.validate(householdMember, HouseholdChecks.class);
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
	            Expense expense =  objectMapper.readValue((new ClassPathResource("expense.json")).getInputStream(), Expense.class);
	    		Set<ConstraintViolation<Expense>> violations =  validator.validate(expense, ExpenseChecks.class);
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
	            Income income =  objectMapper.readValue((new ClassPathResource("income.json")).getInputStream(), Income.class);
	    		Set<ConstraintViolation<Income>> violations =  validator.validate(income, IncomeChecks.class);
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
	            HousingBenefitApplication hba =  objectMapper.readValue((new ClassPathResource("housingbenefit-application.json")).getInputStream(), HousingBenefitApplication.class);
	    		Set<ConstraintViolation<HousingBenefitApplication>> violations =  validator.validate(hba, AllChecks.class);
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
	            Person p =  objectMapper.readValue((new ClassPathResource("person_ok.json")).getInputStream(), Person.class);
	    		Set<ConstraintViolation<Person>> violations =  validator.validate(p, AllChecks.class);
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

 
