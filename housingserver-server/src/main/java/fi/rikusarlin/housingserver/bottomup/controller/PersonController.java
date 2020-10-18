package fi.rikusarlin.housingserver.bottomup.controller;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.DuplicateNotAllowedException;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class PersonController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    PersonRepository personRepo;

    @GetMapping("/api/v1/persons")
    public @ResponseBody Iterable<PersonEntity> findPersons() {
        return  personRepo.findAll();
    }
     
	@GetMapping(value = "/api/v1/person/{id}")
	public PersonEntity fetchPersonById(
			@PathVariable int id) {
		return personRepo.findById(id).orElseThrow(() -> new NotFoundException("Person", id));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/person")
	public PersonEntity addPerson(
			@RequestBody @Validated(InputChecks.class) PersonEntity person) {
		Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(person, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		personRepo.findByPersonNumber(person.getPersonNumber()).ifPresent(
				thePerson -> {
					throw new DuplicateNotAllowedException("personNumber "+thePerson.getPersonNumber());
				});
		return personRepo.save(person);
	}

	@GetMapping(value = "/api/v1/person/{id}/check")
	public PersonEntity checkPersonById(
			@PathVariable int id) {
    	PersonEntity p = personRepo.findById(id).orElseThrow(() -> new NotFoundException("Person", id));
		Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(p, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return p;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/api/v1/person/{id}")
	public PersonEntity updatePerson(
			@PathVariable int id,
			@RequestBody @Validated(InputChecks.class) PersonEntity person) {
		Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(person, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<PersonEntity> previousPerson = personRepo.findById(id);
		previousPerson.ifPresentOrElse(
				(value) 
					-> {
						value.setBirthDate(person.getBirthDate());
						value.setFirstName(person.getFirstName());
						value.setLastName(person.getLastName());
						value.setPersonNumber(person.getPersonNumber());
						personRepo.save(value);
					},
				()
				 	-> {
				 		personRepo.save(person);
				 	});
		return fetchPersonById(id);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/api/v1/person/{id}")
	public void deletePerson(
			@PathVariable int id) {
		PersonEntity person = personRepo.findById(id).orElseThrow(() -> new NotFoundException("Person", id));
 		personRepo.delete(person);
	}

}
