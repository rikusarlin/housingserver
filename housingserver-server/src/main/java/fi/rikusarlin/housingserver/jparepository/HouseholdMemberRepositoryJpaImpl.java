package fi.rikusarlin.housingserver.jparepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;

@Component("householdMemberRepositoryJpa")
public class HouseholdMemberRepositoryJpaImpl implements HouseholdMemberRepository {

    @Autowired
    private HouseholdMemberJpaRepository jpaRepo;

    public HouseholdMember save(HouseholdMember householdMember, HousingBenefitCaseEntity hbce) {
    	HouseholdMemberEntity hme = MappingUtil.modelMapper.map(householdMember, HouseholdMemberEntity.class);
    	hme.setHousingBenefitCase(hbce);
        return MappingUtil.modelMapper.map(jpaRepo.save(hme), HouseholdMember.class);
    }

	@Override
	public Optional<HouseholdMember> findById(Integer id) {
		Optional<HouseholdMemberEntity> householdMember = jpaRepo.findById(id);
		if(householdMember.isPresent()) {
			HouseholdMember hm = MappingUtil.modelMapper.map(householdMember.get(), HouseholdMember.class);
			return Optional.of(hm);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<HouseholdMember> findAll() {
		Iterable<HouseholdMemberEntity> householdMembers = jpaRepo.findAll();
		return StreamSupport.stream(householdMembers.spliterator(), false)
				.map(householdMember -> MappingUtil.modelMapper.map(householdMember, HouseholdMember.class))
				.collect(Collectors.toList());
 	}

	@Override
	public void delete(HouseholdMember HouseholdMember, HousingBenefitCaseEntity hbce) {
		HouseholdMemberEntity hme = MappingUtil.modelMapper.map(HouseholdMember, HouseholdMemberEntity.class);
		hme.setHousingBenefitCase(hbce);
		jpaRepo.delete(hme);
	}

	@Override
	public Iterable<HouseholdMember> findAll(Sort sort) {
		Iterable<HouseholdMemberEntity> householdMembers = jpaRepo.findAll(sort);
		// It might be better to do this using for-next loop rather than map-reduce to retain order
		List<HouseholdMember> householdMemberList = new ArrayList<HouseholdMember>();
		for(HouseholdMemberEntity hm:householdMembers) {
			householdMemberList.add(MappingUtil.modelMapper.map(hm, HouseholdMember.class));
		}
		return householdMemberList;
	}

	@Override
	public List<HouseholdMember> findByHousingBenefitCase(HousingBenefitCaseEntity hbce) {
		Iterable<HouseholdMemberEntity> householdMembers = jpaRepo.findByHousingBenefitCase(hbce);
		return StreamSupport.stream(householdMembers.spliterator(), false)
				.map(householdMember -> MappingUtil.modelMapper.map(householdMember, HouseholdMember.class))
				.collect(Collectors.toList());
	}

	@Override
	public Optional<HouseholdMember> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id) {
		Optional<HouseholdMemberEntity> householdMember = jpaRepo.findByHousingBenefitCaseAndId(hbce, id);
		if(householdMember.isPresent()) {
			HouseholdMember hm = MappingUtil.modelMapper.map(householdMember.get(), HouseholdMember.class);
			return Optional.of(hm);
		} else {
			return Optional.empty();
		}
	}
}