package fi.rikusarlin.housingserver.jsonrepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.ExpenseJsonEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;

public interface ExpenseJsonRepository extends PagingAndSortingRepository<ExpenseJsonEntity, Integer> {
	List<ExpenseJsonEntity> findByHousingBenefitCaseAndHousingDataType(HousingBenefitCaseEntity housingBenefitCase, HousingDataType hdt);
	Optional<ExpenseJsonEntity> findByHousingBenefitCaseAndIdAndHousingDataType(HousingBenefitCaseEntity housingBenefitCase, Integer id, HousingDataType hdt);
}
