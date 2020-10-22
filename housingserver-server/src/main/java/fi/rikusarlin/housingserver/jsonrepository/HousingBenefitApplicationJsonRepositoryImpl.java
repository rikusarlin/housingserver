package fi.rikusarlin.housingserver.jsonrepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.json.HousingBenefitApplicationJsonEntity;
import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;

@Component("housingBenefitApplicationRepositoryJson")
public class HousingBenefitApplicationJsonRepositoryImpl implements HousingBenefitApplicationRepository {

	@Autowired
    private HousingBenefitApplicationJsonRepository jsonRepo;
	@Autowired
    private HousingBenefitCaseJsonRepository caseRepo;
    
	@Override
	public HousingBenefitApplication save(Integer caseId, HousingBenefitApplication hba) {
		Optional<HousingBenefitCaseJsonEntity> hbcje = caseRepo.findById(caseId);
    	HousingBenefitApplicationJsonEntity dataEntity = MappingUtil.modelMapper.map(hba, HousingBenefitApplicationJsonEntity.class);
    	dataEntity.setHousingBenefitCase(hbcje.get());
    	dataEntity.setHousingBenefitApplication(hba);
    	HousingBenefitApplicationJsonEntity savedEntity = jsonRepo.save(dataEntity);
    	hba.setId(savedEntity.getId());
    	return hba;
	}

	@Override
	public Optional<HousingBenefitApplication> findById(Integer id) {
    	Optional<HousingBenefitApplicationJsonEntity> hmje = jsonRepo.findById(id);
		if(hmje.isPresent()) {
			HousingBenefitApplication hba = hmje.get().getHousingBenefitApplication();
			hba.setId(hmje.get().getId());
			return Optional.of(hba);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<HousingBenefitApplication> findAll() {
		Iterable<HousingBenefitApplicationJsonEntity> hbajes = jsonRepo.findAll();
		return StreamSupport.stream(hbajes.spliterator(), false)
				.map(hbaje -> {
					HousingBenefitApplication hba = MappingUtil.modelMapper.map(hbaje.getHousingBenefitApplication(), HousingBenefitApplication.class);
					hba.setId(hbaje.getId());
					return hba;
				})
				.collect(Collectors.toList());
	}

	@Override
	public void delete(HousingBenefitApplication hba) {
    	HousingBenefitApplicationJsonEntity dataEntity = MappingUtil.modelMapper.map(hba, HousingBenefitApplicationJsonEntity.class);
    	dataEntity.getHousingBenefitCase().setApplication(null);
    	jsonRepo.delete(dataEntity);
	}

	@Override
	public Iterable<HousingBenefitApplication> findAll(Sort sort) {
		Iterable<HousingBenefitApplicationJsonEntity> hbajes = jsonRepo.findAll(sort);
		List<HousingBenefitApplication> hbaList = new ArrayList<HousingBenefitApplication>();
		for(HousingBenefitApplicationJsonEntity hbaje:hbajes) {
			HousingBenefitApplication hba = MappingUtil.modelMapper.map(hbaje.getHousingBenefitApplication(), HousingBenefitApplication.class);
			hba.setId(hbaje.getId());
			hbaList.add(hba);
		}
		return hbaList;
	}

	@Override
	public Optional<HousingBenefitApplication> findByHousingBenefitCaseId(Integer caseId) {
		Optional<HousingBenefitCaseJsonEntity> hbcje = caseRepo.findById(caseId);
		Optional<HousingBenefitApplicationJsonEntity> hbaje = jsonRepo.findByHousingBenefitCase(hbcje.get());
		if(hbaje.isPresent()) {
			HousingBenefitApplication hba = hbaje.get().getHousingBenefitApplication();
			hba.setId(hbaje.get().getId());
			return Optional.of(hba);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<HousingBenefitApplication> findByHousingBenefitCaseIdAndId(Integer caseId, Integer id) {
		Optional<HousingBenefitCaseJsonEntity> hbcje = caseRepo.findById(caseId);
		Optional<HousingBenefitApplicationJsonEntity> hbaje = jsonRepo.findByHousingBenefitCaseAndId(hbcje.get(), id);
		if(hbaje.isPresent()) {
			HousingBenefitApplication hba = hbaje.get().getHousingBenefitApplication();
			hba.setId(hbaje.get().getId());
			return Optional.of(hba);
		} else {
			return Optional.empty();
		}

	}
}
