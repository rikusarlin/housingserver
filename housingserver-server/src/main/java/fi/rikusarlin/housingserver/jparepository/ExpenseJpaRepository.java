package fi.rikusarlin.housingserver.jparepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface ExpenseJpaRepository extends PagingAndSortingRepository<ExpenseEntity, Integer> {
	List<ExpenseEntity> findByHousingBenefitCase(HousingBenefitCaseEntity hbce);
	Optional<ExpenseEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id);
}
