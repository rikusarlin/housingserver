package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.model.Expense;

public interface ExpenseRepository{
	Expense save(Expense entity, Integer caseId);
	Optional<Expense> findById(Integer id);
	Iterable<Expense> findAll();
	void delete(Integer id);
	Iterable<Expense> findAll(Sort sort);
	List<Expense> findByHousingBenefitCaseId(Integer caseId);
	Optional<Expense> findByHousingBenefitCaseIdAndId(Integer caseId, Integer id);
}
