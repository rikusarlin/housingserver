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

import fi.rikusarlin.housingserver.data.HousingBenefitApplication;
import fi.rikusarlin.housingserver.data.Income;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.IncomeRepository;
import fi.rikusarlin.housingserver.validation.IncomeChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class IncomeController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    IncomeRepository incomeRepo;
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;

    @GetMapping("/api/v1/housing/{caseId}/incomes")
    public @ResponseBody Iterable<Income> findIncomes(
    		@PathVariable int caseId) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
        return incomeRepo.findByApplication(hba);
    }
     
	@GetMapping(value = "/api/v1/housing/{caseId}/income/{id}")
	public Income findIncomeById(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		return incomeRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Expense", id));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/housing/{caseId}/income")
	public Income addIncome(
			@PathVariable int caseId,
			@RequestBody @Validated(InputChecks.class) Income income) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	income.setApplication(hba);
		return incomeRepo.save(income);
	}

	@GetMapping(value = "/api/v1/housing/{caseId}/income/{id}/check")
	public Income checkIncomeById(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Income income = incomeRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Expense", id));
		Set<ConstraintViolation<Income>> violations =  validator.validate(income, IncomeChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return income;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/api/v1/housing/{caseId}/income/{id}")
	public Income updateIncome(
			@PathVariable int caseId,
			@PathVariable int id,
			@RequestBody @Validated(InputChecks.class) Income income) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<Income>> violations =  validator.validate(income, IncomeChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<Income> previousIncome = incomeRepo.findByApplicationAndId(hba, id);
		previousIncome.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(income.getEndDate());
				 		value.setStartDate(income.getStartDate());
				 		value.setAmount(income.getAmount());
				 		value.setIncomeType(income.getIncomeType());
				 		value.setOtherIncomeDescription(income.getOtherIncomeDescription());
						incomeRepo.save(value);
					},
				()
				 	-> {
				 		incomeRepo.save(income);
				 	});
		return findIncomeById(caseId, id);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/api/v1/housing/{caseId}/income/{id}")
	public void deleteIncome(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Income income = incomeRepo.findByApplicationAndId(hba,id).orElseThrow(() -> new NotFoundException("Expense", id));
 		incomeRepo.delete(income);
	}

}
