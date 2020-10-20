package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.model.Expense;

public interface ExpenseRepository{
	Expense save(Expense entity, HousingBenefitCaseEntity hbc);
	Optional<Expense> findById(Integer id);
	Iterable<Expense> findAll();
	void delete(Expense entity, HousingBenefitCaseEntity hbc);
	Iterable<Expense> findAll(Sort sort);
	List<Expense> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<Expense> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
