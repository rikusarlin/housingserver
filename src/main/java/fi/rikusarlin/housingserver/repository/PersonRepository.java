package fi.rikusarlin.housingserver.repository;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
}
