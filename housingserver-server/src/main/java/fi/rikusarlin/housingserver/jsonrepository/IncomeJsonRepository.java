package fi.rikusarlin.housingserver.jsonrepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;
import fi.rikusarlin.housingserver.data.IncomeJsonEntity;

public interface IncomeJsonRepository extends PagingAndSortingRepository<IncomeJsonEntity, Integer> {
	List<IncomeJsonEntity> findByHousingBenefitCaseAndHousingDataType(HousingBenefitCaseEntity housingBenefitCase, HousingDataType hdt);
	Optional<IncomeJsonEntity> findByHousingBenefitCaseAndIdAndHousingDataType(HousingBenefitCaseEntity housingBenefitCase, Integer id, HousingDataType hdt);
}
