package fi.rikusarlin.housingserver.repository.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;

@Component("expenseRepositoryJpa")
public class ExpenseRepositoryJpaImpl implements ExpenseRepository {

    @Autowired
    private ExpenseJpaRepository expenseJpaRepo;

    public Expense save(Expense expense, HousingBenefitCase hbc) {
    	ExpenseEntity ee = MappingUtil.modelMapper.map(expense, ExpenseEntity.class);
    	HousingBenefitCaseEntity hbce = MappingUtil.modelMapper.map(hbc, HousingBenefitCaseEntity.class);
    	ee.setHousingBenefitCase(hbce);
        return MappingUtil.modelMapper.map(expenseJpaRepo.save(ee), Expense.class);
    }


	@Override
	public Optional<Expense> findById(Integer id) {
		Optional<ExpenseEntity> expense = expenseJpaRepo.findById(id);
		if(expense.isPresent()) {
			Expense e = MappingUtil.modelMapper.map(expense.get(), Expense.class);
			return Optional.of(e);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<Expense> findAll() {
		Iterable<ExpenseEntity> expenses = expenseJpaRepo.findAll();
		return StreamSupport.stream(expenses.spliterator(), false)
				.map(expense -> MappingUtil.modelMapper.map(expense, Expense.class))
				.collect(Collectors.toList());
 	}


	@Override
	public void delete(Expense expense, HousingBenefitCase hbc) {
		ExpenseEntity ee = MappingUtil.modelMapper.map(expense, ExpenseEntity.class);
    	HousingBenefitCaseEntity hbce = MappingUtil.modelMapper.map(hbc, HousingBenefitCaseEntity.class);
		ee.setHousingBenefitCase(hbce);
		expenseJpaRepo.delete(ee);
	}

	@Override
	public Iterable<Expense> findAll(Sort sort) {
		Iterable<ExpenseEntity> expenses = expenseJpaRepo.findAll(sort);
		// It might be better to do this using for-next loop rather than map-reduce to retain order
		List<Expense> expenseList = new ArrayList<Expense>();
		for(ExpenseEntity e:expenses) {
			expenseList.add(MappingUtil.modelMapper.map(e, Expense.class));
		}
		return expenseList;
	}

	@Override
	public List<Expense> findByHousingBenefitCase(HousingBenefitCase housingBenefitCase) {
		HousingBenefitCaseEntity hbce = MappingUtil.modelMapper.map(housingBenefitCase, HousingBenefitCaseEntity.class);
		Iterable<ExpenseEntity> expenses = expenseJpaRepo.findByHousingBenefitCase(hbce);
		return StreamSupport.stream(expenses.spliterator(), false)
				.map(expense -> MappingUtil.modelMapper.map(expense, Expense.class))
				.collect(Collectors.toList());

	}


	@Override
	public Optional<Expense> findByHousingBenefitCaseAndId(HousingBenefitCase housingBenefitCase, Integer id) {
		HousingBenefitCaseEntity hbce = MappingUtil.modelMapper.map(housingBenefitCase, HousingBenefitCaseEntity.class);
		Optional<ExpenseEntity> expense = expenseJpaRepo.findByHousingBenefitCaseAndId(hbce, id);
		if(expense.isPresent()) {
			Expense e = MappingUtil.modelMapper.map(expense.get(), Expense.class);
			return Optional.of(e);
		} else {
			return Optional.empty();
		}
	}

}