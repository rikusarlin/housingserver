package fi.rikusarlin.housingserver.controllerimpl;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.PersonApi;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.DuplicateNotAllowedException;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class PersonControllerImpl implements PersonApi {
	
    @Autowired
    PersonRepository personRepo;

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Override
	public ResponseEntity<Person> fetchPersonById(Integer id) {
		PersonEntity p = personRepo.findById(id).orElseThrow(() -> new NotFoundException("Person", id));
		return ResponseEntity.ok(p.toPerson());
	}
 
	@Override
	public ResponseEntity<Person> addPerson(Person person) {
		PersonEntity p = new PersonEntity(person);
		Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(p, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		personRepo.findByPersonNumber(person.getPersonNumber()).ifPresent(
				thePerson -> {
					throw new DuplicateNotAllowedException("personNumber "+thePerson.getPersonNumber());
				});
		return ResponseEntity.ok(personRepo.save(p).toPerson());
	}

	@Override
	public ResponseEntity<Person> checkPersonById(Integer id) {
    	PersonEntity p = personRepo.findById(id).orElseThrow(() -> new NotFoundException("Person", id));
		Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(p, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return fetchPersonById(id);
	}

	@Override
	public ResponseEntity<Person> updatePerson(Integer id, Person person) {
			PersonEntity p = new PersonEntity(person);
			Set<ConstraintViolation<PersonEntity>> violations =  validator.validate(p, InputChecks.class);
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
				 		personRepo.save(p);
				 	});
			return fetchPersonById(id);
	}
	
	@Override
	public ResponseEntity<Void> deletePerson(Integer id) {
		PersonEntity person = personRepo.findById(id).orElseThrow(() -> new NotFoundException("Person", id));
 		personRepo.delete(person);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
