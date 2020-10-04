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

import fi.rikusarlin.housingserver.data.HouseholdMember;
import fi.rikusarlin.housingserver.data.HousingBenefitApplication;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.exception.TooLongRangeException;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class HouseholdMemberController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    HouseholdMemberRepository householdMemberRepo;
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;

    @GetMapping("/api/v1/housing/{caseId}/householdmembers")
    public @ResponseBody Iterable<HouseholdMember> findHouseholdMembers(
    		@PathVariable int caseId) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
        return householdMemberRepo.findByApplication(hba);
    }
     
	@GetMapping(value = "/api/v1/housing/{caseId}/householdmember/{id}")
	public HouseholdMember findHouseholdMemberById(
			@PathVariable int caseId, 
			@PathVariable int id) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		return householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/housing/{caseId}/householdmember")
	public HouseholdMember addHouseholdMember(
			@PathVariable int caseId, 
			@RequestBody @Validated(InputChecks.class) HouseholdMember householdMember) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	householdMember.setApplication(hba);
		return householdMemberRepo.save(householdMember);
	}

	/**
	 * Cross validate a household member
	 * Note how Spring validation checks and manual checks are combined
	 */
	@GetMapping(value = "/api/v1/housing/{caseId}/householdmember/{id}/check")
	public HouseholdMember checkHouseholdMemberById(
			@PathVariable int caseId, 
			@PathVariable int id) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMember hm = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
		Set<ConstraintViolation<HouseholdMember>> violations =  validator.validate(hm, HouseholdChecks.class);
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
	public HouseholdMember updateHouseholdMember(
			@PathVariable int caseId, 
			@PathVariable int id, 
			@RequestBody @Validated(InputChecks.class) HouseholdMember householdMember) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<HouseholdMember>> violations =  validator.validate(householdMember, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HouseholdMember> hm = householdMemberRepo.findByApplicationAndId(hba, id);
		hm.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(householdMember.getEndDate());
				 		value.setStartDate(householdMember.getStartDate());
				 		value.setPersonNumber(householdMember.getPersonNumber());
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
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	HouseholdMember hm = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
 		householdMemberRepo.delete(hm);
	}
	
}
