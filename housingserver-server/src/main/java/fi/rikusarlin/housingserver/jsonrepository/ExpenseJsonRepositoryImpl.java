package fi.rikusarlin.housingserver.jsonrepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.json.ExpenseJsonEntity;
import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;

@Component("expenseRepositoryJson")
public class ExpenseJsonRepositoryImpl implements ExpenseRepository {

	@Autowired
    private ExpenseJsonRepository expenseJsonRepo;
	@Autowired
    private HousingBenefitCaseJsonRepository caseRepo;


	@Override
	public Expense save(Expense expense, Integer caseId) {
    	HousingBenefitCaseJsonEntity hbcje = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	ExpenseJsonEntity dataEntity = MappingUtil.modelMapper.map(expense, ExpenseJsonEntity.class);
    	dataEntity.setHousingBenefitCase(hbcje);
    	dataEntity.setExpense(expense);
    	ExpenseJsonEntity savedEntity = expenseJsonRepo.save(dataEntity);
    	expense.setId(savedEntity.getId());
    	return expense;
	}

	@Override
	public Optional<Expense> findById(Integer id) {
    	Optional<ExpenseJsonEntity> hdje = expenseJsonRepo.findById(id);
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
		Iterable<ExpenseJsonEntity> expenses = expenseJsonRepo.findAll();
		return StreamSupport.stream(expenses.spliterator(), false)
				.map(expense -> {
					Expense e = MappingUtil.modelMapper.map(expense.getExpense(), Expense.class);
					e.setId(expense.getId());
					return e;
				})
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) {
		ExpenseJsonEntity eje = expenseJsonRepo.findById(id).orElseThrow(() -> new NotFoundException("Expense", id));
		eje.getHousingBenefitCase().getHousingExpenses().remove(eje);
		expenseJsonRepo.delete(eje);
	}

	@Override
	public Iterable<Expense> findAll(Sort sort) {
		Iterable<ExpenseJsonEntity> expenses = expenseJsonRepo.findAll(sort);
		List<Expense> expenseList = new ArrayList<Expense>();
		for(ExpenseJsonEntity expense:expenses) {
			Expense e = MappingUtil.modelMapper.map(expense.getExpense(), Expense.class);
			e.setId(expense.getId());
			expenseList.add(e);
		}
		return expenseList;
	}

	@Override
	public List<Expense> findByHousingBenefitCaseId(Integer caseId) {
    	HousingBenefitCaseJsonEntity hbcje = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Iterable<ExpenseJsonEntity> expenses = expenseJsonRepo.findByHousingBenefitCase(hbcje);
		return StreamSupport.stream(expenses.spliterator(), false)
				.map(expense -> {
					Expense e = MappingUtil.modelMapper.map(expense.getExpense(), Expense.class);
					e.setId(expense.getId());
					return e;
				})
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Expense> findByHousingBenefitCaseIdAndId(Integer caseId, Integer id) {
    	HousingBenefitCaseJsonEntity hbcje = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Optional<ExpenseJsonEntity> hdje = expenseJsonRepo.findByHousingBenefitCaseAndId(hbcje, id);
		if(hdje.isPresent()) {
			Expense e = hdje.get().getExpense();
			e.setId(hdje.get().getId());
			return Optional.of(e);
		} else {
			return Optional.empty();
		}

	}

}
