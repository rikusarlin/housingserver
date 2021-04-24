package fi.rikusarlin.housingserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface HousingBenefitApplicationRepository extends 
	JpaRepository<HousingBenefitApplicationEntity, Integer> {
	Optional<HousingBenefitApplicationEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<HousingBenefitApplicationEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
