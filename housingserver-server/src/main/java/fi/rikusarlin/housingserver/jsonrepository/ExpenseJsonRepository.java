package fi.rikusarlin.housingserver.jsonrepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.json.ExpenseJsonEntity;
import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;

public interface ExpenseJsonRepository extends PagingAndSortingRepository<ExpenseJsonEntity, Integer> {
	List<ExpenseJsonEntity> findByHousingBenefitCase(HousingBenefitCaseJsonEntity hbcje);
	Optional<ExpenseJsonEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseJsonEntity hbcje, Integer id);
}
