package fi.rikusarlin.housingserver.controllerimpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fi.rikusarlin.housingserver.api.PersonsApi;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;

@Service
@Validated
public class PersonsControllerImpl implements PersonsApi {
	
    @Autowired
    PersonRepository personRepo;
    
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public ResponseEntity<List<Person>> fetchPersons() {
    	return ResponseEntity.ok(
    			StreamSupport.stream(personRepo.findAll().spliterator(), false)
    			.map(pe -> modelMapper.map(pe, Person.class))
    			.collect(Collectors.toList()));
    }
}
