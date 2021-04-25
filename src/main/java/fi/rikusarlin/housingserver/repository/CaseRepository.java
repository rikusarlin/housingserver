package fi.rikusarlin.housingserver.repository;

import javax.enterprise.context.ApplicationScoped;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

@ApplicationScoped
public interface CaseRepository extends 
	JpaRepository<HousingBenefitCaseEntity, Integer>,
	CaseRepositoryCustom {
}
