package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HouseholdMember;
import fi.rikusarlin.housingserver.data.HousingBenefitApplication;

public interface HouseholdMemberRepository extends CrudRepository<HouseholdMember, Integer>, HousingBenefitApplicationRepositoryCustom {
	List<HouseholdMember> findByApplication(HousingBenefitApplication application);
	Optional<HouseholdMember> findByApplicationAndId(HousingBenefitApplication application, Integer id);
}
