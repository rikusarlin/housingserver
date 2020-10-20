package fi.rikusarlin.housingserver.repository.basic;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface CaseRepository extends 
	CrudRepository<HousingBenefitCaseEntity, Integer>,
	CaseRepositoryCustom {
}
