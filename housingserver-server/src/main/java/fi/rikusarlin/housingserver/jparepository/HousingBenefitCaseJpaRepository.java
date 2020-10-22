package fi.rikusarlin.housingserver.jparepository;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface HousingBenefitCaseJpaRepository extends 
	PagingAndSortingRepository<HousingBenefitCaseEntity, Integer>{
}
