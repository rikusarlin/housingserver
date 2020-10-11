package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface HouseholdMemberRepository extends CrudRepository<HouseholdMemberEntity, Integer> {
	List<HouseholdMemberEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<HouseholdMemberEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
