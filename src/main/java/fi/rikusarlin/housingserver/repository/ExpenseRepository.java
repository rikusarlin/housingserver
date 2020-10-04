package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.Expense;
import fi.rikusarlin.housingserver.data.HousingBenefitApplication;

public interface ExpenseRepository extends CrudRepository<Expense, Integer> {
	List<Expense> findByApplication(HousingBenefitApplication application);
	Optional<Expense> findByApplicationAndId(HousingBenefitApplication application, Integer id);
}
