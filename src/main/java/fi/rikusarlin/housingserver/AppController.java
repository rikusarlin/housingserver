package fi.rikusarlin.housingserver;

import java.time.Period;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.data.HouseholdMember;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class AppController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    HouseholdMemberRepository householdMemberRepo;

    @GetMapping("/householdmembers")
    public @ResponseBody Iterable<HouseholdMember> findHouseholdMembers() {
        return householdMemberRepo.findAll();
    }
     
	@GetMapping(value = "/householdmember/{id}")
	public HouseholdMember findHouseholdMemberById(@Min(value=0) @PathVariable int id) {
		return householdMemberRepo.findById(id).orElseThrow(() -> new NotFoundException("Household member", id));
	}
 
	/**
	 * Just to demonstrate, this method makes some validations to input
	 * and another set of validations using Spring Validation.
	 * Then, a manual (rather nonsensical) check is made
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/householdmember")
	public HouseholdMember addHouseholdMember(@RequestBody @Validated(InputChecks.class) HouseholdMember householdMember) {
		Set<ConstraintViolation<HouseholdMember>> violations =  validator.validate(householdMember, HouseholdChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		// Period max one year in this case (yes, we could have written a validator for this, too)
		if(householdMember.getStartDate() != null && householdMember.getEndDate() != null){
			Period  period = Period.between(householdMember.getStartDate(),householdMember.getEndDate());
			if(period.getYears() >= 1) {
				throw new TooLongRangeException(householdMember.getStartDate(), householdMember.getEndDate());
			}
		}
		return householdMemberRepo.save(householdMember);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/householdmember/{id}")
	public HouseholdMember updateHouseholdMember(@Min(value=0) @PathVariable int id, @RequestBody @Validated(InputChecks.class) HouseholdMember householdMember) {
		Set<ConstraintViolation<HouseholdMember>> violations =  validator.validate(householdMember, HouseholdChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HouseholdMember> hm = householdMemberRepo.findById(id);
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
		// Well, by now we should always be able to find the updated version
		return householdMemberRepo.findById(id).orElseThrow(() -> new NotFoundException("Household member", id));
	}

}