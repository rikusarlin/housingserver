package fi.rikusarlin.housingserver.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;

public class HousingBenefitApplicationRepositoryCustomImpl implements HousingBenefitApplicationRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    
	@Override
	public List<HousingBenefitApplicationEntity> findByPersonNumber(String personNumber) {
	    TypedQuery<HousingBenefitApplicationEntity> query 
	      = entityManager.createQuery(
	          "Select distinct hba from HousingBenefitApplication hba "
	          + "join fetch hba.householdMembers hm "
	          + "join fetch hba.housingExpenses e "
	          + "join fetch hba.incomes i "
	          + "join fetch hm.person p "
	          + "where p.personNumber=:personNumber", 
	          HousingBenefitApplicationEntity.class);
	    return query.setParameter("personNumber", personNumber).getResultList();
		
	}

}
