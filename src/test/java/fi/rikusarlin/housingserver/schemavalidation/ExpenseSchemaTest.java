package fi.rikusarlin.housingserver.schemavalidation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import fi.rikusarlin.housingserver.data.Expense;

public class ExpenseSchemaTest 
{
	private static boolean setUpIsDone = false;
	Set<ConstraintViolation<Expense>> violations;
    InputStream schemaStream;
    JsonSchema schema;
    ObjectMapper objectMapper;
    JsonSchemaFactory schemaFactory;
    Set<ValidationMessage> validationResult;

	private InputStream inputStreamFromClasspath(String path) {
	    return this.getClass().getClassLoader().getResourceAsStream(path);
	}
	
	@BeforeEach
	public void setUp() {
	    schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
        schemaStream = inputStreamFromClasspath("expense-schema.json");
        schema = schemaFactory.getSchema(schemaStream);
	    objectMapper = new ObjectMapper();
	}
	
    @Test
    public void testGoodExpense()
    {
    	InputStream jsonStream = inputStreamFromClasspath("expense.json");
    	JsonNode json;
		try {
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(validationResult.isEmpty());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testFaultyDate()
    {
    	InputStream jsonStream = inputStreamFromClasspath("expense3.json");
    	JsonNode json;
		try {
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }


    @AfterEach
    public void logValdiationErrorMessages()
    {
    	if(validationResult != null) {
    		validationResult.forEach(vm -> System.out.println("Expense:"+vm.getMessage()));
    	}
    }
}
