package fi.rikusarlin.housingserver.repository.basic;

import java.util.List;

public interface CaseRepositoryCustom{
	public List<Integer> findByPersonNumber(String personNumber);
}
