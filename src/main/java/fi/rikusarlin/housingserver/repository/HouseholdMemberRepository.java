package fi.rikusarlin.housingserver.repository;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HouseholdMember;

public interface HouseholdMemberRepository extends CrudRepository<HouseholdMember, Integer> {
	HouseholdMember findByPersonNumber(String personNumber);
}
