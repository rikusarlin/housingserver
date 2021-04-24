package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import fi.rikusarlin.housingserver.api.PersonsApiService;
import fi.rikusarlin.housingserver.api.NotFoundException;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.DuplicateNotAllowedException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

public class PersonsControllerImpl implements PersonsApiService {

	@Inject
	PersonRepository personRepo;
    @Inject
    Validator validator;

	@Override
	public Response fetchPersonById(Integer id, SecurityContext securityContext) throws NotFoundException{
		PersonEntity p = personRepo.findById(id).orElseThrow(() -> new NotFoundException(id, "Person"));
		return Response.ok(MappingUtil.modelMapper.map(p, Person.class)).build();
	}

	@Override
	public Response addPerson(Person person, SecurityContext securityContext) throws NotFoundException{
		PersonEntity p = MappingUtil.modelMapperInsert.map(person, PersonEntity.class);
		Set<ConstraintViolation<PersonEntity>> violations = validator.validate(p, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		personRepo.findByPersonNumber(person.getPersonNumber()).ifPresent(thePerson -> {
			throw new DuplicateNotAllowedException("personNumber " + thePerson.getPersonNumber());
		});
		PersonEntity personEntitySaved = personRepo.save(p);
		Person personSaved = MappingUtil.modelMapper.map(personEntitySaved, Person.class);
		return Response.ok(personSaved).build();
	}

	@Override
	public Response checkPersonById(Integer id, SecurityContext securityContext) throws NotFoundException{
		PersonEntity p = personRepo.findById(id).orElseThrow(() -> new NotFoundException(id, "Person"));
		Set<ConstraintViolation<PersonEntity>> violations = validator.validate(p, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return Response.ok(MappingUtil.modelMapper.map(p, Person.class)).build();
	}

	@Override
	public Response updatePerson(Integer id, Person person, SecurityContext securityContext) throws NotFoundException{
		PersonEntity p = MappingUtil.modelMapper.map(person, PersonEntity.class);
		p.setId(id);
		Set<ConstraintViolation<PersonEntity>> violations = validator.validate(p, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return Response.ok(MappingUtil.modelMapper.map(personRepo.save(p), Person.class)).build();
	}

	@Override
	public Response deletePerson(Integer id, SecurityContext securityContext) throws NotFoundException{
		PersonEntity person = personRepo.findById(id).orElseThrow(() -> new NotFoundException(id, "Person"));
		personRepo.delete(person);
		return Response.ok().build();
	}

	@Override
	public Response fetchPersons(SecurityContext securityContext) throws NotFoundException{
		return Response.ok(StreamSupport.stream(personRepo.findAll().spliterator(), false)
				.map(pe -> MappingUtil.modelMapper.map(pe, Person.class)).collect(Collectors.toList())).build();
	}
}
