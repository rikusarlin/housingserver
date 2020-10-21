package fi.rikusarlin.housingserver.jparepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.repository.IncomeRepository;

@Component("incomeRepositoryJpa")
public class IncomeRepositoryJpaImpl implements IncomeRepository {

    @Autowired
    private IncomeJpaRepository repo;

    public Income save(Income Income, HousingBenefitCaseEntity hbce) {
    	IncomeEntity ee = MappingUtil.modelMapper.map(Income, IncomeEntity.class);
    	ee.setHousingBenefitCase(hbce);
        return MappingUtil.modelMapper.map(repo.save(ee), Income.class);
    }


	@Override
	public Optional<Income> findById(Integer id) {
		Optional<IncomeEntity> Income = repo.findById(id);
		if(Income.isPresent()) {
			Income e = MappingUtil.modelMapper.map(Income.get(), Income.class);
			return Optional.of(e);
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Iterable<Income> findAll() {
		Iterable<IncomeEntity> Incomes = repo.findAll();
		return StreamSupport.stream(Incomes.spliterator(), false)
				.map(Income -> MappingUtil.modelMapper.map(Income, Income.class))
				.collect(Collectors.toList());
 	}


	@Override
	public void delete(Income Income, HousingBenefitCaseEntity hbce) {
		IncomeEntity ee = MappingUtil.modelMapper.map(Income, IncomeEntity.class);
		ee.setHousingBenefitCase(hbce);
		repo.delete(ee);
	}

	@Override
	public Iterable<Income> findAll(Sort sort) {
		Iterable<IncomeEntity> Incomes = repo.findAll(sort);
		// It might be better to do this using for-next loop rather than map-reduce to retain order
		List<Income> IncomeList = new ArrayList<Income>();
		for(IncomeEntity e:Incomes) {
			IncomeList.add(MappingUtil.modelMapper.map(e, Income.class));
		}
		return IncomeList;
	}

	@Override
	public List<Income> findByHousingBenefitCase(HousingBenefitCaseEntity hbce) {
		Iterable<IncomeEntity> Incomes = repo.findByHousingBenefitCase(hbce);
		return StreamSupport.stream(Incomes.spliterator(), false)
				.map(Income -> MappingUtil.modelMapper.map(Income, Income.class))
				.collect(Collectors.toList());

	}


	@Override
	public Optional<Income> findByHousingBenefitCaseAndId(HousingBenefitCaseEntity hbce, Integer id) {
		Optional<IncomeEntity> Income = repo.findByHousingBenefitCaseAndId(hbce, id);
		if(Income.isPresent()) {
			Income e = MappingUtil.modelMapper.map(Income.get(), Income.class);
			return Optional.of(e);
		} else {
			return Optional.empty();
		}
	}

}