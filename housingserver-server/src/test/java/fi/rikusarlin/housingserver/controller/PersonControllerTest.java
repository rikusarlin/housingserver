package fi.rikusarlin.housingserver.controller;

import static org.mockito.ArgumentMatchers.any;
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

import fi.rikusarlin.housingserver.controllerimpl.PersonControllerImpl;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.DuplicateNotAllowedException;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.testdata.PersonData;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

	@Mock
    PersonRepository mockPersonRepo;

    @InjectMocks
    PersonControllerImpl personService;
    
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
    	
        ResponseEntity<Person> result = personService.addPerson(person1);

        Assertions.assertTrue(result.equals(ResponseEntity.ok(person1out)));
        verify(mockPersonRepo).findByPersonNumber(person1.getPersonNumber());
        verify(mockPersonRepo).save(any(PersonEntity.class));
    }
    
    @Test
    public void testTryAddingAlreadyExistingPerson(){
    	Person person1 = PersonData.getPerson1();
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);

    	when(mockPersonRepo.findByPersonNumber(person1.getPersonNumber())).thenReturn(Optional.of(person1outEntity));
    	
    	Exception e = Assertions.assertThrows(DuplicateNotAllowedException.class, () -> {
            personService.addPerson(person1);
        });
    	Assertions.assertTrue(e.getMessage().equals("personNumber "+person1.getPersonNumber()));
        verify(mockPersonRepo).findByPersonNumber(person1.getPersonNumber());        
    }
    
    @Test
    public void testTryAddingPersonWithBadPersonNumber(){
    	Person person2 = PersonData.getPerson2();
    	person2.setPersonNumber("010170-901L");

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.addPerson(person2);
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
    	
        ResponseEntity<Person> result = personService.fetchPersonById(person1.getId());

        Assertions.assertTrue(result.equals(ResponseEntity.ok(person1out)));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testFetchPersonById_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.fetchPersonById(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testCheckPerson_foundButFailsValidation(){
    	Person person1 = PersonData.getPerson1();
    	person1.setEmail("username@yahoo..com");
        PersonEntity person1outEntity = MappingUtil.modelMapper.map(person1, PersonEntity.class);
    	
    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.of(person1outEntity));

    	Exception e = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            personService.checkPersonById(person1.getId());
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
    	
        ResponseEntity<Person> result = personService.checkPersonById(person1.getId());

        Assertions.assertTrue(result.equals(ResponseEntity.ok(person1out)));
        verify(mockPersonRepo).findById(person1.getId());
    }

    @Test
    public void testCheckById_notFound(){
    	Person person1 = PersonData.getPerson1();

    	when(mockPersonRepo.findById(person1.getId())).thenReturn(Optional.empty());
    	
    	Exception e = Assertions.assertThrows(NotFoundException.class, () -> {
    		personService.checkPersonById(person1.getId());
        });

        Assertions.assertTrue(e.getMessage().equals("Person not found : "+person1.getId()));
        verify(mockPersonRepo).findById(person1.getId());
    }

}