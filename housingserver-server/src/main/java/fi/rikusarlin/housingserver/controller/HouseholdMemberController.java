package fi.rikusarlin.housingserver.controller;

import java.time.Period;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.exception.TooLongRangeException;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class HouseholdMemberController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    HouseholdMemberRepository householdMemberRepo;
    @Autowired
    CaseRepository caseRepo;
    @Autowired
    PersonRepository personRepo;

    @GetMapping("/api/v1/housing/{caseId}/householdmembers")
    public @ResponseBody Iterable<HouseholdMemberEntity> findHouseholdMembers(
    		@PathVariable int caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
        return householdMemberRepo.findByHousingBenefitCase(hbce);
    }
     
	@GetMapping(value = "/api/v1/housing/{caseId}/householdmember/{id}")
	public HouseholdMemberEntity findHouseholdMemberById(
			@PathVariable int caseId, 
			@PathVariable int id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		return householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/housing/{caseId}/householdmember")
	public HouseholdMemberEntity addHouseholdMember(
			@PathVariable int caseId,
			@RequestBody @Validated(InputChecks.class) HouseholdMemberEntity householdMember) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	PersonEntity p = personRepo.findById(householdMember.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", householdMember.getPerson().getId()));
    	householdMember.setHousingBenefitCase(hbce);
    	householdMember.setPerson(p);
		return householdMemberRepo.save(householdMember);
	}

	/**
	 * Cross validate a household member
	 * Note how Spring validation checks and manual checks are combined
	 */
	@GetMapping(value = "/api/v1/housing/{caseId}/householdmember/{id}/check")
	public HouseholdMemberEntity checkHouseholdMemberById(
			@PathVariable int caseId, 
			@PathVariable int id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		HouseholdMemberEntity hm = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
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
		return hm;
	}

	
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PutMapping("/api/v1/housing/{caseId}/householdmember/{id}")
	public HouseholdMemberEntity updateHouseholdMember(
			@PathVariable int caseId, 
			@PathVariable int id, 
			@RequestBody @Validated(InputChecks.class) HouseholdMemberEntity householdMember) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(householdMember, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HouseholdMemberEntity> hm = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id);
		hm.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(householdMember.getEndDate());
				 		value.setStartDate(householdMember.getStartDate());
						householdMemberRepo.save(value);
					},
				()
				 	-> {
				 		householdMemberRepo.save(householdMember);
				 	});
		return findHouseholdMemberById(caseId, id);
	}

	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/api/v1/housing/{caseId}/householdmember/{id}")
	public void deleteHouseholdMember(
			@PathVariable int caseId, 
			@PathVariable int id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HouseholdMemberEntity hm = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
 		householdMemberRepo.delete(hm);
	}
	
}
