package fi.rikusarlin.housingserver.controllerimpl;

import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
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

    ModelMapper modelMapper = new ModelMapper();

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
     			.map(hbae -> modelMapper.map(hbae, HousingBenefitApplication.class))
     			.collect(Collectors.toList()));
    }

    @Override
	public ResponseEntity<HousingBenefitApplication> fetchHousingBenefitApplicationById(Integer caseId) {
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	return ResponseEntity.ok(modelMapper.map(hbae, HousingBenefitApplication.class));
	}
 
    @Override
	public ResponseEntity<HousingBenefitApplication> addHousingBenefitApplication(HousingBenefitApplication hba) {
    	HousingBenefitApplicationEntity hbae = new HousingBenefitApplicationEntity();
    	BeanUtils.copyProperties(hba, hbae, "id");
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		HousingBenefitApplicationEntity hbaeSaved = hbaRepo.save(hbae);
		for(HouseholdMember hm:hba.getHouseholdMembers()) {
			PersonEntity p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
	    	HouseholdMemberEntity hme = new HouseholdMemberEntity();
	    	BeanUtils.copyProperties(hm, hme, "id");
			hme.setApplication(hbaeSaved);
			hme.setPerson(p);
			householdMemberRepo.save(hme);
			hbae.getHouseholdMembers().add(hme);
		}
		for(Expense e:hba.getHousingExpenses()) {
			ExpenseEntity ee = new ExpenseEntity();
			BeanUtils.copyProperties(e, ee, "id");
			ee.setApplication(hbaeSaved);
			expenseRepo.save(ee);
			hbae.getHousingExpenses().add(ee);
		}
		for(Income i:hba.getIncomes()) {
			IncomeEntity ie = new IncomeEntity();
			BeanUtils.copyProperties(i, ie, "id");
			ie.setApplication(hbaeSaved);
			incomeRepo.save(ie);
			hbae.getIncomes().add(ie);
		}
		return ResponseEntity.ok(modelMapper.map(hbaeSaved, HousingBenefitApplication.class));
	}

    @Override
	public ResponseEntity<HousingBenefitApplication> checkHousingBenefitApplication(Integer caseId) {
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(modelMapper.map(hbae, HousingBenefitApplication.class));
	}

    @Override
	public ResponseEntity<HousingBenefitApplication> updateHousingBenefitApplication(
			Integer caseId,
			HousingBenefitApplication hba) {
		HousingBenefitApplicationEntity hbae = modelMapper.map(hba, HousingBenefitApplicationEntity.class);
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HousingBenefitApplicationEntity> previousHbae = hbaRepo.findById(caseId);
		previousHbae.ifPresentOrElse(
				(value) 
					-> {
						BeanUtils.copyProperties(hba, value, "id");
						value = hbaRepo.save(value);
						// Remove all children and add those received in parameter
						value.getHouseholdMembers().removeAll(value.getHouseholdMembers());
						for(HouseholdMember hm:hba.getHouseholdMembers()) {
							HouseholdMemberEntity hme = new HouseholdMemberEntity();
					    	BeanUtils.copyProperties(hm, hme, "id");
							PersonEntity pe = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
							hme.setApplication(value);
							hme.setPerson(pe);
							value.getHouseholdMembers().add(hme);
							householdMemberRepo.save(hme);
						}
						value.getHousingExpenses().removeAll(value.getHousingExpenses());
						for(Expense e:hba.getHousingExpenses()) {
							ExpenseEntity ee = new ExpenseEntity();
					    	BeanUtils.copyProperties(e, ee, "id");
							ee.setApplication(value);
							value.getHousingExpenses().add(ee);
							expenseRepo.save(ee);
						}
						value.getIncomes().removeAll(value.getIncomes());
						for(Income i:hba.getIncomes()) {
							IncomeEntity ie = new IncomeEntity();
					    	BeanUtils.copyProperties(i, ie, "id");
							ie.setApplication(value);
							value.getIncomes().add(ie);
							incomeRepo.save(ie);
						}
					},
				()
				 	-> {
				 		hbaRepo.save(hbae);
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
    	Iterable<ExpenseEntity> ees = expenseRepo.findByApplication(hba);
    	return ResponseEntity.ok(
    			StreamSupport.stream(ees.spliterator(), false)
    			.map(ee -> modelMapper.map(ee, Expense.class))
    			.collect(Collectors.toList()));
    }

	@Override
	public ResponseEntity<Expense> fetchExpenseById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		ExpenseEntity ee = expenseRepo.findById(id).orElseThrow(() -> new NotFoundException("Expense", id));
		ee.setApplication(hba);
		return ResponseEntity.ok(modelMapper.map(ee, Expense.class));
	}
 
	@Override
	public ResponseEntity<Expense> addExpense(Integer caseId, Expense expense) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		ExpenseEntity ee = modelMapper.map(expense, ExpenseEntity.class);
		ee.setApplication(hba);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		ee.setId(null);
		return ResponseEntity.ok(modelMapper.map(expenseRepo.save(ee), Expense.class));
	}

	@Override
	public ResponseEntity<Expense> checkExpenseById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	ExpenseEntity ee = expenseRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Expense", id));
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(modelMapper.map(ee, Expense.class));
	}

	@Override
	public ResponseEntity<Expense> updateExpense(Integer caseId, Integer id, Expense expense) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		ExpenseEntity ee = modelMapper.map(expense, ExpenseEntity.class);
		Set<ConstraintViolation<ExpenseEntity>> violations =  validator.validate(ee, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<ExpenseEntity> previousExpense = expenseRepo.findByApplicationAndId(hba, id);
		previousExpense.ifPresentOrElse(
			(value) 
				-> {
					BeanUtils.copyProperties(expense, value, "id");
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
    			.map(hm -> modelMapper.map(hm, HouseholdMember.class))
    			.collect(Collectors.toList()));
    }
     
    @Override
	public ResponseEntity<HouseholdMember> fetchHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMemberEntity hme = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
		return ResponseEntity.ok(modelMapper.map(hme, HouseholdMember.class));
	}
 
    @Override
	public ResponseEntity<HouseholdMember> addHouseholdMember(
			Integer caseId,
			HouseholdMember hm) {
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	PersonEntity pe = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
    	HouseholdMemberEntity hme = new HouseholdMemberEntity();
    	BeanUtils.copyProperties(hm,  hme);
    	hme.setApplication(hbae);
    	hme.setPerson(pe);
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		hme.setId(null);
		return ResponseEntity.ok(modelMapper.map(householdMemberRepo.save(hme), HouseholdMember.class));
	}

	/**
	 * Cross validate a household member
	 * Note how Spring validation checks and manual checks are combined
	 */
    @Override
	public ResponseEntity<HouseholdMember> checkHouseholdMemberById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMemberEntity hme = householdMemberRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Household member", id));
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, HouseholdChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		/*
		 * Period max one year in this case
		 * Yes, we could have written a validator for this, too, but wanted to show
		 * manual checks are added to Spring Validation checks
		 */
		if(hme.getStartDate() != null && hme.getEndDate() != null){
			Period  period = Period.between(hme.getStartDate(), hme.getEndDate());
			if(period.getYears() >= 1) {
				throw new TooLongRangeException(hme.getStartDate(), hme.getEndDate());
			}
		}
		return ResponseEntity.ok(modelMapper.map(hme, HouseholdMember.class));
	}

	
    @Override
	public ResponseEntity<HouseholdMember> updateHouseholdMember(
			Integer caseId, 
			Integer id, 
			HouseholdMember hm) {
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		HouseholdMemberEntity hme = modelMapper.map(hm, HouseholdMemberEntity.class);
    	Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HouseholdMemberEntity> previousHme = householdMemberRepo.findByApplicationAndId(hbae, id);
		previousHme.ifPresentOrElse(
				(value) 
					-> {
						BeanUtils.copyProperties(hm, value, "id");
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
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	Iterable<IncomeEntity> incomes = incomeRepo.findByApplication(hbae);
    	return ResponseEntity.ok(
    			StreamSupport.stream(incomes.spliterator(), false)
    			.map(ie -> modelMapper.map(ie, Income.class))
    			.collect(Collectors.toList()));
    }

	@Override
	public ResponseEntity<Income> fetchIncomeById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = incomeRepo.findById(id).orElseThrow(() -> new NotFoundException("Income", id));
		ie.setApplication(hba);
		return ResponseEntity.ok(modelMapper.map(ie, Income.class));
	}
 
	@Override
	public ResponseEntity<Income> addIncome(Integer caseId, Income income) {
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = new IncomeEntity();
		BeanUtils.copyProperties(income, ie, "id");
		ie.setApplication(hbae);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(modelMapper.map(incomeRepo.save(ie), Income.class));
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
    	HousingBenefitApplicationEntity hbae = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = modelMapper.map(income, IncomeEntity.class);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<IncomeEntity> previousIncome = incomeRepo.findByApplicationAndId(hbae, id);
		previousIncome.ifPresentOrElse(
			(value) 
				-> {
					BeanUtils.copyProperties(income, value, "id");
					value.setApplication(hbae);
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
