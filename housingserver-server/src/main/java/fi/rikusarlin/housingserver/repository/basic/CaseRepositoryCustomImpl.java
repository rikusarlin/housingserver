package fi.rikusarlin.housingserver.repository.basic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class CaseRepositoryCustomImpl implements CaseRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    
	@Override
	public List<Integer> findByPersonNumber(String personNumber) {
	    TypedQuery<Integer> query 
	      = entityManager.createQuery(
	          "Select distinct c.id from HousingBenefitCaseEntity c "
	          + "join c.householdMembers hm "
	          + "join hm.person hmPerson "
	          + "where hmPerson.personNumber=:personNumber", 
	          Integer.class);
	    return query.setParameter("personNumber", personNumber).getResultList();
		
	}

}
