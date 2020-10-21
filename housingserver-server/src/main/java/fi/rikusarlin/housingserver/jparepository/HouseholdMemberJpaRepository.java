package fi.rikusarlin.housingserver.jparepository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface HouseholdMemberJpaRepository extends PagingAndSortingRepository<HouseholdMemberEntity, Integer> {
	Iterable<HouseholdMemberEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<HouseholdMemberEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
