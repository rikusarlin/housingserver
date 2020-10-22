package fi.rikusarlin.housingserver.repository;

import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.model.HousingBenefitApplication;

public interface HousingBenefitApplicationRepository{
	HousingBenefitApplication save(Integer caseId, HousingBenefitApplication hba);
	Optional<HousingBenefitApplication> findById(Integer id);
	Iterable<HousingBenefitApplication> findAll();
	void delete(HousingBenefitApplication entity);
	Iterable<HousingBenefitApplication> findAll(Sort sort);
	Optional<HousingBenefitApplication> findByHousingBenefitCaseId(Integer caseId);
	Optional<HousingBenefitApplication> findByHousingBenefitCaseIdAndId(Integer caseId, Integer id);
}
