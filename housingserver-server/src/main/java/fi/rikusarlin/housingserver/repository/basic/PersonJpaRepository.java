package fi.rikusarlin.housingserver.repository.basic;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.PersonEntity;

public interface PersonJpaRepository extends PagingAndSortingRepository<PersonEntity, Integer> {
	Optional<PersonEntity> findByPersonNumber(String personNumber);
}
