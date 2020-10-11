package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.ExpenseEntity;

public interface ExpenseRepository extends CrudRepository<ExpenseEntity, Integer> {
	List<ExpenseEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<ExpenseEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
