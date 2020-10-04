package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fi.rikusarlin.housingserver.data.HousingBenefitApplication;
import fi.rikusarlin.housingserver.data.Income;

public interface IncomeRepository extends CrudRepository<Income, Integer> {
	List<Income> findByApplication(HousingBenefitApplication application);
	Optional<Income> findByApplicationAndId(HousingBenefitApplication application, Integer id);
}
