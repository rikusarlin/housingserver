package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import fi.rikusarlin.housingserver.api.ExpensesApiService;
import fi.rikusarlin.housingserver.api.NotFoundException;
import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@ApplicationScoped
public class ExpensesControllerImpl implements ExpensesApiService {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Inject
    ExpenseRepository expenseRepo;
    @Inject
    CaseRepository caseRepo;
    
	@Override
	public Response fetchExpenseById(Integer caseId, Integer id, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
		ExpenseEntity ee = expenseRepo.findById(id).orElseThrow(() -> new NotFoundException(id, "Expense"));
		ee.setHousingBenefitCase(hbce);
		return Response.ok(MappingUtil.modelMapper.map(ee, Expense.class)).build();
	}
 
	@Override
	public Response addExpense(Integer caseId, Expense expense, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	ExpenseEntity ee = new ExpenseEntity();
    	MappingUtil.modelMapperInsert.map(expense, ee);
    	ee.setHousingBenefitCase(hbce);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		ee.setId(null);
		return Response.ok(MappingUtil.modelMapper.map(expenseRepo.save(ee), Expense.class)).build();
	}

	@Override
	public Response checkExpenseById(Integer caseId, Integer id, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	ExpenseEntity ee = expenseRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException(id, "Expense"));
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return Response.ok(MappingUtil.modelMapper.map(ee, Expense.class)).build();
	}

	@Override
	public Response updateExpense(Integer caseId, Integer id, Expense expense, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
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
		return fetchExpenseById(caseId, id, securityContext);
	}
	
	@Override
	public Response deleteExpense(Integer caseId, Integer id, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
		ExpenseEntity ee = expenseRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException(id, "Expense"));
 		expenseRepo.delete(ee);
 		return Response.ok().build();
	}

    
    @Override
    public Response fetchExpenses(Integer caseId, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	Iterable<ExpenseEntity> ees = expenseRepo.findByHousingBenefitCase(hbce);
    	return Response.ok(
    			StreamSupport.stream(ees.spliterator(), false)
    			.map(ee -> MappingUtil.modelMapper.map(ee, Expense.class))
    			.collect(Collectors.toList())).build();
    }
}
