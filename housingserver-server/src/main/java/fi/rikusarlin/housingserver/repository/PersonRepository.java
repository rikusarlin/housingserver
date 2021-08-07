package fi.rikusarlin.housingserver.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.PersonEntity;
import reactor.core.publisher.Mono;

@Component
public interface PersonRepository extends ReactiveCrudRepository<PersonEntity, Integer> {
	Mono<PersonEntity> findByPersonNumber(Mono<String> personNumber);
}
