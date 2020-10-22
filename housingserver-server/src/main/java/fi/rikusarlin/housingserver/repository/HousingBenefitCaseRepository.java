package fi.rikusarlin.housingserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import fi.rikusarlin.housingserver.model.HousingBenefitCase;

public interface HousingBenefitCaseRepository{
	HousingBenefitCase save(HousingBenefitCase hbc);
	Optional<HousingBenefitCase> findById(Integer id);
	Iterable<HousingBenefitCase> findAll();
	Iterable<HousingBenefitCase> findAllById(Iterable<Integer> ids);
	void delete(Integer id);
	Iterable<HousingBenefitCase> findAll(Sort sort);
	public List<Integer> findByPersonNumber(String personNumber);
}
