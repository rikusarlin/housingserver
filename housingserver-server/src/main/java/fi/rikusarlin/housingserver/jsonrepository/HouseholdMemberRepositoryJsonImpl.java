package fi.rikusarlin.housingserver.jsonrepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.HouseholdMemberJsonEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;

@Component("householdMemberRepositoryJson")
public class HouseholdMemberRepositoryJsonImpl implements HouseholdMemberRepository {

	@Autowired
    private HouseholdMemberJsonRepository jsonRepo;
    
	@Override
	public HouseholdMember save(HouseholdMember hm, HousingBenefitCaseEntity hbce) {
    	HouseholdMemberJsonEntity dataEntity = MappingUtil.modelMapper.map(hm, HouseholdMemberJsonEntity.class);
    	dataEntity.setHousingBenefitCase(hbce);
    	dataEntity.setHousingDataType(HousingDataType.HOUSEHOLDMEMBER);
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
	public void delete(HouseholdMember hm, HousingBenefitCaseEntity hbce) {
    	HouseholdMemberJsonEntity dataEntity = MappingUtil.modelMapper.map(hm, HouseholdMemberJsonEntity.class);
    	dataEntity.setHousingBenefitCase(hbce);
    	dataEntity.setHousingDataType(HousingDataType.HOUSEHOLDMEMBER);
    	dataEntity.setHouseholdMember(hm);
    	jsonRepo.delete(dataEntity);
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
	public List<HouseholdMember> findByHousingBenefitCase(HousingBenefitCaseEntity hbce) {
		Iterable<HouseholdMemberJsonEntity> hmjes = jsonRepo.findByHousingBenefitCaseAndHousingDataType(hbce, HousingDataType.HOUSEHOLDMEMBER);
		return StreamSupport.stream(hmjes.spliterator(), false)
				.map(hmje -> {
					HouseholdMember hm = MappingUtil.modelMapper.map(hmje.getHouseholdMember(), HouseholdMember.class);
					hm.setId(hmje.getId());
					return hm;
				})
				.collect(Collectors.toList());
	}

	@Override
	public Optional<HouseholdMember> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id) {
		Optional<HouseholdMemberJsonEntity> hmje = jsonRepo.findByHousingBenefitCaseAndIdAndHousingDataType(hbce, id, HousingDataType.HOUSEHOLDMEMBER);
		if(hmje.isPresent()) {
			HouseholdMember hm = hmje.get().getHouseholdMember();
			hm.setId(hmje.get().getId());
			return Optional.of(hm);
		} else {
			return Optional.empty();
		}

	}

}
