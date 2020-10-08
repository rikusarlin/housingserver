package fi.rikusarlin.housingserver.repository;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;

public interface HousingBenefitApplicationRepository extends 
	CrudRepository<HousingBenefitApplicationEntity, Integer>,
	HousingBenefitApplicationRepositoryCustom {
}
