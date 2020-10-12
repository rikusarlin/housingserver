package fi.rikusarlin.housingserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface CaseRepository extends 
	JpaRepository<HousingBenefitCaseEntity, Integer>,
	CaseRepositoryCustom {
}
