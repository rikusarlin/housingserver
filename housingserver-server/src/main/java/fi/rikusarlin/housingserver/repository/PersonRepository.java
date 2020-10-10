package fi.rikusarlin.housingserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.PersonEntity;

public interface PersonRepository extends CrudRepository<PersonEntity, Integer> {
	Optional<PersonEntity> findByPersonNumber(String personNumber);
}
