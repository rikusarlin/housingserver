package fi.rikusarlin.housingserver.jparepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;

public interface IncomeJpaRepository extends PagingAndSortingRepository<IncomeEntity, Integer> {
	List<IncomeEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<IncomeEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
