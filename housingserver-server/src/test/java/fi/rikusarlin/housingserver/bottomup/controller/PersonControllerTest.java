package fi.rikusarlin.housingserver.bottomup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fi.rikusarlin.housingserver.bottomup.testdata.PersonData;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.DuplicateNotAllowedException;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

	@Mock
    PersonRepository mockPersonRepo;

    @InjectMocks
    PersonController personService;
    
    @AfterEach
    public void tearDown() {
        clearInvocations(mockPersonRepo);
    }

    @Test
    public void testAddNewPerson(){
    	PersonEntity person1 = PersonData.getPerson1();
    	when(mockPersonRepo.save(person1)).thenReturn(person1);
    	when(mockPersonRepo.findByPersonNumber(person1.getPersonNumber())).thenReturn(Optional.empty());
    	
        PersonEntity result = personService.addPerson(person1);

        Assertions.assertTrue(result.equals(person1));
        verify(mockPersonRepo).findByPersonNumber(person1.getPersonNumber());
        verify(mockPersonRepo).save(person1);
    }
    
    @Test
    public void testTryAddingAlreadyExistingPerson(){
    	PersonEntity person1 = PersonData.getPerson1();
    	when(mockPersonRepo.findByPersonNumber(person1.getPersonNumber())).thenReturn(Optional.of(person1));
    	
    	Exception e = Assertions.assertThrows(DuplicateNotAllowedException.class, () -> {
            personService.addPerson(person1);
        });
    	Assertions.assertTrue(e.getMessage().equals("personNumber "+person1.getPersonNumber()));
        verify(mockPersonRepo).findByPersonNumber(person1.getPersonNumber());        
    }
    
    @Test
    public void testTryAddingPersonWithBadPersonNumber(){
    	PersonEntity person2 = PersonData.getPerson2();
    	person2.setPersonNumber("010170-901O");

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.addPerson(person2);
        });
        Assertions.assertTrue(e.getMessage().equals("personNumber: invalid person number '"+person2.getPersonNumber()+"'"));
        verify(mockPersonRepo, times(0)).findByPersonNumber(person2.getPersonNumber());
        verify(mockPersonRepo, times(0)).save(any(PersonEntity.class));

    }
    
    @Test
    public void testFetchAllPersons(){
    	List<PersonEntity> personList = new ArrayList<PersonEntity>();
    	personList.add(PersonData.getPerson1());
    	personList.add(PersonData.getPerson4());
    	when(mockPersonRepo.findAll()).thenReturn(personList);
    	
    	Iterable<PersonEntity> resultList = personService.fetchPersons();

        Assertions.assertTrue(resultList.equals(personList));
        verify(mockPersonRepo).findAll();
    }

    
    @Test
    public void testFetchPersonById_found(){
    	PersonEntity person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1));
    	
        PersonEntity result = personService.fetchPersonById(person1.getId());

        Assertions.assertTrue(result.equals(person1));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testFetchPersonById_notFound(){
    	PersonEntity person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.fetchPersonById(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testCheckPerson_foundButFailsValidation(){
    	PersonEntity person1 = PersonData.getPerson1();
    	person1.setEmail("username@yahoo..com");
    	
    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1));

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.checkPersonById(person1.getId());
        });
        Assertions.assertTrue(e.getMessage().equals("email: invalid emailAddress 'username@yahoo..com'"));
        verify(mockPersonRepo).findById(person1.getId());
    }
    
    @Test
    public void testCheckById_foundAndIsValid(){
    	PersonEntity person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1));
    	
        PersonEntity result = personService.checkPersonById(person1.getId());

        Assertions.assertTrue(result.equals(person1));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testCheckById_notFound(){
    	PersonEntity person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.checkPersonById(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(mockPersonRepo).findById(person1.getId());
    }
    
    @Test
    public void testUpdatePerson_ok(){
    	PersonEntity person1 = PersonData.getPerson1();

    	when(mockPersonRepo.save(person1)).thenReturn(person1);

        PersonEntity result = personService.updatePerson(person1.getId(), person1);

        Assertions.assertTrue(result.equals(person1));
        verify(mockPersonRepo).save(person1);
    }

    @Test
    public void testUpdatePerson_failsValidation(){
    	PersonEntity person1 = PersonData.getPerson1();
    	person1.setEmail("username@yahoo..com");
    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.updatePerson(person1.getId(), person1);
        });
        Assertions.assertTrue(e.getMessage().equals("email: invalid emailAddress 'username@yahoo..com'"));
    }

    @Test
    public void testDeletePerson_found(){
    	PersonEntity person1 = PersonData.getPerson1();
    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1));
    	
        personService.deletePerson(person1.getId());

        verify(mockPersonRepo).findById(person1.getId());
        verify(mockPersonRepo).delete(person1);
    }

    @Test
    public void testDeletePerson_notFound(){
    	PersonEntity person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.deletePerson(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(mockPersonRepo).findById(person1.getId());
    }


}