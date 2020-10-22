package fi.rikusarlin.housingserver.jsonrepository;

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

import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.repository.HousingBenefitCaseRepository;

@Component("housingBenefitCaseRepositoryJson")
public class HousingBenefitCaseJsonRepositoryImpl implements HousingBenefitCaseRepository {

	@Autowired
    private HousingBenefitCaseJsonRepository hbcJsonRepo;
    
    @PersistenceContext
    private EntityManager entityManager;
    
	@Override
	public List<Integer> findByPersonNumber(String personNumber) {
	    TypedQuery<Integer> query 
	      = entityManager.createQuery(
	          "Select distinct c.id from HousingBenefitCaseJsonEntity c "
	          + "join c.householdMembers hm "
	          + "join hm.person hmPerson "
	          + "where hmPerson.personNumber=:personNumber", 
	          Integer.class);
	    return query.setParameter("personNumber", personNumber).getResultList();
		
	}

	@Override
	public HousingBenefitCase save(HousingBenefitCase hbc) {
    	HousingBenefitCaseJsonEntity dataEntity = MappingUtil.modelMapper.map(hbc, HousingBenefitCaseJsonEntity.class);
    	HousingBenefitCaseJsonEntity savedEntity = hbcJsonRepo.save(dataEntity);
    	hbc.setId(savedEntity.getId());
    	return hbc;
	}

	@Override
	public Optional<HousingBenefitCase> findById(Integer id) {
    	Optional<HousingBenefitCaseJsonEntity> hbcje = hbcJsonRepo.findById(id);
		if(hbcje.isPresent()) {
			return Optional.of(MappingUtil.modelMapper.map(hbcje.get(), HousingBenefitCase.class));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<HousingBenefitCase> findAll() {
		Iterable<HousingBenefitCaseJsonEntity> hbcjes = hbcJsonRepo.findAll();
		return StreamSupport.stream(hbcjes.spliterator(), false)
				.map(hbcje -> MappingUtil.modelMapper.map(hbcje, HousingBenefitCase.class))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) {
    	hbcJsonRepo.deleteById(id);
	}

	@Override
	public Iterable<HousingBenefitCase> findAll(Sort sort) {
		Iterable<HousingBenefitCaseJsonEntity> hbcjes = hbcJsonRepo.findAll(sort);
		List<HousingBenefitCase> hbcList = new ArrayList<HousingBenefitCase>();
		for(HousingBenefitCaseJsonEntity hbcje:hbcjes) {
			HousingBenefitCase hbc = MappingUtil.modelMapper.map(hbcje, HousingBenefitCase.class);
			hbcList.add(hbc);
		}
		return hbcList;
	}

	@Override
	public Iterable<HousingBenefitCase> findAllById(Iterable<Integer> ids) {
		Iterable<HousingBenefitCaseJsonEntity> hbcjes = hbcJsonRepo.findAllById(ids);
		return StreamSupport.stream(hbcjes.spliterator(), false)
				.map(hbcje -> MappingUtil.modelMapper.map(hbcje, HousingBenefitCase.class))
				.collect(Collectors.toList());
	}
}
