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
	          "Select * from HousingBenefitApplication hba  left join HouseholdMember hm on hba.id=hm.id left join Person p on hm.customer_id=p.id and p.personNumber=:personNumber", 
	          HousingBenefitApplication.class);
	    return query.getResultList();
		
	}

}
