package fi.rikusarlin.housingserver.topdown.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.IncomesApi;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.repository.HousingBenefitCaseRepository;
import fi.rikusarlin.housingserver.repository.IncomeRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class IncomesControllerImpl implements IncomesApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    @Qualifier("housingBenefitCaseRepositoryJson")
    HousingBenefitCaseRepository caseRepo;
	@Autowired
	@Qualifier("incomeRepositoryJson")
	IncomeRepository incomeRepo;

	@Override
	public ResponseEntity<Income> fetchIncomeById(Integer caseId, Integer id) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Income i = incomeRepo.findByHousingBenefitCaseIdAndId(caseId, id).orElseThrow(() -> new NotFoundException("Income", id));
		return ResponseEntity.ok(i);
	}
 
	@Override
	public ResponseEntity<Income> addIncome(Integer caseId, Income income) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		IncomeEntity ie = MappingUtil.modelMapperInsert.map(income, IncomeEntity.class);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(incomeRepo.save(income, caseId));
	}

	@Override
	public ResponseEntity<Income> checkIncomeById(Integer caseId, Integer id) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	Income i = incomeRepo.findByHousingBenefitCaseIdAndId(caseId, id).orElseThrow(() -> new NotFoundException("Income", id));
		IncomeEntity ie = MappingUtil.modelMapperInsert.map(i, IncomeEntity.class);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(i);
	}

	@Override
	public ResponseEntity<Income> updateIncome(Integer caseId, Integer id, Income income) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		IncomeEntity ie = MappingUtil.modelMapper.map(income, IncomeEntity.class);
		ie.setId(id);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		income.setId(id);
		return ResponseEntity.ok(incomeRepo.save(income, caseId));
	}
	
	@Override
	public ResponseEntity<Void> deleteIncome(Integer caseId, Integer id) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		incomeRepo.findByHousingBenefitCaseIdAndId(caseId, id).orElseThrow(() -> new NotFoundException("Income", id));
 		incomeRepo.delete(id);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

    @Override
    public ResponseEntity<List<Income>> fetchIncomes(Integer caseId) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	Iterable<Income> incomes = incomeRepo.findByHousingBenefitCaseId(caseId);
    	return ResponseEntity.ok(
    			StreamSupport.stream(incomes.spliterator(), false)
    			.collect(Collectors.toList()));
    }
}
