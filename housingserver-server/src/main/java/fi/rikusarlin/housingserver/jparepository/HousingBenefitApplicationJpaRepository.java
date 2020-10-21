package fi.rikusarlin.housingserver.jparepository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface HousingBenefitApplicationJpaRepository extends 
	PagingAndSortingRepository<HousingBenefitApplicationEntity, Integer> {
	Optional<HousingBenefitApplicationEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<HousingBenefitApplicationEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
