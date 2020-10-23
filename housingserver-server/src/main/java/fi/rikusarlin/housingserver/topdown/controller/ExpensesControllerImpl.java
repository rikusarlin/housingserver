package fi.rikusarlin.housingserver.topdown.controller;

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

import fi.rikusarlin.housingserver.api.ExpensesApi;
import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class ExpensesControllerImpl implements ExpensesApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    ExpenseRepository expenseRepo;
    @Autowired
    CaseRepository caseRepo;
    
	@Override
	public ResponseEntity<Expense> fetchExpenseById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		ExpenseEntity ee = expenseRepo.findById(id).orElseThrow(() -> new NotFoundException("Expense", id));
		ee.setHousingBenefitCase(hbce);
		return ResponseEntity.ok(MappingUtil.modelMapper.map(ee, Expense.class));
	}
 
	@Override
	public ResponseEntity<Expense> addExpense(Integer caseId, Expense expense) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	ExpenseEntity ee = new ExpenseEntity();
    	MappingUtil.modelMapperInsert.map(expense, ee);
    	ee.setHousingBenefitCase(hbce);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		ee.setId(null);
		return ResponseEntity.ok(MappingUtil.modelMapper.map(expenseRepo.save(ee), Expense.class));
	}

	@Override
	public ResponseEntity<Expense> checkExpenseById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	ExpenseEntity ee = expenseRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Expense", id));
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(MappingUtil.modelMapper.map(ee, Expense.class));
	}

	@Override
	public ResponseEntity<Expense> updateExpense(Integer caseId, Integer id, Expense expense) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		ExpenseEntity ee = MappingUtil.modelMapper.map(expense, ExpenseEntity.class);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<ExpenseEntity> previousExpense = expenseRepo.findByHousingBenefitCaseAndId(hbce, id);
		previousExpense.ifPresentOrElse(
			(value) 
				-> {
					MappingUtil.modelMapper.map(expense, value);
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
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		ExpenseEntity ee = expenseRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Expense", id));
 		expenseRepo.delete(ee);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

    
    @Override
    public ResponseEntity<List<Expense>> fetchExpenses(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	Iterable<ExpenseEntity> ees = expenseRepo.findByHousingBenefitCase(hbce);
    	return ResponseEntity.ok(
    			StreamSupport.stream(ees.spliterator(), false)
    			.map(ee -> MappingUtil.modelMapper.map(ee, Expense.class))
    			.collect(Collectors.toList()));
    }
}
