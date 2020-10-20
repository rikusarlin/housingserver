package fi.rikusarlin.housingserver.controller;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import fi.rikusarlin.housingserver.exception.DuplicateNotAllowedException;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.testdata.PersonData;
import fi.rikusarlin.housingserver.topdown.controller.PersonControllerImpl;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

	@Mock
    PersonRepository personRepo;

    @InjectMocks
    PersonControllerImpl personService;
    
    @AfterEach
    public void tearDown() {
        clearInvocations(personRepo);
    }

    @Test
    public void testAddNewPerson(){
    	Person person1 = PersonData.getPerson1();

    	when(personRepo.save(person1)).thenReturn(person1);
    	when(personRepo.findByPersonNumber(person1.getPersonNumber())).thenReturn(Optional.empty());
    	
        ResponseEntity<Person> result = personService.addPerson(person1);

        Assertions.assertTrue(result.equals(ResponseEntity.ok(person1)));
        verify(personRepo).findByPersonNumber(person1.getPersonNumber());
        verify(personRepo).save(person1);
    }
    
    @Test
    public void testTryAddingAlreadyExistingPerson(){
    	Person person1 = PersonData.getPerson1();

    	when(personRepo.findByPersonNumber(person1.getPersonNumber())).thenReturn(Optional.of(person1));
    	
    	Exception e = Assertions.assertThrows(DuplicateNotAllowedException.class, () -> {
            personService.addPerson(person1);
        });
    	Assertions.assertTrue(e.getMessage().equals("personNumber "+person1.getPersonNumber()));
        verify(personRepo).findByPersonNumber(person1.getPersonNumber());        
    }
    
    @Test
    public void testTryAddingPersonWithBadPersonNumber(){
    	Person person2 = PersonData.getPerson2();
    	person2.setPersonNumber("010170-901L");

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.addPerson(person2);
        });
        Assertions.assertTrue(e.getMessage().equals("personNumber: invalid person number '"+person2.getPersonNumber()+"'"));
        verify(personRepo, times(0)).findByPersonNumber(person2.getPersonNumber());
        verify(personRepo, times(0)).save(person2);

    }
    
    @Test
    public void testFetchPersonById_found(){
    	Person person1 = PersonData.getPerson1();

    	when(personRepo.findById(person1.getId())).thenReturn(Optional.of(person1));
    	
        ResponseEntity<Person> result = personService.fetchPersonById(person1.getId());

        Assertions.assertTrue(result.equals(ResponseEntity.ok(person1)));
        verify(personRepo).findById(person1.getId());
    }

    @Test
    public void testFetchPersonById_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(personRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.fetchPersonById(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(personRepo).findById(person1.getId());
    }

    @Test
    public void testCheckPerson_foundButFailsValidation(){
    	Person person1 = PersonData.getPerson1();
    	person1.setEmail("username@yahoo..com");
    	
    	when(personRepo.findById(person1.getId())).thenReturn(Optional.of(person1));

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.checkPersonById(person1.getId());
        });
        Assertions.assertTrue(e.getMessage().equals("email: invalid emailAddress 'username@yahoo..com'"));
        verify(personRepo).findById(person1.getId());
    }
    
    @Test
    public void testCheckById_foundAndIsValid(){
    	Person person1 = PersonData.getPerson1();

    	when(personRepo.findById(person1.getId())).thenReturn(Optional.of(person1));
    	
        ResponseEntity<Person> result = personService.checkPersonById(person1.getId());

        Assertions.assertTrue(result.equals(ResponseEntity.ok(person1)));
        verify(personRepo).findById(person1.getId());
    }

    @Test
    public void testCheckById_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(personRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.checkPersonById(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(personRepo).findById(person1.getId());
    }
    
    @Test
    public void testUpdatePerson_ok(){
    	Person person1 = PersonData.getPerson1();
    	person1.setLastName("Onomatopoe");

    	when(personRepo.save(person1)).thenReturn(person1);
    	
        ResponseEntity<Person> result = personService.updatePerson(person1.getId(), person1);

        Assertions.assertTrue(result.equals(ResponseEntity.ok(person1)));
        verify(personRepo).save(person1);
    }

    @Test
    public void testUpdatePerson_failsValidation(){
    	Person person1 = PersonData.getPerson1();
    	person1.setEmail("username@yahoo..com");
    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.updatePerson(person1.getId(), person1);
        });
        Assertions.assertTrue(e.getMessage().equals("email: invalid emailAddress 'username@yahoo..com'"));
    }

    @Test
    public void testDeletePerson_found(){
    	Person person1 = PersonData.getPerson1();
 
    	when(personRepo.findById(person1.getId())).thenReturn(Optional.of(person1));
    	
        personService.deletePerson(person1.getId());

        verify(personRepo).findById(person1.getId());
    }

    @Test
    public void testDeletePerson_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(personRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.deletePerson(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(personRepo).findById(person1.getId());
    }


}