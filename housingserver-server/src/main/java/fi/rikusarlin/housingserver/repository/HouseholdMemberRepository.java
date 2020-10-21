package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.model.HouseholdMember;

public interface HouseholdMemberRepository{
	HouseholdMember save(HouseholdMember entity, HousingBenefitCaseEntity hbce);
	Optional<HouseholdMember> findById(Integer id);
	Iterable<HouseholdMember> findAll();
	void delete(HouseholdMember entity, HousingBenefitCaseEntity hbce);
	Iterable<HouseholdMember> findAll(Sort sort);
	List<HouseholdMember> findByHousingBenefitCase(HousingBenefitCaseEntity hbce);
	Optional<HouseholdMember> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id);
}
