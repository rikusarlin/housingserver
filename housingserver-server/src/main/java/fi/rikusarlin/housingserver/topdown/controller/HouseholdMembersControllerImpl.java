package fi.rikusarlin.housingserver.topdown.controller;

import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.HouseholdmembersApi;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.exception.TooLongRangeException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
@RequestMapping("/api/v2/housing")
public class HouseholdMembersControllerImpl implements HouseholdmembersApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Autowired
    CaseRepository caseRepo;
    @Autowired
    HouseholdMemberRepository householdMemberRepo;
    @Autowired
    PersonRepository personRepo;

    @Override
	public ResponseEntity<HouseholdMember> fetchHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		HouseholdMemberEntity hme = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
		return ResponseEntity.ok(MappingUtil.modelMapper.map(hme, HouseholdMember.class));
	}
 
    @Override
	public ResponseEntity<HouseholdMember> addHouseholdMember(
			Integer caseId,
			HouseholdMember hm) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	PersonEntity pe = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
    	HouseholdMemberEntity hme = new HouseholdMemberEntity();
    	MappingUtil.modelMapperInsert.map(hm,  hme);
    	hme.setHousingBenefitCase(hbce);
    	hme.setPerson(pe);
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		hme.setId(null);
		return ResponseEntity.ok(MappingUtil.modelMapper.map(householdMemberRepo.save(hme), HouseholdMember.class));
	}

	/**
	 * Cross validate a household member
	 * Note how Spring validation checks and manual checks are combined
	 */
    @Override
	public ResponseEntity<HouseholdMember> checkHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		HouseholdMemberEntity hme = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, HouseholdChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		/*
		 * Period max one year in this case
		 * Yes, we could have written a validator for this, too, but wanted to show
		 * manual checks are added to Spring Validation checks
		 */
		if(hme.getStartDate() != null && hme.getEndDate() != null){
			Period  period = Period.between(hme.getStartDate(), hme.getEndDate());
			if(period.getYears() >= 1) {
				throw new TooLongRangeException(hme.getStartDate(), hme.getEndDate());
			}
		}
		return ResponseEntity.ok(MappingUtil.modelMapper.map(hme, HouseholdMember.class));
	}

	
    @Override
	public ResponseEntity<HouseholdMember> updateHouseholdMember(
			Integer caseId, 
			Integer id, 
			HouseholdMember hm) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		HouseholdMemberEntity hme = MappingUtil.modelMapper.map(hm, HouseholdMemberEntity.class);
    	Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HouseholdMemberEntity> previousHme = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id);
		previousHme.ifPresentOrElse(
				(value) 
					-> {
						MappingUtil.modelMapper.map(hm, value);
				 		value.setHousingBenefitCase(hbce);
						householdMemberRepo.save(value);
					},
				()
				 	-> {
				 		householdMemberRepo.save(hme);
				 	});
		return fetchHouseholdMemberById(caseId, id);
	}

    @Override
	public ResponseEntity<Void> deleteHouseholdMember(
			Integer caseId, 
			Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HouseholdMemberEntity hm = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
 		householdMemberRepo.delete(hm);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}
    
    @Override
    public ResponseEntity<List<HouseholdMember>> fetchHouseholdMembers(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	Iterable<HouseholdMemberEntity> householdMembers = householdMemberRepo.findByHousingBenefitCase(hbce);
    	return ResponseEntity.ok(
    			StreamSupport.stream(householdMembers.spliterator(), false)
    			.map(hm -> MappingUtil.modelMapper.map(hm, HouseholdMember.class))
    			.collect(Collectors.toList()));
    }
 }
