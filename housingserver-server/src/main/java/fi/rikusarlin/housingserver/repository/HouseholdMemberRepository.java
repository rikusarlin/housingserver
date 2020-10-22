package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.model.HouseholdMember;

public interface HouseholdMemberRepository{
	HouseholdMember save(HouseholdMember entity, Integer caseId);
	Optional<HouseholdMember> findById(Integer id);
	Iterable<HouseholdMember> findAll();
	void delete(Integer id);
	Iterable<HouseholdMember> findAll(Sort sort);
	List<HouseholdMember> findByHousingBenefitCaseId(Integer caseId);
	Optional<HouseholdMember> findByHousingBenefitCaseIdAndId(Integer caseId, Integer id);
}
