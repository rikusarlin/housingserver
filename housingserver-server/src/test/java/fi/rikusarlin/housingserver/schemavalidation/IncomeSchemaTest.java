package fi.rikusarlin.housingserver.schemavalidation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

public class IncomeSchemaTest 
{
    InputStream schemaStream;
    JsonSchema schema;
    ObjectMapper objectMapper;
    JsonSchemaFactory schemaFactory;
    Set<ValidationMessage> validationResult;

	@BeforeEach
	public void setUp() {
        try {
    	    schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);
			schemaStream = (new ClassPathResource("income-schema.json")).getInputStream();
	        schema = schemaFactory.getSchema(schemaStream);
		    objectMapper = new ObjectMapper();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @Test
    public void testGoodIncome()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("income.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(validationResult.isEmpty());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testBadIncomeType()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("income4.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 1);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("incomeType: does not have a value in the enumeration"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testFaultyDateAndAmount()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("income2.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 2);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("is an invalid date"));
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("amount: must have a minimum value"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testMissingDates()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("income3.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 2);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("startDate: is missing but it is required"));
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("endDate: is missing but it is required"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @AfterEach
    public void logValdiationErrorMessages()
    {
    	if(validationResult != null) {
    		validationResult.forEach(vm -> System.out.println("Income:"+vm.getMessage()));
    	}
    }
}
