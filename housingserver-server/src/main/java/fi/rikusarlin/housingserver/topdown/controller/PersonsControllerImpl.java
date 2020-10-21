package fi.rikusarlin.housingserver.topdown.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.PersonsApi;
import fi.rikusarlin.housingserver.jparepository.PersonJpaRepository;
import fi.rikusarlin.housingserver.model.Person;

@RestController
@Service
@Validated
public class PersonsControllerImpl implements PersonsApi {
	
    @Autowired
    PersonJpaRepository personRepo;
    
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public ResponseEntity<List<Person>> fetchPersons() {
    	return ResponseEntity.ok(
    			StreamSupport.stream(personRepo.findAll().spliterator(), false)
    			.map(pe -> modelMapper.map(pe, Person.class))
    			.collect(Collectors.toList()));
    }
}
