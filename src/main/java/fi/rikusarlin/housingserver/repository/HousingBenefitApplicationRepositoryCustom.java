package fi.rikusarlin.housingserver.repository;

import java.util.List;

import fi.rikusarlin.housingserver.data.HousingBenefitApplication;

public interface HousingBenefitApplicationRepositoryCustom{
	public List<HousingBenefitApplication> findByPersonNumber(String personNumber);
}
