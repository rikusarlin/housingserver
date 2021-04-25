package fi.rikusarlin.housingserver.repository;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.rikusarlin.housingserver.data.PersonEntity;

@ApplicationScoped
public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {
	Optional<PersonEntity> findByPersonNumber(String personNumber);
}
