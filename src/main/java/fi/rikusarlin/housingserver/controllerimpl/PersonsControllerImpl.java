package fi.rikusarlin.housingserver.controllerimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.PersonsApi;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;

@RestController
@Service
@Validated
public class PersonsControllerImpl implements PersonsApi {
	
    @Autowired
    PersonRepository personRepo;

    @Override
    public ResponseEntity<List<Person>> fetchPersons() {
    	Iterable<PersonEntity> persons = personRepo.findAll();
    	List<Person> personList = new ArrayList<Person>();
    	for(PersonEntity p:persons) {
    		personList.add(p.toPerson());
    	}
    	return ResponseEntity.ok(personList);
    }
}
