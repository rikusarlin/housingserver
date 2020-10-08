package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;

public interface ExpenseRepository extends CrudRepository<ExpenseEntity, Integer> {
	List<ExpenseEntity> findByApplication(HousingBenefitApplicationEntity application);
	Optional<ExpenseEntity> findByApplicationAndId(HousingBenefitApplicationEntity application, Integer id);
}
