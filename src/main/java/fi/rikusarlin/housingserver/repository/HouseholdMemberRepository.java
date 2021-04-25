package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

@ApplicationScoped
public interface HouseholdMemberRepository extends JpaRepository<HouseholdMemberEntity, Integer> {
	List<HouseholdMemberEntity> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase);
	Optional<HouseholdMemberEntity> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity housingBenefitCase, Integer id);
}
