package fi.rikusarlin.housingserver.controllerimpl;

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
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.HousingApi;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.exception.TooLongRangeException;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class HouseholdMemberControllerImpl implements HousingApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    HouseholdMemberRepository householdMemberRepo;
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;
    @Autowired
    PersonRepository personRepo;

    @Override
    public ResponseEntity<List<HouseholdMember>> fetchHouseholdMembers(Integer caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	Iterable<HouseholdMemberEntity> householdMembers = householdMemberRepo.findByApplication(hba);
    	return ResponseEntity.ok(
    			StreamSupport.stream(householdMembers.spliterator(), false)
    			.map(hm -> hm.toHouseholdMember())
    			.collect(Collectors.toList()));
    }
     
    @Override
	public ResponseEntity<HouseholdMember> fetchHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMemberEntity hme = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
		return ResponseEntity.ok(hme.toHouseholdMember());
	}
 
    @Override
	public ResponseEntity<HouseholdMember> addHouseholdMember(
			Integer caseId,
			HouseholdMember householdMember) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	PersonEntity p = personRepo.findById(householdMember.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", householdMember.getPerson().getId()));
    	HouseholdMemberEntity hme = new HouseholdMemberEntity(householdMember);
    	hme.setApplication(hba);
    	hme.setPerson(p);
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(householdMemberRepo.save(hme).toHouseholdMember());
	}

	/**
	 * Cross validate a household member
	 * Note how Spring validation checks and manual checks are combined
	 */
    @Override
	public ResponseEntity<HouseholdMember> checkHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMemberEntity hm = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hm, HouseholdChecks.class);
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
		return ResponseEntity.ok(hm.toHouseholdMember());
	}

	
    @Override
	public ResponseEntity<HouseholdMember> updateHouseholdMember(
			Integer caseId, 
			Integer id, 
			HouseholdMember householdMember) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMemberEntity hme = new HouseholdMemberEntity(householdMember);
    	Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HouseholdMemberEntity> hm = householdMemberRepo.findByApplicationAndId(hba, id);
		hm.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(householdMember.getEndDate());
				 		value.setStartDate(householdMember.getStartDate());
				 		value.setPerson(new PersonEntity(householdMember.getPerson()));
				 		value.setApplication(hba);
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
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	HouseholdMemberEntity hm = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
 		householdMemberRepo.delete(hm);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
