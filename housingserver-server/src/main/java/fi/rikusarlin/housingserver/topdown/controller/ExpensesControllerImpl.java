package fi.rikusarlin.housingserver.topdown.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.ExpensesApi;
import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitCaseRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class ExpensesControllerImpl implements ExpensesApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Autowired
	@Qualifier("expenseRepositoryJson")
	ExpenseRepository expenseRepo;
    @Autowired
	@Qualifier("housingBenefitCaseRepositoryJson")
    HousingBenefitCaseRepository caseRepo;
    
	@Override
	public ResponseEntity<Expense> fetchExpenseById(Integer caseId, Integer id) {
    	Expense e = expenseRepo.findByHousingBenefitCaseIdAndId(caseId, id).orElseThrow(() -> new NotFoundException("Expense", id));
		return ResponseEntity.ok(e);
	}
 
	@Override
	public ResponseEntity<Expense> addExpense(Integer caseId, Expense expense) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	ExpenseEntity ee = MappingUtil.modelMapperInsert.map(expense, ExpenseEntity.class);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(expenseRepo.save(expense, caseId));
	}

	@Override
	public ResponseEntity<Expense> checkExpenseById(Integer caseId, Integer id) {
    	Expense e = expenseRepo.findByHousingBenefitCaseIdAndId(caseId, id).orElseThrow(() -> new NotFoundException("Expense", id));
    	ExpenseEntity ee = MappingUtil.modelMapperInsert.map(e, ExpenseEntity.class);
    	Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(e);
	}

	@Override
	public ResponseEntity<Expense> updateExpense(Integer caseId, Integer id, Expense expense) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	ExpenseEntity ee = MappingUtil.modelMapper.map(expense, ExpenseEntity.class);
		ee.setId(id);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		expense.setId(id);
		return ResponseEntity.ok(expenseRepo.save(expense, caseId));
	}
	
	@Override
	public ResponseEntity<Void> deleteExpense(Integer caseId, Integer id) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	expenseRepo.findByHousingBenefitCaseIdAndId(caseId, id).orElseThrow(() -> new NotFoundException("Expense", id));
 		expenseRepo.delete(id);
 		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
    
    @Override
    public ResponseEntity<List<Expense>> fetchExpenses(Integer caseId) {
    	Iterable<Expense> expenses = expenseRepo.findByHousingBenefitCaseId(caseId);
    	return ResponseEntity.ok(
    			StreamSupport.stream(expenses.spliterator(), false)
    			.collect(Collectors.toList()));
    }
}
