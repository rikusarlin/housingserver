package fi.rikusarlin.housingserver.topdown.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.IncomesApi;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.IncomeRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
@RequestMapping("/api/v2/housing")
public class IncomesControllerImpl implements IncomesApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    CaseRepository caseRepo;
    @Autowired
    IncomeRepository incomeRepo;


	@Override
	public ResponseEntity<Income> fetchIncomeById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		IncomeEntity ie = incomeRepo.findById(id).orElseThrow(() -> new NotFoundException("Income", id));
		ie.setHousingBenefitCase(hbce);
		return ResponseEntity.ok(MappingUtil.modelMapper.map(ie, Income.class));
	}
 
	@Override
	public ResponseEntity<Income> addIncome(Integer caseId, Income income) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		IncomeEntity ie = new IncomeEntity();
		MappingUtil.modelMapperInsert.map(income, ie);
		ie.setHousingBenefitCase(hbce);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(MappingUtil.modelMapper.map(incomeRepo.save(ie), Income.class));
	}

	@Override
	public ResponseEntity<Income> checkIncomeById(Integer caseId, Integer id) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	IncomeEntity ie = incomeRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Income", id));
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return fetchIncomeById(id, caseId);
	}

	@Override
	public ResponseEntity<Income> updateIncome(Integer caseId, Integer id, Income income) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		IncomeEntity ie = MappingUtil.modelMapper.map(income, IncomeEntity.class);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<IncomeEntity> previousIncome = incomeRepo.findByHousingBenefitCaseAndId(hbce, id);
		previousIncome.ifPresentOrElse(
			(value) 
				-> {
					MappingUtil.modelMapper.map(income, value);
					value.setHousingBenefitCase(hbce);
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
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		IncomeEntity ie = incomeRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Income", id));
 		incomeRepo.delete(ie);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}


    @Override
    public ResponseEntity<List<Income>> fetchIncomes(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	Iterable<IncomeEntity> incomes = incomeRepo.findByHousingBenefitCase(hbce);
    	return ResponseEntity.ok(
    			StreamSupport.stream(incomes.spliterator(), false)
    			.map(ie -> MappingUtil.modelMapper.map(ie, Income.class))
    			.collect(Collectors.toList()));
    }
}
