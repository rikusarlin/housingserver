package fi.rikusarlin.housingserver.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import fi.rikusarlin.housingserver.data.HousingBenefitApplication;

public class HousingBenefitApplicationRepositoryCustomImpl implements HousingBenefitApplicationRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    
	@Override
	public List<HousingBenefitApplication> findByPersonNumber(String personNumber) {
	    TypedQuery<HousingBenefitApplication> query 
	      = entityManager.createQuery(
	          "Select distinct hba from HousingBenefitApplication hba "
	          + "join fetch hba.householdMembers hm "
	          + "join fetch hba.housingExpenses e "
	          + "join fetch hba.incomes i "
	          + "join fetch hm.person p "
	          + "where p.personNumber=:personNumber", 
	          HousingBenefitApplication.class);
	    return query.setParameter("personNumber", personNumber).getResultList();
		
	}

}
