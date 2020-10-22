package fi.rikusarlin.housingserver.jsonrepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.json.HouseholdMemberJsonEntity;
import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;

@Component("householdMemberRepositoryJson")
public class HouseholdMemberJsonRepositoryImpl implements HouseholdMemberRepository {

	@Autowired
    private HouseholdMemberJsonRepository jsonRepo;
	@Autowired
    private HousingBenefitCaseJsonRepository caseRepo;
    
	@Override
	public HouseholdMember save(HouseholdMember hm, Integer caseId) {
       	HousingBenefitCaseJsonEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HouseholdMemberJsonEntity dataEntity = MappingUtil.modelMapper.map(hm, HouseholdMemberJsonEntity.class);
    	dataEntity.setHousingBenefitCase(hbce);
    	dataEntity.setHouseholdMember(hm);
    	HouseholdMemberJsonEntity savedEntity = jsonRepo.save(dataEntity);
    	hm.setId(savedEntity.getId());
    	return hm;
	}

	@Override
	public Optional<HouseholdMember> findById(Integer id) {
    	Optional<HouseholdMemberJsonEntity> hmje = jsonRepo.findById(id);
		if(hmje.isPresent()) {
			HouseholdMember e = hmje.get().getHouseholdMember();
			e.setId(hmje.get().getId());
			return Optional.of(e);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<HouseholdMember> findAll() {
		Iterable<HouseholdMemberJsonEntity> hmjes = jsonRepo.findAll();
		return StreamSupport.stream(hmjes.spliterator(), false)
				.map(hmje -> {
					HouseholdMember hm = MappingUtil.modelMapper.map(hmje.getHouseholdMember(), HouseholdMember.class);
					hm.setId(hmje.getId());
					return hm;
				})
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) {
		HouseholdMemberJsonEntity hmje = jsonRepo.findById(id).orElseThrow(() -> new NotFoundException("Household member", id));
    	hmje.getHousingBenefitCase().getHouseholdMembers().remove(hmje);
		jsonRepo.delete(hmje);
	}

	@Override
	public Iterable<HouseholdMember> findAll(Sort sort) {
		Iterable<HouseholdMemberJsonEntity> hmjes = jsonRepo.findAll(sort);
		List<HouseholdMember> hmList = new ArrayList<HouseholdMember>();
		for(HouseholdMemberJsonEntity hmje:hmjes) {
			HouseholdMember hm = MappingUtil.modelMapper.map(hmje.getHouseholdMember(), HouseholdMember.class);
			hm.setId(hmje.getId());
			hmList.add(hm);
		}
		return hmList;
	}

	@Override
	public List<HouseholdMember> findByHousingBenefitCaseId(Integer caseId) {
       	HousingBenefitCaseJsonEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Iterable<HouseholdMemberJsonEntity> hmjes = jsonRepo.findByHousingBenefitCase(hbce);
		return StreamSupport.stream(hmjes.spliterator(), false)
				.map(hmje -> {
					HouseholdMember hm = MappingUtil.modelMapper.map(hmje.getHouseholdMember(), HouseholdMember.class);
					hm.setId(hmje.getId());
					return hm;
				})
				.collect(Collectors.toList());
	}

	@Override
	public Optional<HouseholdMember> findByHousingBenefitCaseIdAndId(Integer caseId, Integer id) {
       	HousingBenefitCaseJsonEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Optional<HouseholdMemberJsonEntity> hmje = jsonRepo.findByHousingBenefitCaseAndId(hbce, id);
		if(hmje.isPresent()) {
			HouseholdMember hm = hmje.get().getHouseholdMember();
			hm.setId(hmje.get().getId());
			return Optional.of(hm);
		} else {
			return Optional.empty();
		}
	}
}
