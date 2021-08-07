package fi.rikusarlin.housingserver.topdown.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import fi.rikusarlin.housingserver.api.PersonsApi;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Component
@RestController
@Service
@Validated
@RequestMapping("/api/v2/housing")
public class PersonsControllerImpl implements PersonsApi {

	PersonRepository personRepo;

	//private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public PersonsControllerImpl(PersonRepository repo) {
		this.personRepo = repo;
	}
	@Override
	public Mono<ResponseEntity<Person>> fetchPersonById(Integer id, ServerWebExchange exchange) {
		return  personRepo.findById(id)
	             .flatMap(p -> Mono.just(ResponseEntity.ok(MappingUtil.modelMapper.map(p, Person.class))))
				 .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}
	
	/*
	@Override
	public Mono<ResponseEntity<Person>> addPerson(Mono<Person> person, ServerWebExchange exchange) {
		Mono<PersonEntity> p = person.subscribe(pers -> MappingUtil.modelMapperInsert.map(pers, PersonEntity.class));
		Set<ConstraintViolation<PersonEntity>> violations = p.flatMap(p2 -> validator.validate(p2, InputChecks.class));
		if (!violations.isEmpty()) {
			return Mono.error(new ConstraintViolationException(violations));
		}
		Mono<PersonEntity> personFound = personRepo.findByPersonNumber(Mono.just(p.getPersonNumber()));
		if(!personFound.equals(Mono.empty())) {
			return Mono.error(new DuplicateNotAllowedException("personNumber " + personFound.map(pers -> pers.getBirthDate())));
		}
		Person personSaved = (Person) personRepo.save(p).subscribe(pe -> MappingUtil.modelMapper.map(pe, Person.class));
		return Mono.just(ResponseEntity.ok(personSaved));
	}

	@Override
	public Mono<ResponseEntity<Person>> checkPersonById(Integer id, ServerWebExchange exchange) {
		PersonEntity person =  (PersonEntity) personRepo.findById(id).subscribe(
				p -> MappingUtil.modelMapper.map(p, PersonEntity.class));
		if(person == null){
			return Mono.error(new NotFoundException("Person", id));
		}
		Set<ConstraintViolation<PersonEntity>> violations = validator.validate(person, AllChecks.class);
		if (!violations.isEmpty()) {
			return Mono.error(new ConstraintViolationException(violations));
		}
		return Mono.just(ResponseEntity.ok(MappingUtil.modelMapper.map(person, Person.class)));
	}

	@Override
	public Mono<ResponseEntity<Person>> updatePerson(Integer id, Mono<Person> person, ServerWebExchange exchange) {
		PersonEntity p = MappingUtil.modelMapper.map(person, PersonEntity.class);
		p.setId(id);
		Set<ConstraintViolation<PersonEntity>> violations = validator.validate(p, InputChecks.class);
		if (!violations.isEmpty()) {
			return Mono.error(new ConstraintViolationException(violations));
		}
		return Mono.just(ResponseEntity.ok(MappingUtil.modelMapper.map(personRepo.save(p), Person.class)));
	}

	@Override
	public Mono<ResponseEntity<Void>> deletePerson(Integer id, ServerWebExchange exchange) {
		Mono<PersonEntity> person = personRepo.findById(id);
		if(person.equals(Mono.empty())){
			return Mono.error(new NotFoundException("Person", id));
		}
		personRepo.deleteById(id);
		return Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
	}

	@Override
	public Mono<ResponseEntity<Flux<Person>>> fetchPersons(ServerWebExchange exchange) {
		return Mono.just(
				ResponseEntity.ok(
						personRepo.findAll().map(pe -> MappingUtil.modelMapper.map(pe, Person.class))));
	}
	*/
}
