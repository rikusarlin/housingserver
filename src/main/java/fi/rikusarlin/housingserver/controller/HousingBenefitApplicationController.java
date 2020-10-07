package fi.rikusarlin.housingserver.controller;

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

import fi.rikusarlin.housingserver.data.Expense;
import fi.rikusarlin.housingserver.data.HouseholdMember;
import fi.rikusarlin.housingserver.data.HousingBenefitApplication;
import fi.rikusarlin.housingserver.data.Income;
import fi.rikusarlin.housingserver.data.Person;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.IncomeRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class HousingBenefitApplicationController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    HousingBenefitApplicationRepository hbaRepo;
    @Autowired
    HouseholdMemberRepository hmRepo;
    @Autowired
    IncomeRepository incomeRepo;
    @Autowired
    ExpenseRepository expenseRepo;
    @Autowired
    PersonRepository personRepo;
    
    @GetMapping("/api/v1/housing")
    public @ResponseBody Iterable<HousingBenefitApplication> findHousingBenefitApplications() {
        return hbaRepo.findAll();
    }

    @GetMapping("/api/v1/housing/person/{personNumber}")
    public @ResponseBody Iterable<HousingBenefitApplication> findHousingBenefitApplicationsForPerson(
    		@PathVariable String personNumber) {
    	return hbaRepo.findByPersonNumber(personNumber);
    }

	@GetMapping(value = "/api/v1/housing/{caseId}")
	public HousingBenefitApplication findHousingBenefitApplicationById(
			@PathVariable int caseId) {
    	return hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/housing")
	public HousingBenefitApplication addHousingBenefitApplication(
			@RequestBody @Validated(InputChecks.class) HousingBenefitApplication hba) {
		
		HousingBenefitApplication hbaSaved = hbaRepo.save(hba);
		for(HouseholdMember hm:hba.getHouseholdMembers()) {
			Person p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
			hm.setApplication(hbaSaved);
			hm.setPerson(p);
			hmRepo.save(hm);
		}
		for(Expense e:hba.getHousingExpenses()) {
			e.setApplication(hbaSaved);
			expenseRepo.save(e);
		}
		for(Income i:hba.getIncomes()) {
			i.setApplication(hbaSaved);
			incomeRepo.save(i);
		}
		return hbaSaved;
	}

	@GetMapping(value = "/api/v1/housing/{caseId}/check")
	public HousingBenefitApplication checkHousingBenefitApplication(
			@PathVariable int caseId) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<HousingBenefitApplication>> violations =  validator.validate(hba, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return hba;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/api/v1/housing/{caseId}")
	public HousingBenefitApplication updateHousingBenefitApplication(
			@PathVariable int caseId,
			@RequestBody @Validated(InputChecks.class) HousingBenefitApplication hba) {
		Set<ConstraintViolation<HousingBenefitApplication>> violations =  validator.validate(hba, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HousingBenefitApplication> previousHba = hbaRepo.findById(caseId);
		previousHba.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(hba.getEndDate());
				 		value.setStartDate(hba.getStartDate());
				 		value.setHouseholdMembers(hba.getHouseholdMembers());
				 		value.setHousingExpenses(hba.getHousingExpenses());
				 		value.setIncomes(hba.getIncomes());
						value = hbaRepo.save(value);
						for(HouseholdMember hm:hba.getHouseholdMembers()) {
							Person p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
							hm.setApplication(value);
							hm.setPerson(p);
							hmRepo.save(hm);
						}
						for(Expense e:hba.getHousingExpenses()) {
							e.setApplication(value);
							expenseRepo.save(e);
						}
						for(Income i:hba.getIncomes()) {
							i.setApplication(value);
							incomeRepo.save(i);
						}
					},
				()
				 	-> {
				 		hbaRepo.save(hba);
				 	});
		return findHousingBenefitApplicationById(caseId);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/api/v1/housing/{caseId}")
	public void deleteHousingBenefitApplication(
			@PathVariable int caseId) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
 		hbaRepo.delete(hba);
	}

}
