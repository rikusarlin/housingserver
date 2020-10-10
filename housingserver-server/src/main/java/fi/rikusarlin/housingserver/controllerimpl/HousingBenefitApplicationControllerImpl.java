package fi.rikusarlin.housingserver.controllerimpl;

import java.time.Period;
import java.util.HashSet;
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
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.exception.TooLongRangeException;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.IncomeRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class HousingBenefitApplicationControllerImpl implements HousingApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    HousingBenefitApplicationRepository hbaRepo;
    @Autowired
    HouseholdMemberRepository householdMemberRepo;
    @Autowired
    IncomeRepository incomeRepo;
    @Autowired
    ExpenseRepository expenseRepo;
    @Autowired
    PersonRepository personRepo;
    
    @Override
    public ResponseEntity<List<HousingBenefitApplication>> fetchHousingBenefitApplicationsByPersonNumber(
    		String personNumber) {
    	Iterable<HousingBenefitApplicationEntity> hbas = hbaRepo.findByPersonNumber(personNumber);
    	return ResponseEntity.ok(
     			StreamSupport.stream(hbas.spliterator(), false)
     			.map(hba -> hba.toHousingBenefitApplication())
     			.collect(Collectors.toList()));
    }

    @Override
	public ResponseEntity<HousingBenefitApplication> fetchHousingBenefitApplicationById(Integer caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	return ResponseEntity.ok(hba.toHousingBenefitApplication());
	}
 
    @Override
	public ResponseEntity<HousingBenefitApplication> addHousingBenefitApplication(HousingBenefitApplication hba) {
		HousingBenefitApplicationEntity hbae = new HousingBenefitApplicationEntity(hba);
		HousingBenefitApplicationEntity hbaSaved = hbaRepo.save(hbae);
		for(HouseholdMember hm:hba.getHouseholdMembers()) {
			PersonEntity p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
			HouseholdMemberEntity hme = new HouseholdMemberEntity(hm);
			hme.setApplication(hbaSaved);
			hme.setPerson(p);
			householdMemberRepo.save(hme);
		}
		for(Expense e:hba.getHousingExpenses()) {
			ExpenseEntity ee = new ExpenseEntity(e);
			ee.setApplication(hbaSaved);
			expenseRepo.save(ee);
		}
		for(Income i:hba.getIncomes()) {
			IncomeEntity ie = new IncomeEntity(i);
			ie.setApplication(hbaSaved);
			incomeRepo.save(ie);
		}
		return ResponseEntity.ok(hbaSaved.toHousingBenefitApplication());
	}

    @Override
	public ResponseEntity<HousingBenefitApplication> checkHousingBenefitApplication(Integer caseId) {
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(hbae.toHousingBenefitApplication());
	}

    @Override
	public ResponseEntity<HousingBenefitApplication> updateHousingBenefitApplication(
			Integer caseId,
			HousingBenefitApplication hba) {
    	HousingBenefitApplicationEntity hbae = new HousingBenefitApplicationEntity(hba);
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HousingBenefitApplicationEntity> previousHbae = hbaRepo.findById(caseId);
		previousHbae.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(hba.getEndDate());
				 		value.setStartDate(hba.getStartDate());
				 		value.setHouseholdMembers(new HashSet<HouseholdMemberEntity>());
				 		value.setIncomes(new HashSet<IncomeEntity>());
				 		value.setHousingExpenses(new HashSet<ExpenseEntity>());
						for(HouseholdMember hm:hba.getHouseholdMembers()) {
							HouseholdMemberEntity hme = new HouseholdMemberEntity(hm);
							PersonEntity pe = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
							hme.setApplication(value);
							hme.setPerson(pe);
							value.getHouseholdMembers().add(hme);
							householdMemberRepo.save(hme);
						}
						for(Expense e:hba.getHousingExpenses()) {
							ExpenseEntity ee = new ExpenseEntity(e);
							ee.setApplication(value);
							value.addExpense(ee);
							expenseRepo.save(ee);
						}
						for(Income i:hba.getIncomes()) {
							IncomeEntity ie = new IncomeEntity(i);
							ie.setApplication(value);
							value.addIncome(ie);
							incomeRepo.save(ie);
						}
						value = hbaRepo.save(value);
					},
				()
				 	-> {
				 		hbaRepo.save(new HousingBenefitApplicationEntity(hba));
				 	});
		return fetchHousingBenefitApplicationById(caseId);
	}
	
    @Override
	public ResponseEntity<Void> deleteHousingBenefitApplication(Integer caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
 		hbaRepo.delete(hba);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

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

    @Override
    public ResponseEntity<List<HouseholdMember>> fetchHouseholdMembers(Integer caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	Iterable<HouseholdMemberEntity> householdMembers = householdMemberRepo.findByApplication(hba);
    	return ResponseEntity.ok(
    			StreamSupport.stream(householdMembers.spliterator(), false)
    			.map(hm -> hm.toHouseholdMember())
    			.collect(Collectors.toList()));
    }
     
    @Override
	public ResponseEntity<HouseholdMember> fetchHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMemberEntity hme = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
		return ResponseEntity.ok(hme.toHouseholdMember());
	}
 
    @Override
	public ResponseEntity<HouseholdMember> addHouseholdMember(
			Integer caseId,
			HouseholdMember householdMember) {
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	PersonEntity pe = personRepo.findById(householdMember.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", householdMember.getPerson().getId()));
    	HouseholdMemberEntity hme = new HouseholdMemberEntity(householdMember);
    	hme.setApplication(hbae);
    	hme.setPerson(pe);
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(householdMemberRepo.save(hme).toHouseholdMember());
	}

	/**
	 * Cross validate a household member
	 * Note how Spring validation checks and manual checks are combined
	 */
    @Override
	public ResponseEntity<HouseholdMember> checkHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMemberEntity hm = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hm, HouseholdChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		/*
		 * Period max one year in this case
		 * Yes, we could have written a validator for this, too, but wanted to show
		 * manual checks are added to Spring Validation checks
		 */
		if(hm.getStartDate() != null && hm.getEndDate() != null){
			Period  period = Period.between(hm.getStartDate(), hm.getEndDate());
			if(period.getYears() >= 1) {
				throw new TooLongRangeException(hm.getStartDate(), hm.getEndDate());
			}
		}
		return ResponseEntity.ok(hm.toHouseholdMember());
	}

	
    @Override
	public ResponseEntity<HouseholdMember> updateHouseholdMember(
			Integer caseId, 
			Integer id, 
			HouseholdMember householdMember) {
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMemberEntity hme = new HouseholdMemberEntity(householdMember);
    	Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HouseholdMemberEntity> hm = householdMemberRepo.findByApplicationAndId(hbae, id);
		hm.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(householdMember.getEndDate());
				 		value.setStartDate(householdMember.getStartDate());
				 		value.setPerson(new PersonEntity(householdMember.getPerson()));
				 		value.setApplication(hbae);
						householdMemberRepo.save(value);
					},
				()
				 	-> {
				 		householdMemberRepo.save(hme);
				 	});
		return fetchHouseholdMemberById(caseId, id);
	}

    @Override
	public ResponseEntity<Void> deleteHouseholdMember(
			Integer caseId, 
			Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	HouseholdMemberEntity hm = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
 		householdMemberRepo.delete(hm);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

    @Override
    public ResponseEntity<List<Income>> fetchIncomes(Integer caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	Iterable<IncomeEntity> incomes = incomeRepo.findByApplication(hba);
    	return ResponseEntity.ok(
    			StreamSupport.stream(incomes.spliterator(), false)
    			.map(income -> income.toIncome())
    			.collect(Collectors.toList()));
    }

	@Override
	public ResponseEntity<Income> fetchIncomeById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = incomeRepo.findById(id).orElseThrow(() -> new NotFoundException("Income", id));
		ie.setApplication(hba);
		return ResponseEntity.ok(ie.toIncome());
	}
 
	@Override
	public ResponseEntity<Income> addIncome(Integer caseId, Income income) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = new IncomeEntity(income);
		ie.setApplication(hba);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(incomeRepo.save(ie).toIncome());
	}

	@Override
	public ResponseEntity<Income> checkIncomeById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	IncomeEntity ie = incomeRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Income", id));
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return fetchIncomeById(id, caseId);
	}

	@Override
	public ResponseEntity<Income> updateIncome(Integer caseId, Integer id, Income income) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = new IncomeEntity(income);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<IncomeEntity> previousIncome = incomeRepo.findByApplicationAndId(hba, id);
		previousIncome.ifPresentOrElse(
			(value) 
				-> {
					value.setStartDate(income.getStartDate());
					value.setEndDate(income.getEndDate());
					value.setAmount(income.getAmount());
					value.setIncomeType(income.getIncomeType());
					value.setOtherIncomeDescription(income.getOtherIncomeDescription());
					incomeRepo.save(value);
				},
			()
				-> {
			 		incomeRepo.save(ie);
				});
		return fetchIncomeById(caseId, id);
	}
	
	@Override
	public ResponseEntity<Void> deleteIncome(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = incomeRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Income", id));
 		incomeRepo.delete(ie);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
