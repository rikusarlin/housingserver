package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;

public interface ExpenseRepository{
	Expense save(Expense entity, HousingBenefitCase hbc);
	Optional<Expense> findById(Integer id);
	Iterable<Expense> findAll();
	void delete(Expense entity, HousingBenefitCase hbc);
	Iterable<Expense> findAll(Sort sort);
	List<Expense> findByHousingBenefitCase(HousingBenefitCase housingBenefitCase);
	Optional<Expense> findByHousingBenefitCaseAndId(HousingBenefitCase housingBenefitCase, Integer id);
}
