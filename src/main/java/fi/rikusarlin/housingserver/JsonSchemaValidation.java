package fi.rikusarlin.housingserver;

import java.io.InputStream;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

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
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.ExpenseChecks;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.IncomeChecks;
import fi.rikusarlin.housingserver.validation.ValidatorUtils;

public class JsonSchemaValidation {
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	
	private static InputStream inputStreamFromClasspath(String path) {
	    return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}
	
	public static void main(String[] args) throws Exception {
	    ObjectMapper objectMapper = new ObjectMapper();
	    JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
	 
	    try (
	            InputStream jsonStream = inputStreamFromClasspath("householdMember.json");
	            InputStream schemaStream = inputStreamFromClasspath("householdmember-schema.json")
	    ) {
	        JsonNode json = objectMapper.readTree(jsonStream);
	        JsonSchema schema = schemaFactory.getSchema(schemaStream);
	        Set<ValidationMessage> validationResult = schema.validate(json);
	 
	        // print validation errors
	        if (validationResult.isEmpty()) {
	            System.out.println("Household member: no validation errors from JSON schema validation");
	            // Now we can safely populate the Java object
	            HouseholdMember householdMember =  objectMapper.readValue(inputStreamFromClasspath("householdMember.json"), HouseholdMember.class);
	    		Set<ConstraintViolation<HouseholdMember>> violations =  validator.validate(householdMember, HouseholdChecks.class);
	    		if (violations.isEmpty()) {
		            System.out.println("Household member: no validation errors from Spring validation");
	    		} else {
	    			ValidatorUtils.logValidationErrors(violations);
	    			
	    		}

	        } else {
	            validationResult.forEach(vm -> System.out.println("Household member:"+vm.getMessage()));
	        }
	    }

	    try (
	            InputStream jsonStream = inputStreamFromClasspath("expense3.json");
	            InputStream schemaStream = inputStreamFromClasspath("expense-schema.json")
	    ) {
	        JsonNode json = objectMapper.readTree(jsonStream);
	        JsonSchema schema = schemaFactory.getSchema(schemaStream);
	        Set<ValidationMessage> validationResult = schema.validate(json);
	 
	        // print validation errors
	        if (validationResult.isEmpty()) {
	            System.out.println("Expense: no validation errors from JSON schema validation");
	            // Now we can safely populate the Java object
	            Expense expense =  objectMapper.readValue(inputStreamFromClasspath("expense.json"), Expense.class);
	    		Set<ConstraintViolation<Expense>> violations =  validator.validate(expense, ExpenseChecks.class);
	    		if (violations.isEmpty()) {
		            System.out.println("Expense: no validation errors from Spring validation");
	    		} else {
	    			ValidatorUtils.logValidationErrors(violations);
	    			
	    		}

	        } else {
	            validationResult.forEach(vm -> System.out.println("Expense: "+vm.getMessage()));
	        }
	    }

	    try (
	            InputStream jsonStream = inputStreamFromClasspath("income.json");
	            InputStream schemaStream = inputStreamFromClasspath("income-schema.json")
	    ) {
	        JsonNode json = objectMapper.readTree(jsonStream);
	        JsonSchema schema = schemaFactory.getSchema(schemaStream);
	        Set<ValidationMessage> validationResult = schema.validate(json);
	 
	        // print validation errors
	        if (validationResult.isEmpty()) {
	            System.out.println("Income: no validation errors from JSON schema validation");
	            // Now we can safely populate the Java object
	            Income income =  objectMapper.readValue(inputStreamFromClasspath("income.json"), Income.class);
	    		Set<ConstraintViolation<Income>> violations =  validator.validate(income, IncomeChecks.class);
	    		if (violations.isEmpty()) {
		            System.out.println("Income: no validation errors from Spring validation");
	    		} else {
	    			ValidatorUtils.logValidationErrors(violations);
	    			
	    		}

	        } else {
	            validationResult.forEach(vm -> System.out.println("Income: "+vm.getMessage()));
	        }
	    }

	    try (
	            InputStream jsonStream = inputStreamFromClasspath("housingbenefit-application.json");
	            InputStream schemaStream = inputStreamFromClasspath("housingbenefit-schema.json")
	    ) {
	        JsonNode json = objectMapper.readTree(jsonStream);
	        JsonSchema schema = schemaFactory.getSchema(schemaStream);
	        Set<ValidationMessage> validationResult = schema.validate(json);
	 
	        // print validation errors
	        if (validationResult.isEmpty()) {
	            System.out.println("Housingbenefit: no validation errors from JSON schema validation");
	            // Now we can safely populate the Java object
	            HousingBenefitApplication hba =  objectMapper.readValue(inputStreamFromClasspath("housingbenefit-application.json"), HousingBenefitApplication.class);
	    		Set<ConstraintViolation<HousingBenefitApplication>> violations =  validator.validate(hba, AllChecks.class);
	    		if (violations.isEmpty()) {
		            System.out.println("Housingbenefit: no validation errors from Spring validation");
	    		} else {
	    			ValidatorUtils.logValidationErrors(violations);
	    			
	    		}

	        } else {
	            validationResult.forEach(vm -> System.out.println("Housingbenefit: "+vm.getMessage()));
	        }
	    }

	}

}

 
