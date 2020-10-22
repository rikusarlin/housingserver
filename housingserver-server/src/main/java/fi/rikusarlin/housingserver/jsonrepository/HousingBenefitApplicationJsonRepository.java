package fi.rikusarlin.housingserver.jsonrepository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.json.HousingBenefitApplicationJsonEntity;
import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;

public interface HousingBenefitApplicationJsonRepository extends PagingAndSortingRepository<HousingBenefitApplicationJsonEntity, Integer> {
	Optional<HousingBenefitApplicationJsonEntity> findByHousingBenefitCase(HousingBenefitCaseJsonEntity hbce);
	Optional<HousingBenefitApplicationJsonEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseJsonEntity hbce, Integer id);
}
