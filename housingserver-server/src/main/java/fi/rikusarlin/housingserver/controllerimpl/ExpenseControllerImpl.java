package fi.rikusarlin.housingserver.controllerimpl;

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
import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class ExpenseControllerImpl implements HousingApi{
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    ExpenseRepository expenseRepo;
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;

    @Override
    public ResponseEntity<List<Expense>> fetchExpenses(Integer caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	Iterable<ExpenseEntity> expenses = expenseRepo.findByApplication(hba);
    	return ResponseEntity.ok(
    			StreamSupport.stream(expenses.spliterator(), false)
    			.map(expense -> expense.toExpense())
    			.collect(Collectors.toList()));
    }

	@Override
	public ResponseEntity<Expense> fetchExpenseById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		ExpenseEntity ee = expenseRepo.findById(id).orElseThrow(() -> new NotFoundException("Expense", id));
		ee.setApplication(hba);
		return ResponseEntity.ok(ee.toExpense());
	}
 
	@Override
	public ResponseEntity<Expense> addExpense(Integer caseId, Expense expense) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		ExpenseEntity ee = new ExpenseEntity(expense);
		ee.setApplication(hba);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(expenseRepo.save(ee).toExpense());
	}

	@Override
	public ResponseEntity<Expense> checkExpenseById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	ExpenseEntity ee = expenseRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Expense", id));
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(ee.toExpense());
	}

	@Override
	public ResponseEntity<Expense> updateExpense(Integer caseId, Integer id, Expense expense) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		ExpenseEntity ee = new ExpenseEntity(expense);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<ExpenseEntity> previousExpense = expenseRepo.findByApplicationAndId(hba, id);
		previousExpense.ifPresentOrElse(
			(value) 
				-> {
					value.setStartDate(expense.getStartDate());
					value.setEndDate(expense.getEndDate());
					value.setAmount(expense.getAmount());
					value.setExpenseType(expense.getExpenseType());
					value.setOtherExpenseDescription(expense.getOtherExpenseDescription());
					expenseRepo.save(value);
				},
			()
				-> {
			 		expenseRepo.save(ee);
				});
		return fetchExpenseById(caseId, id);
	}
	
	@Override
	public ResponseEntity<Void> deleteExpense(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		ExpenseEntity ee = expenseRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Expense", id));
 		expenseRepo.delete(ee);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
