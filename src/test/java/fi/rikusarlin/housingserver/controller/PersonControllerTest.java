package fi.rikusarlin.housingserver.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.DuplicateNotAllowedException;
import fi.rikusarlin.housingserver.api.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.testdata.PersonData;
import fi.rikusarlin.housingserver.topdown.controller.PersonsControllerImpl;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

	@Mock
    PersonRepository mockPersonRepo;

    @InjectMocks
    PersonsControllerImpl personService;
    
    SecurityContext securityContext;
 
 	@BeforeEach
 	public void setUp() {
 	   securityContext = Mockito.mock(SecurityContext.class); 		
 	}
 	
    @AfterEach
    public void tearDown() {
        clearInvocations(mockPersonRepo);
    }

    @Test
    public void testAddNewPerson(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
        Person person1out = MappingUtil.modelMapper.map(person1outEntity, Person.class);

    	when(mockPersonRepo.save(any(PersonEntity.class))).thenReturn(person1outEntity);
    	when(mockPersonRepo.findByPersonNumber(person1.getPersonNumber())).thenReturn(Optional.empty());
    	
    	
        Response result = null;
		try {
			result = personService.addPerson(person1, securityContext);
		} catch (fi.rikusarlin.housingserver.api.NotFoundException e) {
			e.printStackTrace();
		}

		Assertions.assertTrue(result.getEntity().equals(Response.ok(person1out).build().getEntity()));
        verify(mockPersonRepo).findByPersonNumber(person1.getPersonNumber());
        verify(mockPersonRepo).save(any(PersonEntity.class));
    }
    
    @Test
    public void testTryAddingAlreadyExistingPerson(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);

    	when(mockPersonRepo.findByPersonNumber(person1.getPersonNumber())).thenReturn(Optional.of(person1outEntity));
    	
    	Exception e = Assertions.assertThrows(DuplicateNotAllowedException.class, () -> {
            personService.addPerson(person1, securityContext);
        });
    	Assertions.assertTrue(e.getMessage().equals("personNumber "+person1.getPersonNumber()));
        verify(mockPersonRepo).findByPersonNumber(person1.getPersonNumber());        
    }
    
    @Test
    public void testTryAddingPersonWithBadPersonNumber(){
    	Person person2 = PersonData.getPerson2();
    	person2.setPersonNumber("010170-901L");

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.addPerson(person2, securityContext);
        });
        Assertions.assertTrue(e.getMessage().equals("personNumber: invalid person number '"+person2.getPersonNumber()+"'"));
        verify(mockPersonRepo, times(0)).findByPersonNumber(person2.getPersonNumber());
        verify(mockPersonRepo, times(0)).save(any(PersonEntity.class));

    }
    
    @Test
    public void testFetchPersonById_found(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
        Person person1out = MappingUtil.modelMapper.map(person1outEntity, Person.class);

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1outEntity));
    	
        Response result = null;
		try {
			result = personService.fetchPersonById(person1.getId(), securityContext);
		} catch (fi.rikusarlin.housingserver.api.NotFoundException e) {
			e.printStackTrace();
		}

        Assertions.assertTrue(result.getEntity().equals(Response.ok(person1out).build().getEntity()));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testFetchPersonById_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.fetchPersonById(person1.getId(), securityContext);
        });

        Assertions.assertTrue(e.getMessage().equals("Person"));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testCheckPerson_foundButFailsValidation(){
    	Person person1 = PersonData.getPerson1();
    	person1.setEmail("username@yahoo..com");
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
    	
    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1outEntity));

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.checkPersonById(person1.getId(), securityContext);
        });
        Assertions.assertTrue(e.getMessage().equals("email: invalid emailAddress 'username@yahoo..com'"));
        verify(mockPersonRepo).findById(person1.getId());
    }
    
    @Test
    public void testCheckById_foundAndIsValid(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
        Person person1out = MappingUtil.modelMapper.map(person1outEntity, Person.class);

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1outEntity));
    	
        Response result = null;
		try {
			result = personService.checkPersonById(person1.getId(), securityContext);
		} catch (fi.rikusarlin.housingserver.api.NotFoundException e) {
			e.printStackTrace();
		}

        Assertions.assertTrue(result.getEntity().equals(Response.ok(person1out).build().getEntity()));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testCheckById_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.checkPersonById(person1.getId(), securityContext);
        });

        Assertions.assertTrue(e.getMessage().equals("Person"));
        verify(mockPersonRepo).findById(person1.getId());
    }
    
    @Test
    public void testUpdatePerson_ok(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1Entity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
        Person person1out = MappingUtil.modelMapper.map(person1Entity, Person.class);

    	when(mockPersonRepo.save(any(PersonEntity.class))).thenReturn(person1Entity);
    	
        Response result = null;
		try {
			result = personService.updatePerson(person1.getId(), person1, securityContext);
		} catch (fi.rikusarlin.housingserver.api.NotFoundException e) {
			e.printStackTrace();
		}

        Assertions.assertTrue(result.getEntity().equals(Response.ok(person1out).build().getEntity()));
        verify(mockPersonRepo).save(any(PersonEntity.class));
    }

    @Test
    public void testUpdatePerson_failsValidation(){
    	Person person1 = PersonData.getPerson1();
    	person1.setEmail("username@yahoo..com");
    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.updatePerson(person1.getId(), person1, securityContext);
        });
        Assertions.assertTrue(e.getMessage().equals("email: invalid emailAddress 'username@yahoo..com'"));
    }

    @Test
    public void testDeletePerson_found(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
 
    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1outEntity));
    	
        try {
			personService.deletePerson(person1.getId(), securityContext);
		} catch (fi.rikusarlin.housingserver.api.NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testDeletePerson_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.deletePerson(person1.getId(), securityContext);
        });

        Assertions.assertTrue(e.getMessage().equals("Person"));
        verify(mockPersonRepo).findById(person1.getId());
    }


}