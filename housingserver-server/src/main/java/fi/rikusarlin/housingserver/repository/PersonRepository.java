package fi.rikusarlin.housingserver.repository;

import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.model.Person;

public interface PersonRepository{
	Person save(Person entity);
	Optional<Person> findById(Integer id);
	Iterable<Person> findAll();
	void delete(Person entity);
	Iterable<Person> findAll(Sort sort);
	Optional<Person> findByPersonNumber(String personNumber);
}
