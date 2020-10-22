package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.model.Income;

public interface IncomeRepository{
	Income save(Income entity, Integer caseId);
	Optional<Income> findById(Integer id);
	Iterable<Income> findAll();
	void delete(Integer id);
	Iterable<Income> findAll(Sort sort);
	List<Income> findByHousingBenefitCaseId(Integer caseId);
	Optional<Income> findByHousingBenefitCaseIdAndId(Integer caseId, Integer id);
}
