package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.PersonApi;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.DuplicateNotAllowedException;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class PersonControllerImpl implements PersonApi {
	
	@Autowired
	@Qualifier("personRepositoryJpa")
	PersonRepository personRepo;
    
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	/*
    public PersonControllerImpl(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }
    */
	
	@Override
	public ResponseEntity<Person> fetchPersonById(Integer id) {
		Person person = personRepo.findById(id).orElseThrow(() -> new NotFoundException("Person", id));
		return ResponseEntity.ok(person);
	}
 
	@Override
	public ResponseEntity<Person> addPerson(Person person) {
		PersonEntity p = MappingUtil.modelMapperInsert.map(person, PersonEntity.class);
		Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(p, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		personRepo.findByPersonNumber(person.getPersonNumber()).ifPresent(
				thePerson -> {
					throw new DuplicateNotAllowedException("personNumber "+thePerson.getPersonNumber());
				});
		return ResponseEntity.ok(personRepo.save(person));
	}

	@Override
	public ResponseEntity<Person> checkPersonById(Integer id) {
    	Person p = personRepo.findById(id).orElseThrow(() -> new NotFoundException("Person", id));
    	PersonEntity pe = MappingUtil.modelMapperInsert.map(p, PersonEntity.class);
		Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(pe, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(p);
	}

	@Override
	public ResponseEntity<Person> updatePerson(Integer id, Person person) {
		PersonEntity pe = MappingUtil.modelMapper.map(person, PersonEntity.class);
		pe.setId(id);
		Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(pe, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		person.setId(id);
		return ResponseEntity.ok(personRepo.save(person));
	}
	
	@Override
	public ResponseEntity<Void> deletePerson(Integer id) {
		personRepo.delete(personRepo.findById(id).orElseThrow(() -> new NotFoundException("Person", id)));
 		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
