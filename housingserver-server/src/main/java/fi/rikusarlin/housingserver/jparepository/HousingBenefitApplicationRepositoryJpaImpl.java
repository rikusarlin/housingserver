package fi.rikusarlin.housingserver.jparepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;

@Component("housingBenefitApplicationRepositoryJpa")
public class HousingBenefitApplicationRepositoryJpaImpl implements HousingBenefitApplicationRepository {

    @Autowired
    private HousingBenefitApplicationJpaRepository jpaRepo;

    public HousingBenefitApplication save(HousingBenefitApplication hba, HousingBenefitCaseEntity hbce) {
    	HousingBenefitApplicationEntity hbae = MappingUtil.modelMapper.map(hba, HousingBenefitApplicationEntity.class);
    	hbae.setHousingBenefitCase(hbce);
        return MappingUtil.modelMapper.map(jpaRepo.save(hbae), HousingBenefitApplication.class);
    }

	@Override
	public Optional<HousingBenefitApplication> findById(Integer id) {
		Optional<HousingBenefitApplicationEntity> hbae = jpaRepo.findById(id);
		if(hbae.isPresent()) {
			HousingBenefitApplication hba = MappingUtil.modelMapper.map(hbae.get(), HousingBenefitApplication.class);
			return Optional.of(hba);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<HousingBenefitApplication> findAll() {
		Iterable<HousingBenefitApplicationEntity> hbaes = jpaRepo.findAll();
		return StreamSupport.stream(hbaes.spliterator(), false)
				.map(householdMember -> MappingUtil.modelMapper.map(householdMember, HousingBenefitApplication.class))
				.collect(Collectors.toList());
 	}

	@Override
	public void delete(HousingBenefitApplication HousingBenefitApplication, HousingBenefitCaseEntity hbce) {
		HousingBenefitApplicationEntity hbae = MappingUtil.modelMapper.map(HousingBenefitApplication, HousingBenefitApplicationEntity.class);
		hbae.setHousingBenefitCase(hbce);
		jpaRepo.delete(hbae);
	}

	@Override
	public Iterable<HousingBenefitApplication> findAll(Sort sort) {
		Iterable<HousingBenefitApplicationEntity> hbaes = jpaRepo.findAll(sort);
		List<HousingBenefitApplication> hbaList = new ArrayList<HousingBenefitApplication>();
		for(HousingBenefitApplicationEntity hbae:hbaes) {
			hbaList.add(MappingUtil.modelMapper.map(hbae, HousingBenefitApplication.class));
		}
		return hbaList;
	}

	@Override
	public Optional<HousingBenefitApplication> findByHousingBenefitCase(HousingBenefitCaseEntity hbce) {
		Optional<HousingBenefitApplicationEntity> hbae = jpaRepo.findByHousingBenefitCase(hbce);
		if(hbae.isPresent()) {
			HousingBenefitApplication hba = MappingUtil.modelMapper.map(hbae.get(), HousingBenefitApplication.class);
			return Optional.of(hba);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<HousingBenefitApplication> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id) {
		Optional<HousingBenefitApplicationEntity> hbae = jpaRepo.findByHousingBenefitCaseAndId(hbce, id);
		if(hbae.isPresent()) {
			HousingBenefitApplication hba = MappingUtil.modelMapper.map(hbae.get(), HousingBenefitApplication.class);
			return Optional.of(hba);
		} else {
			return Optional.empty();
		}
	}
}