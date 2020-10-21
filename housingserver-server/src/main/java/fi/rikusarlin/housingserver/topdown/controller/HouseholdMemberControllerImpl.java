package fi.rikusarlin.housingserver.topdown.controller;

import java.time.Period;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.HouseholdmemberApi;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.exception.TooLongRangeException;
import fi.rikusarlin.housingserver.jparepository.CaseRepository;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class HouseholdMemberControllerImpl implements HouseholdmemberApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Autowired
    CaseRepository caseRepo;
    @Autowired
	@Qualifier("householdMemberRepositoryJson")
    HouseholdMemberRepository householdMemberRepo;
    @Autowired
	@Qualifier("personRepositoryJpa")
    PersonRepository personRepo;

    @Override
	public ResponseEntity<HouseholdMember> fetchHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		HouseholdMember hm = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
		return ResponseEntity.ok(hm);
	}
 
    @Override
	public ResponseEntity<HouseholdMember> addHouseholdMember(
			Integer caseId,
			HouseholdMember hm) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	Person p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
    	hm.setPerson(p);
    	PersonEntity pe = MappingUtil.modelMapper.map(p, PersonEntity.class);
    	HouseholdMemberEntity hme = MappingUtil.modelMapperInsert.map(hm,  HouseholdMemberEntity.class);
    	hme.setHousingBenefitCase(hbce);
    	hme.setPerson(pe);
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(householdMemberRepo.save(hm, hbce));
	}

	/**
	 * Cross validate a household member
	 * Note how Spring validation checks and manual checks are combined
	 */
    @Override
	public ResponseEntity<HouseholdMember> checkHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		HouseholdMember hm = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
    	personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
    	HouseholdMemberEntity hme = MappingUtil.modelMapperInsert.map(hm,  HouseholdMemberEntity.class);
    	Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, HouseholdChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		/*
		 * Period max one year in this case
		 * Yes, we could have written a validator for this, too, but wanted to show
		 * manual checks are added to Spring Validation checks
		 */
		if(hm.getStartDate() != null && hm.getEndDate() != null){
			Period  period = Period.between(hm.getStartDate(), hm.getEndDate());
			if(period.getYears() >= 1) {
				throw new TooLongRangeException(hm.getStartDate(), hm.getEndDate());
			}
		}
		return ResponseEntity.ok(hm);
	}

	
    @Override
	public ResponseEntity<HouseholdMember> updateHouseholdMember(
			Integer caseId, 
			Integer id, 
			HouseholdMember hm) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		HouseholdMemberEntity hme = MappingUtil.modelMapper.map(hm, HouseholdMemberEntity.class);
		Person person = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
    	hm.setPerson(person);
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		hm.setId(id);
		return ResponseEntity.ok(householdMemberRepo.save(hm, hbce));
	}

    @Override
	public ResponseEntity<Void> deleteHouseholdMember(
			Integer caseId, 
			Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HouseholdMember hm = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
 		householdMemberRepo.delete(hm, hbce);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
