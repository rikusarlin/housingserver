package fi.rikusarlin.housingserver.topdown.controller;

import java.util.ArrayList;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.CasesApi;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.model.NewHousingBenefitCase;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitCaseRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Validated
public class HousingBenefitCasesControllerImpl implements CasesApi{
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    @Qualifier("housingBenefitCaseRepositoryJson")
    HousingBenefitCaseRepository caseRepo;
    @Autowired
    @Qualifier("housingBenefitApplicationRepositoryJson")
    HousingBenefitApplicationRepository hbaRepo;
    @Autowired
	@Qualifier("personRepositoryJpa")
    PersonRepository personRepo;
    
    @Override
	public ResponseEntity<HousingBenefitCase> fetchHousingBenefitCaseById(Integer caseId) {
    	HousingBenefitCase hbc = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	return ResponseEntity.ok(hbc);
	}
 
    @Override
	public ResponseEntity<HousingBenefitCase> addHousingBenefitCase(NewHousingBenefitCase nhbc) {
    	HousingBenefitCase hbc = MappingUtil.modelMapperInsert.map(nhbc, HousingBenefitCase.class);
		Person customer = personRepo.findById(hbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer", hbc.getCustomer().getId()));
		hbc.setCustomer(customer);
		HousingBenefitCaseEntity hbce = MappingUtil.modelMapperInsert.map(hbc, HousingBenefitCaseEntity.class);
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		HousingBenefitApplication hba = hbc.getApplication();
		Person applicant = personRepo.findById(hba.getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hba.getApplicant().getId()));
		hbc.setApplication(null);
		HousingBenefitCase hbcSaved = caseRepo.save(hbc);
		hba.setApplicant(applicant);
		HousingBenefitApplication hbaSaved = hbaRepo.save(hbcSaved.getId(), hba);
		hbcSaved.setApplication(hbaSaved);
		return ResponseEntity.ok(hbcSaved);
	}

    @Override
	public ResponseEntity<HousingBenefitCase> checkHousingBenefitCase(Integer caseId) {
    	HousingBenefitCase hbc = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
    	HousingBenefitCaseEntity hbce = MappingUtil.modelMapper.map(hbc, HousingBenefitCaseEntity.class); 
    	Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}		
		return ResponseEntity.ok(hbc);
	}

    @Override
	public ResponseEntity<HousingBenefitCase> updateHousingBenefitCase(
			Integer caseId,
			HousingBenefitCase hbc) {
		HousingBenefitCaseEntity hbce = MappingUtil.modelMapper.map(hbc, HousingBenefitCaseEntity.class);
		Person customer = personRepo.findById(hbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer", hbc.getCustomer().getId()));
		hbc.setCustomer(customer);
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		HousingBenefitApplication hba = hbc.getApplication();
		Person applicant = personRepo.findById(hba.getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hba.getApplicant().getId()));
		hbc.setApplication(null);
		HousingBenefitCase hbcSaved = caseRepo.save(hbc);
		hba.setApplicant(applicant);
		HousingBenefitApplication hbaSaved = hbaRepo.save(hbcSaved.getId(), hba);
		hbcSaved.setApplication(hbaSaved);
		return ResponseEntity.ok(hbcSaved);
	}
	
    @Override
	public ResponseEntity<Void> deleteHousingBenefitCase(Integer caseId) {
    	caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	caseRepo.delete(caseId);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

    @Override
    public ResponseEntity<List<HousingBenefitCase>> fetchHousingBenefitCases() {
    	return ResponseEntity.ok(
    			StreamSupport.stream(caseRepo.findAll().spliterator(), false)
    			.collect(Collectors.toList()));
    }    

    @Override
    public ResponseEntity<List<HousingBenefitCase>> fetchHousingBenefitCasesByPersonNumber(String personNumber) {
    	List<Integer> ids = caseRepo.findByPersonNumber(personNumber);
    	List<HousingBenefitCase> hbcList = new ArrayList<HousingBenefitCase>();
    	for(Integer id:ids) {
    		hbcList.add(caseRepo.findById(id).get());
    	}
    	return ResponseEntity.ok(hbcList);
    	/* for reasons unknown findAllById produces a StackOverflow
    	return ResponseEntity.ok(
    			StreamSupport.stream(caseRepo.findAllById(caseRepo.findByPersonNumber(personNumber)).spliterator(), false)
    			.collect(Collectors.toList()));
    	*/
    }    
}
