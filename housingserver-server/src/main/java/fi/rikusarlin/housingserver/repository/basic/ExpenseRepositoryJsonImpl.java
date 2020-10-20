package fi.rikusarlin.housingserver.repository.basic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.HousingDataJsonEntity;
import fi.rikusarlin.housingserver.data.HousingDataType;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;

@Component("expenseRepositoryJson")
public class ExpenseRepositoryJsonImpl implements ExpenseRepository {

	@Autowired
    private HousingDataJpaRepository housingDataJpaRepo;

    ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public Expense save(Expense expense, HousingBenefitCase hbc) {
    	HousingBenefitCaseEntity hbce = MappingUtil.modelMapper.map(hbc, HousingBenefitCaseEntity.class);
    	HousingDataJsonEntity dataEntity = MappingUtil.modelMapper.map(expense, HousingDataJsonEntity.class);
    	String json;
    	try {
			json = objectMapper.writeValueAsString(expense);
		} catch (JsonProcessingException e) {
			json = "{}";
		}
    	dataEntity.setHousingBenefitCase(hbce);
    	dataEntity.setHousingDataType(HousingDataType.EXPENSE);
    	dataEntity.setJsonData(json);
    	HousingDataJsonEntity savedEntity = housingDataJpaRepo.save(dataEntity);
    	expense.setId(savedEntity.getId());
    	return expense;
	}

	@Override
	public Optional<Expense> findById(Integer id) {
    	Optional<HousingDataJsonEntity> hdje = housingDataJpaRepo.findById(id);
		if(hdje.isPresent()) {
			Expense e;
			try {
				e = objectMapper.readValue(hdje.get().getJsonData(), Expense.class);
			} catch (JsonProcessingException e1) {
				// Mayhaps we get at least something from here?
				e = MappingUtil.modelMapper.map(hdje.get(), Expense.class);
			}
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
	public void delete(Expense entity, HousingBenefitCase hbc) {
		// TODO Auto-generated method stub

	}

	@Override
	public Iterable<Expense> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Expense> findByHousingBenefitCase(HousingBenefitCase housingBenefitCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Expense> findByHousingBenefitCaseAndId(HousingBenefitCase housingBenefitCase, Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
