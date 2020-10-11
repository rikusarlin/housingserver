package fi.rikusarlin.housingserver.repository;

import java.util.List;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public interface CaseRepositoryCustom{
	public List<HousingBenefitCaseEntity> findByPersonNumber(String personNumber);
}
