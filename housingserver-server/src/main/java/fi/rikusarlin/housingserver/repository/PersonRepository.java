package fi.rikusarlin.housingserver.repository;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.PersonEntity;

public interface PersonRepository extends CrudRepository<PersonEntity, Integer> {
}
