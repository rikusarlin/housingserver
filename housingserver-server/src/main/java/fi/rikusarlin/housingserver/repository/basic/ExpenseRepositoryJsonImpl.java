package fi.rikusarlin.housingserver.repository.basic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataJsonEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;

@Component("expenseRepositoryJson")
public class ExpenseRepositoryJsonImpl implements ExpenseRepository {

	@Autowired
    private HousingDataJpaRepository housingDataJpaRepo;

	/*
    static ObjectMapper objectMapper = new ObjectMapper();
	
    static {
    	objectMapper.registerModule(new JavaTimeModule());
    	objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    */
    
	@Override
	public Expense save(Expense expense, HousingBenefitCaseEntity hbce) {
    	HousingDataJsonEntity dataEntity = MappingUtil.modelMapper.map(expense, HousingDataJsonEntity.class);
    	dataEntity.setHousingBenefitCase(hbce);
    	dataEntity.setHousingDataType(HousingDataType.EXPENSE);
    	dataEntity.setExpense(expense);
    	HousingDataJsonEntity savedEntity = housingDataJpaRepo.save(dataEntity);
    	expense.setId(savedEntity.getId());
    	return expense;
	}

	@Override
	public Optional<Expense> findById(Integer id) {
    	Optional<HousingDataJsonEntity> hdje = housingDataJpaRepo.findById(id);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Expense entity, HousingBenefitCaseEntity hbc) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterable<Expense> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Expense> findByHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Expense> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id) {
		Optional<HousingDataJsonEntity> hdje = housingDataJpaRepo.findByHousingBenefitCaseAndId(hbce, id);
		if(hdje.isPresent()) {
			Expense e = hdje.get().getExpense();
			e.setId(hdje.get().getId());
			return Optional.of(e);
		} else {
			return Optional.empty();
		}

	}

}
