package fi.rikusarlin.housingserver.controllerimpl;

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
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.HousingApi;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.IncomeRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class IncomeControllerImpl implements HousingApi{
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    IncomeRepository incomeRepo;
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;

    @Override
    public ResponseEntity<List<Income>> fetchIncomes(Integer caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	Iterable<IncomeEntity> incomes = incomeRepo.findByApplication(hba);
    	return ResponseEntity.ok(
    			StreamSupport.stream(incomes.spliterator(), false)
    			.map(income -> income.toIncome())
    			.collect(Collectors.toList()));
    }

	@Override
	public ResponseEntity<Income> fetchIncomeById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = incomeRepo.findById(id).orElseThrow(() -> new NotFoundException("Income", id));
		ie.setApplication(hba);
		return ResponseEntity.ok(ie.toIncome());
	}
 
	@Override
	public ResponseEntity<Income> addIncome(Integer caseId, Income income) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = new IncomeEntity(income);
		ie.setApplication(hba);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(incomeRepo.save(ie).toIncome());
	}

	@Override
	public ResponseEntity<Income> checkIncomeById(Integer caseId, Integer id) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	IncomeEntity ie = incomeRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Income", id));
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return fetchIncomeById(id, caseId);
	}

	@Override
	public ResponseEntity<Income> updateIncome(Integer caseId, Integer id, Income income) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = new IncomeEntity(income);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<IncomeEntity> previousIncome = incomeRepo.findByApplicationAndId(hba, id);
		previousIncome.ifPresentOrElse(
			(value) 
				-> {
					value.setStartDate(income.getStartDate());
					value.setEndDate(income.getEndDate());
					value.setAmount(income.getAmount());
					value.setIncomeType(income.getIncomeType());
					value.setOtherIncomeDescription(income.getOtherIncomeDescription());
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
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		IncomeEntity ie = incomeRepo.findByApplicationAndId(hba, id).orElseThrow(() -> new NotFoundException("Income", id));
 		incomeRepo.delete(ie);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
