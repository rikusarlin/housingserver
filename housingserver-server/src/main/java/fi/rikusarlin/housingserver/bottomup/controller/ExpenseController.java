package fi.rikusarlin.housingserver.bottomup.controller;

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
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.validation.ExpenseChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class ExpenseController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    ExpenseRepository expenseRepo;
    @Autowired
    CaseRepository caseRepo;

    @GetMapping("/api/v1/housing/{caseId}/expenses")
    public @ResponseBody Iterable<ExpenseEntity> findExpenses(
    		@PathVariable int caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
        return expenseRepo.findByHousingBenefitCase(hbce);
    }
     
	@GetMapping(value = "/api/v1/housing/{caseId}/expense/{id}")
	public ExpenseEntity findExpenseById(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		return expenseRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Expense", id));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/api/v1/housing/{caseId}/expense")
	public ExpenseEntity addExpense(
			@PathVariable int caseId,
			@RequestBody @Validated(InputChecks.class) ExpenseEntity expense) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	expense.setHousingBenefitCase(hbce);
		return expenseRepo.save(expense);
	}

	@GetMapping(value = "/api/v1/housing/{caseId}/expense/{id}/check")
	public ExpenseEntity checkExpenseById(
			@PathVariable int caseId,
			@PathVariable int id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		ExpenseEntity expense = expenseRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Expense", id));
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(expense, ExpenseChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return expense;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/api/v1/housing/{caseId}/expense/{id}")
	public ExpenseEntity updateExpense(
			@PathVariable int caseId,
			@PathVariable int id,
			@RequestBody @Validated(InputChecks.class) ExpenseEntity expense) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(expense, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<ExpenseEntity> previousExpense = expenseRepo.findByHousingBenefitCaseAndId(hbce, id);
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
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		ExpenseEntity expense = expenseRepo.findByHousingBenefitCaseAndId(hbce,id).orElseThrow(() -> new NotFoundException("Expense", id));
 		expenseRepo.delete(expense);
	}

}
