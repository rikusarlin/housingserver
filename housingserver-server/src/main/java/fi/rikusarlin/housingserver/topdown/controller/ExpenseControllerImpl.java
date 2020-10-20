package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Set;

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

import fi.rikusarlin.housingserver.api.ExpenseApi;
import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.repository.basic.CaseRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class ExpenseControllerImpl implements ExpenseApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Autowired
	@Qualifier("expenseRepositoryJpa")
	ExpenseRepository expenseRepo;
    @Autowired
    CaseRepository caseRepo;
    
	@Override
	public ResponseEntity<Expense> fetchExpenseById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		HousingBenefitCase hbc = MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class);
    	Expense e = expenseRepo.findByHousingBenefitCaseAndId(hbc, id).orElseThrow(() -> new NotFoundException("Expense", id));
		return ResponseEntity.ok(e);
	}
 
	@Override
	public ResponseEntity<Expense> addExpense(Integer caseId, Expense expense) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HousingBenefitCase hbc = MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class);
    	ExpenseEntity ee = MappingUtil.modelMapperInsert.map(expense, ExpenseEntity.class);
    	ee.setHousingBenefitCase(hbce);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(expenseRepo.save(expense, hbc));
	}

	@Override
	public ResponseEntity<Expense> checkExpenseById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HousingBenefitCase hbc = MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class);
    	Expense e = expenseRepo.findByHousingBenefitCaseAndId(hbc, id).orElseThrow(() -> new NotFoundException("Expense", id));
    	ExpenseEntity ee = MappingUtil.modelMapperInsert.map(e, ExpenseEntity.class);
    	Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(e);
	}

	@Override
	public ResponseEntity<Expense> updateExpense(Integer caseId, Integer id, Expense expense) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HousingBenefitCase hbc = MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class);
    	ExpenseEntity ee = MappingUtil.modelMapper.map(expense, ExpenseEntity.class);
		ee.setHousingBenefitCase(hbce);
		ee.setId(id);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		expense.setId(id);
		return ResponseEntity.ok(expenseRepo.save(expense, hbc));
	}
	
	@Override
	public ResponseEntity<Void> deleteExpense(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		HousingBenefitCase hbc = MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class);
    	Expense e = expenseRepo.findByHousingBenefitCaseAndId(hbc, id).orElseThrow(() -> new NotFoundException("Expense", id));
 		expenseRepo.delete(e, hbc);
 		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

    
}
