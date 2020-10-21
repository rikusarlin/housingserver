package fi.rikusarlin.housingserver.jsonrepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.HouseholdMemberJsonEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;

public interface HouseholdMemberJsonRepository extends PagingAndSortingRepository<HouseholdMemberJsonEntity, Integer> {
	List<HouseholdMemberJsonEntity> findByHousingBenefitCaseAndHousingDataType(HousingBenefitCaseEntity hbce, HousingDataType housingDataType);
	Optional<HouseholdMemberJsonEntity> findByHousingBenefitCaseAndIdAndHousingDataType(HousingBenefitCaseEntity hbce, Integer id, HousingDataType hdt);
}
