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

public class PersonSchemaTest 
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
			schemaStream = (new ClassPathResource("person-schema.json")).getInputStream();
	        schema = schemaFactory.getSchema(schemaStream);
		    objectMapper = new ObjectMapper();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @Test
    public void testGoodPerson()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("person_ok.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(validationResult.isEmpty());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testGoodPerson2()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("person_ok2.json")).getInputStream();
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
	    	InputStream jsonStream = (new ClassPathResource("person_missingPersonNumber.json")).getInputStream();
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
    public void testMissingBirthDate()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("person_missingBirthDate.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 1);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("birthDate: is missing but it is required"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testMissingFirstName()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("person_missingFirstName.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 1);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("firstName: is missing but it is required"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testMissingLastName()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("person_missingLastName.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 1);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("lastName: is missing but it is required"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testTooLongLastName()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("person_tooLongLastName.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 1);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("lastName: may only be 80 characters long"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testTooShortPersonNumber()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("person_tooShortPersonNumber.json")).getInputStream();
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
    public void testInvalidBirthDate()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("person_invalidBirthDate.json")).getInputStream();
	    	JsonNode json;
			json = objectMapper.readTree(jsonStream);
	    	validationResult = schema.validate(json);
	        Assertions.assertTrue(!validationResult.isEmpty());
	        Assertions.assertTrue(validationResult.size() == 1);
	        Iterator<ValidationMessage> iter = validationResult.iterator();
	        Assertions.assertTrue(((ValidationMessage)iter.next()).getMessage().contains("birthDate: 1971-13-17 is an invalid date"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Test
    public void testFaultyPersonNumber()
    {
		try {
	    	InputStream jsonStream = (new ClassPathResource("person_invalidPersonNumber.json")).getInputStream();
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
