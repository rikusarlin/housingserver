package fi.rikusarlin.housingserver.jsonrepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;
import fi.rikusarlin.housingserver.data.json.IncomeJsonEntity;

public interface IncomeJsonRepository extends PagingAndSortingRepository<IncomeJsonEntity, Integer> {
	List<IncomeJsonEntity> findByHousingBenefitCase(HousingBenefitCaseJsonEntity housingBenefitCase);
	Optional<IncomeJsonEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseJsonEntity housingBenefitCase, Integer id);
}
