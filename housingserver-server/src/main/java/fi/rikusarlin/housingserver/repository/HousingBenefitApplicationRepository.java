package fi.rikusarlin.housingserver.repository;

import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;

public interface HousingBenefitApplicationRepository{
	HousingBenefitApplication save(HousingBenefitApplication entity, HousingBenefitCaseEntity hbce);
	Optional<HousingBenefitApplication> findById(Integer id);
	Iterable<HousingBenefitApplication> findAll();
	void delete(HousingBenefitApplication entity, HousingBenefitCaseEntity hbce);
	Iterable<HousingBenefitApplication> findAll(Sort sort);
	Optional<HousingBenefitApplication> findByHousingBenefitCase(HousingBenefitCaseEntity hbce);
	Optional<HousingBenefitApplication> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id);
}
