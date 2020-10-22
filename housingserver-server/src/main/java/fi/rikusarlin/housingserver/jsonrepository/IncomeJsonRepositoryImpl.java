package fi.rikusarlin.housingserver.jsonrepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.json.HousingBenefitCaseJsonEntity;
import fi.rikusarlin.housingserver.data.json.IncomeJsonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.repository.IncomeRepository;

@Component("incomeRepositoryJson")
public class IncomeJsonRepositoryImpl implements IncomeRepository {

	@Autowired
    private IncomeJsonRepository incomeJsonRepo;
	@Autowired
    private HousingBenefitCaseJsonRepository caseRepo;
    
	@Override
	public Income save(Income Income, Integer caseId) {
       	HousingBenefitCaseJsonEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	IncomeJsonEntity dataEntity = MappingUtil.modelMapper.map(Income, IncomeJsonEntity.class);
    	dataEntity.setHousingBenefitCase(hbce);
    	dataEntity.setIncome(Income);
    	IncomeJsonEntity savedEntity = incomeJsonRepo.save(dataEntity);
    	Income.setId(savedEntity.getId());
    	return Income;
	}

	@Override
	public Optional<Income> findById(Integer id) {
    	Optional<IncomeJsonEntity> hdje = incomeJsonRepo.findById(id);
		if(hdje.isPresent()) {
			Income e = hdje.get().getIncome();
			e.setId(hdje.get().getId());
			return Optional.of(e);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<Income> findAll() {
		Iterable<IncomeJsonEntity> Incomes = incomeJsonRepo.findAll();
		return StreamSupport.stream(Incomes.spliterator(), false)
				.map(Income -> {
					Income e = MappingUtil.modelMapper.map(Income.getIncome(), Income.class);
					e.setId(Income.getId());
					return e;
				})
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) {
		IncomeJsonEntity hdje = incomeJsonRepo.findById(id).orElseThrow(() -> new NotFoundException("Income", id));
		hdje.getHousingBenefitCase().getIncomes().remove(hdje);
    	incomeJsonRepo.delete(hdje);
	}

	@Override
	public Iterable<Income> findAll(Sort sort) {
		Iterable<IncomeJsonEntity> Incomes = incomeJsonRepo.findAll(sort);
		List<Income> IncomeList = new ArrayList<Income>();
		for(IncomeJsonEntity Income:Incomes) {
			Income e = MappingUtil.modelMapper.map(Income.getIncome(), Income.class);
			e.setId(Income.getId());
			IncomeList.add(e);
		}
		return IncomeList;
	}

	@Override
	public List<Income> findByHousingBenefitCaseId(Integer caseId) {
       	HousingBenefitCaseJsonEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Iterable<IncomeJsonEntity> Incomes = incomeJsonRepo.findByHousingBenefitCase(hbce);
		return StreamSupport.stream(Incomes.spliterator(), false)
				.map(Income -> {
					Income e = MappingUtil.modelMapper.map(Income.getIncome(), Income.class);
					e.setId(Income.getId());
					return e;
				})
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Income> findByHousingBenefitCaseIdAndId(Integer caseId, Integer id) {
       	HousingBenefitCaseJsonEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Optional<IncomeJsonEntity> hdje = incomeJsonRepo.findByHousingBenefitCaseAndId(hbce, id);
		if(hdje.isPresent()) {
			Income e = hdje.get().getIncome();
			e.setId(hdje.get().getId());
			return Optional.of(e);
		} else {
			return Optional.empty();
		}
	}
}
