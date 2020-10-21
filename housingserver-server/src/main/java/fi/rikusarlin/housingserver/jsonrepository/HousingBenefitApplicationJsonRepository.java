package fi.rikusarlin.housingserver.jsonrepository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitApplicationJsonEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;

public interface HousingBenefitApplicationJsonRepository extends PagingAndSortingRepository<HousingBenefitApplicationJsonEntity, Integer> {
	Optional<HousingBenefitApplicationJsonEntity> findByHousingBenefitCaseAndHousingDataType(HousingBenefitCaseEntity hbce, HousingDataType hdt);
	Optional<HousingBenefitApplicationJsonEntity> findByHousingBenefitCaseAndIdAndHousingDataType(HousingBenefitCaseEntity hbce, Integer id, HousingDataType hdt);
}
