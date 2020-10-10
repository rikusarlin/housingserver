package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;

public interface HouseholdMemberRepository extends CrudRepository<HouseholdMemberEntity, Integer>, HousingBenefitApplicationRepositoryCustom {
	List<HouseholdMemberEntity> findByApplication(HousingBenefitApplicationEntity application);
	Optional<HouseholdMemberEntity> findByApplicationAndId(HousingBenefitApplicationEntity application, Integer id);
}
