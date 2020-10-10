package fi.rikusarlin.housingserver.controllerimpl;

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

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
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
public class HousingBenefitApplicationControllerImpl {
	
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
    
    @GetMapping("/api/v1/housings")
    public @ResponseBody Iterable<HousingBenefitApplicationEntity> findHousingBenefitApplications() {
        return hbaRepo.findAll();
    }

    @GetMapping("/api/v1/housings/{personNumber}")
    public @ResponseBody Iterable<HousingBenefitApplicationEntity> findHousingBenefitApplicationsForPerson(
    		@PathVariable String personNumber) {
    	return hbaRepo.findByPersonNumber(personNumber);
    }

	@GetMapping(value = "/api/v1/housing/{caseId}")
	public HousingBenefitApplicationEntity findHousingBenefitApplicationById(
			@PathVariable int caseId) {
    	return hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/housing")
	public HousingBenefitApplicationEntity addHousingBenefitApplication(
			@RequestBody @Validated(InputChecks.class) HousingBenefitApplicationEntity hba) {
		
		HousingBenefitApplicationEntity hbaSaved = hbaRepo.save(hba);
		for(HouseholdMemberEntity hm:hba.getHouseholdMembers()) {
			PersonEntity p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
			hm.setApplication(hbaSaved);
			hm.setPerson(p);
			hmRepo.save(hm);
		}
		for(ExpenseEntity e:hba.getHousingExpenses()) {
			e.setApplication(hbaSaved);
			expenseRepo.save(e);
		}
		for(IncomeEntity i:hba.getIncomes()) {
			i.setApplication(hbaSaved);
			incomeRepo.save(i);
		}
		return hbaSaved;
	}

	@GetMapping(value = "/api/v1/housing/{caseId}/check")
	public HousingBenefitApplicationEntity checkHousingBenefitApplication(
			@PathVariable int caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hba, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return hba;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/api/v1/housing/{caseId}")
	public HousingBenefitApplicationEntity updateHousingBenefitApplication(
			@PathVariable int caseId,
			@RequestBody @Validated(InputChecks.class) HousingBenefitApplicationEntity hba) {
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hba, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HousingBenefitApplicationEntity> previousHba = hbaRepo.findById(caseId);
		previousHba.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(hba.getEndDate());
				 		value.setStartDate(hba.getStartDate());
				 		value.setHouseholdMembers(hba.getHouseholdMembers());
				 		value.setHousingExpenses(hba.getHousingExpenses());
				 		value.setIncomes(hba.getIncomes());
						value = hbaRepo.save(value);
						for(HouseholdMemberEntity hm:hba.getHouseholdMembers()) {
							PersonEntity p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
							hm.setApplication(value);
							hm.setPerson(p);
							hmRepo.save(hm);
						}
						for(ExpenseEntity e:hba.getHousingExpenses()) {
							e.setApplication(value);
							expenseRepo.save(e);
						}
						for(IncomeEntity i:hba.getIncomes()) {
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
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
 		hbaRepo.delete(hba);
	}

}
