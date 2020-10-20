package fi.rikusarlin.housingserver.repository.basic;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataJsonEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;

public interface HousingDataJpaRepository extends CrudRepository<HousingDataJsonEntity, Integer> {
	List<HousingDataJsonEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	List<HousingDataJsonEntity> findByHousingBenefitCaseAndHousingDataType(HousingBenefitCaseEntity housingBenefitCase, HousingDataType housingDataType);
	Optional<HousingDataJsonEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
