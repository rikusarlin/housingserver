package fi.rikusarlin.housingserver.repository.basic;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.ExpenseJsonEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;

public interface HousingDataJpaRepository extends PagingAndSortingRepository<ExpenseJsonEntity, Integer> {
	List<ExpenseJsonEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	List<ExpenseJsonEntity> findByHousingBenefitCaseAndHousingDataType(HousingBenefitCaseEntity housingBenefitCase, HousingDataType housingDataType);
	Optional<ExpenseJsonEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
