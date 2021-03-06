package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;

public interface IncomeRepository extends JpaRepository<IncomeEntity, Integer> {
	List<IncomeEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<IncomeEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
