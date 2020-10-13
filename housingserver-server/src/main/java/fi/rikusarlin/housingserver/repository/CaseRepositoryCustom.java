package fi.rikusarlin.housingserver.repository;

import java.util.List;

public interface CaseRepositoryCustom{
	public List<Integer> findByPersonNumber(String personNumber);
}
