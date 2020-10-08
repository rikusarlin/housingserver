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

import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
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
    public @ResponseBody Iterable<IncomeEntity> findIncomes(
    		@PathVariable int caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
        return incomeRepo.findByApplication(hba);
    }
     
	@GetMapping(value = "/api/v1/housing/{caseId}/income/{id}")
	public IncomeEntity findIncomeById(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		return incomeRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Income", id));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/housing/{caseId}/income")
	public IncomeEntity addIncome(
			@PathVariable int caseId,
			@RequestBody @Validated(InputChecks.class) IncomeEntity income) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	income.setApplication(hba);
		return incomeRepo.save(income);
	}

	@GetMapping(value = "/api/v1/housing/{caseId}/income/{id}/check")
	public IncomeEntity checkIncomeById(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity income = incomeRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Income", id));
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(income, IncomeChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return income;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/api/v1/housing/{caseId}/income/{id}")
	public IncomeEntity updateIncome(
			@PathVariable int caseId,
			@PathVariable int id,
			@RequestBody @Validated(InputChecks.class) IncomeEntity income) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(income, IncomeChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<IncomeEntity> previousIncome = incomeRepo.findByApplicationAndId(hba, id);
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
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity income = incomeRepo.findByApplicationAndId(hba,id).orElseThrow(() -> new NotFoundException("Income", id));
 		incomeRepo.delete(income);
	}

}
