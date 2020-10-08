package fi.rikusarlin.housingserver.repository;

import java.util.List;

import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;

public interface HousingBenefitApplicationRepositoryCustom{
	public List<HousingBenefitApplicationEntity> findByPersonNumber(String personNumber);
}
