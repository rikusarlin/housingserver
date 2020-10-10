package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;

public interface IncomeRepository extends CrudRepository<IncomeEntity, Integer> {
	List<IncomeEntity> findByApplication(HousingBenefitApplicationEntity application);
	Optional<IncomeEntity> findByApplicationAndId(HousingBenefitApplicationEntity application, Integer id);
}
