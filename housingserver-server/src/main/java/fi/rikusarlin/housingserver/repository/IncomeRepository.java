package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.model.Income;

public interface IncomeRepository{
	Income save(Income entity, HousingBenefitCaseEntity hbce);
	Optional<Income> findById(Integer id);
	Iterable<Income> findAll();
	void delete(Income entity, HousingBenefitCaseEntity hbce);
	Iterable<Income> findAll(Sort sort);
	List<Income> findByHousingBenefitCase(HousingBenefitCaseEntity hbce);
	Optional<Income> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id);
}
