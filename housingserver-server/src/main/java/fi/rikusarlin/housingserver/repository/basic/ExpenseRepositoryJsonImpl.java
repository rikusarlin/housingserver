package fi.rikusarlin.housingserver.repository.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.ExpenseJsonEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;

@Component("expenseRepositoryJson")
public class ExpenseRepositoryJsonImpl implements ExpenseRepository {

	@Autowired
    private HousingDataJpaRepository housingDataJpaRepo;
    
	@Override
	public Expense save(Expense expense, HousingBenefitCaseEntity hbce) {
    	ExpenseJsonEntity dataEntity = MappingUtil.modelMapper.map(expense, ExpenseJsonEntity.class);
    	dataEntity.setHousingBenefitCase(hbce);
    	dataEntity.setHousingDataType(HousingDataType.EXPENSE);
    	dataEntity.setExpense(expense);
    	ExpenseJsonEntity savedEntity = housingDataJpaRepo.save(dataEntity);
    	expense.setId(savedEntity.getId());
    	return expense;
	}

	@Override
	public Optional<Expense> findById(Integer id) {
    	Optional<ExpenseJsonEntity> hdje = housingDataJpaRepo.findById(id);
		if(hdje.isPresent()) {
			Expense e = hdje.get().getExpense();
			e.setId(hdje.get().getId());
			return Optional.of(e);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<Expense> findAll() {
		Iterable<ExpenseJsonEntity> expenses = housingDataJpaRepo.findAll();
		return StreamSupport.stream(expenses.spliterator(), false)
				.map(expense -> {
					Expense e = MappingUtil.modelMapper.map(expense.getExpense(), Expense.class);
					e.setId(expense.getId());
					return e;
				})
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Expense expense, HousingBenefitCaseEntity hbce) {
    	ExpenseJsonEntity dataEntity = MappingUtil.modelMapper.map(expense, ExpenseJsonEntity.class);
    	dataEntity.setHousingBenefitCase(hbce);
    	dataEntity.setHousingDataType(HousingDataType.EXPENSE);
    	dataEntity.setExpense(expense);
    	housingDataJpaRepo.delete(dataEntity);
	}

	@Override
	public Iterable<Expense> findAll(Sort sort) {
		Iterable<ExpenseJsonEntity> expenses = housingDataJpaRepo.findAll(sort);
		List<Expense> expenseList = new ArrayList<Expense>();
		for(ExpenseJsonEntity expense:expenses) {
			Expense e = MappingUtil.modelMapper.map(expense.getExpense(), Expense.class);
			e.setId(expense.getId());
			expenseList.add(e);
		}
		return expenseList;
	}

	@Override
	public List<Expense> findByHousingBenefitCase(HousingBenefitCaseEntity hbce) {
		Iterable<ExpenseJsonEntity> expenses = housingDataJpaRepo.findByHousingBenefitCase(hbce);
		return StreamSupport.stream(expenses.spliterator(), false)
				.map(expense -> {
					Expense e = MappingUtil.modelMapper.map(expense.getExpense(), Expense.class);
					e.setId(expense.getId());
					return e;
				})
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Expense> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id) {
		Optional<ExpenseJsonEntity> hdje = housingDataJpaRepo.findByHousingBenefitCaseAndId(hbce, id);
		if(hdje.isPresent()) {
			Expense e = hdje.get().getExpense();
			e.setId(hdje.get().getId());
			return Optional.of(e);
		} else {
			return Optional.empty();
		}

	}

}
