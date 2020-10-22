package fi.rikusarlin.housingserver.jsonrepository;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;

public interface HousingBenefitCaseJsonRepository extends 
	PagingAndSortingRepository<HousingBenefitCaseJsonEntity, Integer> {
}
