package fi.rikusarlin.housingserver.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;

public class CaseRepositoryCustomImpl implements CaseRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    
	@Override
	public List<HousingBenefitCaseEntity> findByPersonNumber(String personNumber) {
	    TypedQuery<HousingBenefitCaseEntity> query 
	      = entityManager.createQuery(
	          "Select distinct c from HousingBenefitCaseEntity c "
	          + "join fetch c.householdMembers hm "
	          + "join fetch hm.person hmPerson "
	          + "where hmPerson.personNumber=:personNumber", 
	          HousingBenefitCaseEntity.class);
	    return query.setParameter("personNumber", personNumber).getResultList();
		
	}

}
