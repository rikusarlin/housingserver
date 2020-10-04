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

import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.data.Expense;
import fi.rikusarlin.housingserver.data.HousingBenefitApplication;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.validation.ExpenseChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class ExpenseController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    ExpenseRepository expenseRepo;
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;

    @GetMapping("/api/v1/housing/{caseId}/expenses")
    public @ResponseBody Iterable<Expense> findExpenses(
    		@PathVariable int caseId) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
        return expenseRepo.findByApplication(hba);
    }
     
	@GetMapping(value = "/api/v1/housing/{caseId}/expense/{id}")
	public Expense findExpenseById(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		return expenseRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Expense", id));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/housing/{caseId}/expense")
	public Expense addExpense(
			@PathVariable int caseId,
			@RequestBody @Validated(InputChecks.class) Expense expense) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	expense.setApplication(hba);
		return expenseRepo.save(expense);
	}

	@GetMapping(value = "/api/v1/housing/{caseId}/expense/{id}/check")
	public Expense checkExpenseById(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Expense expense = expenseRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Expense", id));
		Set<ConstraintViolation<Expense>> violations =  validator.validate(expense, ExpenseChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return expense;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/api/v1/housing/{caseId}/expense/{id}")
	public Expense updateExpense(
			@PathVariable int caseId,
			@PathVariable int id,
			@RequestBody @Validated(InputChecks.class) Expense expense) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<Expense>> violations =  validator.validate(expense, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<Expense> previousExpense = expenseRepo.findByApplicationAndId(hba, id);
		previousExpense.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(expense.getEndDate());
				 		value.setStartDate(expense.getStartDate());
				 		value.setAmount(expense.getAmount());
				 		value.setExpenseType(expense.getExpenseType());
				 		value.setOtherExpenseDescription(expense.getOtherExpenseDescription());
						expenseRepo.save(value);
					},
				()
				 	-> {
				 		expenseRepo.save(expense);
				 	});
		return findExpenseById(caseId, id);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/api/v1/housing/{caseId}/expense/{id}")
	public void deleteExpense(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitApplication hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Expense expense = expenseRepo.findByApplicationAndId(hba,id).orElseThrow(() -> new NotFoundException("Expense", id));
 		expenseRepo.delete(expense);
	}

}