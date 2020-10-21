package fi.rikusarlin.housingserver.jparepository;

import java.util.List;

public interface CaseRepositoryCustom{
	public List<Integer> findByPersonNumber(String personNumber);
}
