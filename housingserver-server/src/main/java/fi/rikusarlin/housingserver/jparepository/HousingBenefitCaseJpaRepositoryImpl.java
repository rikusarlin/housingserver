package fi.rikusarlin.housingserver.jparepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.repository.HousingBenefitCaseRepository;

@Component("housingBenefitCaseRepositoryJpa")
public class HousingBenefitCaseJpaRepositoryImpl implements HousingBenefitCaseRepository {

    @Autowired
    private HousingBenefitCaseJpaRepository repo;

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
    public HousingBenefitCase save(HousingBenefitCase HousingBenefitCase) {
    	HousingBenefitCaseEntity hbce = MappingUtil.modelMapper.map(HousingBenefitCase, HousingBenefitCaseEntity.class);
        return MappingUtil.modelMapper.map(repo.save(hbce), HousingBenefitCase.class);
    }


	@Override
	public Optional<HousingBenefitCase> findById(Integer id) {
		Optional<HousingBenefitCaseEntity> HousingBenefitCase = repo.findById(id);
		if(HousingBenefitCase.isPresent()) {
			HousingBenefitCase e = MappingUtil.modelMapper.map(HousingBenefitCase.get(), HousingBenefitCase.class);
			return Optional.of(e);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<HousingBenefitCase> findAll() {
		Iterable<HousingBenefitCaseEntity> HousingBenefitCases = repo.findAll();
		return StreamSupport.stream(HousingBenefitCases.spliterator(), false)
				.map(HousingBenefitCase -> MappingUtil.modelMapper.map(HousingBenefitCase, HousingBenefitCase.class))
				.collect(Collectors.toList());
 	}


	@Override
	public void delete(Integer id) {
		repo.deleteById(id);
	}

	@Override
	public Iterable<HousingBenefitCase> findAll(Sort sort) {
		Iterable<HousingBenefitCaseEntity> hbces = repo.findAll(sort);
		// It might be better to do this using for-next loop rather than map-reduce to retain order
		List<HousingBenefitCase> hbcList = new ArrayList<HousingBenefitCase>();
		for(HousingBenefitCaseEntity hbce:hbces) {
			hbcList.add(MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class));
		}
		return hbcList;
	}
	@Override
	public Iterable<HousingBenefitCase> findAllById(Iterable<Integer> ids) {
		Iterable<HousingBenefitCaseEntity> HousingBenefitCases = repo.findAllById(ids);
		return StreamSupport.stream(HousingBenefitCases.spliterator(), false)
				.map(HousingBenefitCase -> MappingUtil.modelMapper.map(HousingBenefitCase, HousingBenefitCase.class))
				.collect(Collectors.toList());
	}

}