package fi.rikusarlin.housingserver;

import java.time.Period;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;

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

import fi.rikusarlin.housingserver.data.Expense;
import fi.rikusarlin.housingserver.data.HouseholdMember;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.validation.ExpenseChecks;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class AppController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    HouseholdMemberRepository householdMemberRepo;
    @Autowired
    ExpenseRepository expenseRepo;

    @GetMapping("/householdmembers")
    public @ResponseBody Iterable<HouseholdMember> findHouseholdMembers() {
        return householdMemberRepo.findAll();
    }
     
	@GetMapping(value = "/householdmember/{id}")
	public HouseholdMember findHouseholdMemberById(@Min(value=0) @PathVariable int id) {
		return householdMemberRepo.findById(id).orElseThrow(() -> new NotFoundException("Household member", id));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/householdmember")
	public HouseholdMember addHouseholdMember(@RequestBody @Validated(InputChecks.class) HouseholdMember householdMember) {
		return householdMemberRepo.save(householdMember);
	}

	/**
	 * Cross validate a household member
	 * Note how Spring validation checks and manual checks are combined
	 */
	@GetMapping(value = "/householdmember/{id}/check")
	public HouseholdMember checkHouseholdMemberById(@Min(value=0) @PathVariable int id) {
		HouseholdMember hm = householdMemberRepo.findById(id).orElseThrow(() -> new NotFoundException("Household member", id));
		Set<ConstraintViolation<HouseholdMember>> violations =  validator.validate(hm, HouseholdChecks.class);
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
		return hm;
	}

	
	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/householdmember/{id}")
	public HouseholdMember updateHouseholdMember(@Min(value=0) @PathVariable int id, @RequestBody @Validated(InputChecks.class) HouseholdMember householdMember) {
		Set<ConstraintViolation<HouseholdMember>> violations =  validator.validate(householdMember, HouseholdChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HouseholdMember> hm = householdMemberRepo.findById(id);
		hm.ifPresentOrElse(
				(value) 
					-> {
				 		value.setEndDate(householdMember.getEndDate());
				 		value.setStartDate(householdMember.getStartDate());
				 		value.setPersonNumber(householdMember.getPersonNumber());
						householdMemberRepo.save(value);
					},
				()
				 	-> {
				 		householdMemberRepo.save(householdMember);
				 	});
		return findHouseholdMemberById(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/householdmember/{id}")
	public void deleteHouseholdMember(@Min(value=0) @PathVariable int id) {
		HouseholdMember hm = householdMemberRepo.findById(id).orElseThrow(() -> new NotFoundException("Household member", id));
 		householdMemberRepo.delete(hm);
	}
	
    @GetMapping("/expenses")
    public @ResponseBody Iterable<Expense> findExpenses() {
        return expenseRepo.findAll();
    }
     
	@GetMapping(value = "/expense/{id}")
	public Expense findExpenseById(@Min(value=0) @PathVariable int id) {
		return expenseRepo.findById(id).orElseThrow(() -> new NotFoundException("Expense", id));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/expense")
	public Expense addExpense(@RequestBody @Validated(InputChecks.class) Expense expense) {
		return expenseRepo.save(expense);
	}

	@GetMapping(value = "/expense/{id}/check")
	public Expense checkExpenseById(@Min(value=0) @PathVariable int id) {
		Expense expense = expenseRepo.findById(id).orElseThrow(() -> new NotFoundException("Expense", id));
		Set<ConstraintViolation<Expense>> violations =  validator.validate(expense, ExpenseChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return expense;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/expense/{id}")
	public Expense updateExpense(@Min(value=0) @PathVariable int id, @RequestBody @Validated(InputChecks.class) Expense expense) {
		Set<ConstraintViolation<Expense>> violations =  validator.validate(expense, ExpenseChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<Expense> previousExpense = expenseRepo.findById(id);
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
		return findExpenseById(id);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/expense/{id}")
	public void deleteExpense(@Min(value=0) @PathVariable int id) {
		Expense expense = expenseRepo.findById(id).orElseThrow(() -> new NotFoundException("Expense", id));
 		expenseRepo.delete(expense);
	}


}
