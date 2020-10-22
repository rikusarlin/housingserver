package fi.rikusarlin.housingserver.jsonrepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import fi.rikusarlin.housingserver.data.json.HouseholdMemberJsonEntity;
import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;

public interface HouseholdMemberJsonRepository extends PagingAndSortingRepository<HouseholdMemberJsonEntity, Integer> {
	List<HouseholdMemberJsonEntity> findByHousingBenefitCase(HousingBenefitCaseJsonEntity hbcje);
	Optional<HouseholdMemberJsonEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseJsonEntity hbcje, Integer id);
}
