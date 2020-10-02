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

public class HouseholdMemberSchemaTest 
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
			schemaStream = (new ClassPathResource("householdmember-schema.json")).getInputStream();
	        schema = schemaFactory.getSchema(schemaStream);
		    objectMapper = new ObjectMapper();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @Test
    public void testGoodHouseholdMember()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("householdMember.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(validationResult.isEmpty());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testMissingPersonNumber()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("householdMember2.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 1);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("personNumber: is missing but it is required"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testMissingDate()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("householdMember3.json")).getInputStream();
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

    @Test
    public void testTooShortPersonNumber()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("householdMember4.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 2);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("personNumber: does not match the regex pattern"));
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("personNumber: must be at least 11 characters long"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testFaultyPersonNumber()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("householdMember5.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 1);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("personNumber: does not match the regex pattern"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @AfterEach
    public void logValdiationErrorMessages()
    {
    	if(validationResult != null) {
    		validationResult.forEach(vm -> System.out.println("HouseholdMember:"+vm.getMessage()));
    	}
    }
}
