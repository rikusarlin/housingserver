package fi.rikusarlin.housingserver.jparepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.PersonRepository;

@Component("personRepositoryJpa")
public class PersonJpaRepositoryImpl implements PersonRepository {

    @Autowired
    private PersonJpaRepository personJpaRepo;

    public Person save(Person person) {
    	PersonEntity pe = MappingUtil.modelMapper.map(person, PersonEntity.class);
        return MappingUtil.modelMapper.map(personJpaRepo.save(pe), Person.class);
    }


	@Override
	public Optional<Person> findById(Integer id) {
		Optional<PersonEntity> person = personJpaRepo.findById(id);
		if(person.isPresent()) {
			Person p = MappingUtil.modelMapper.map(person.get(), Person.class);
			return Optional.of(p);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<Person> findAll() {
		Iterable<PersonEntity> persons = personJpaRepo.findAll();
		return StreamSupport.stream(persons.spliterator(), false)
				.map(person -> MappingUtil.modelMapper.map(person, Person.class))
				.collect(Collectors.toList());
 	}


	@Override
	public void delete(Person person) {
		personJpaRepo.delete(MappingUtil.modelMapper.map(person, PersonEntity.class));
	}

	@Override
	public Iterable<Person> findAll(Sort sort) {
		Iterable<PersonEntity> persons = personJpaRepo.findAll(sort);
		// It might be better to do this using for-next loop rather than map-reduce to retain order
		List<Person> personList = new ArrayList<Person>();
		for(PersonEntity pe:persons) {
			personList.add(MappingUtil.modelMapper.map(pe, Person.class));
		}
		return personList;
	}

	@Override
	public Optional<Person> findByPersonNumber(String personNumber) {
		Optional<PersonEntity> person = personJpaRepo.findByPersonNumber(personNumber);
		if(person.equals(Optional.empty())) {
			return Optional.empty();
		} else {
			return Optional.of(MappingUtil.modelMapper.map(person.get(), Person.class));
		}
	}
}