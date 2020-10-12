package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
	List<ExpenseEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<ExpenseEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
