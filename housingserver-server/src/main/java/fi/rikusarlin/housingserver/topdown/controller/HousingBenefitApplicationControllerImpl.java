package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Set;

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

import fi.rikusarlin.housingserver.api.ApplicationApi;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.jparepository.CaseRepository;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class HousingBenefitApplicationControllerImpl implements ApplicationApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    CaseRepository caseRepo;
    @Autowired
	@Qualifier("housingBenefitApplicationRepositoryJson")
    HousingBenefitApplicationRepository hbaRepo;
    @Autowired
	@Qualifier("personRepositoryJpa")
    PersonRepository personRepo;
    

    @Override
	public ResponseEntity<HousingBenefitApplication> fetchApplication(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HousingBenefitApplication hba = hbaRepo.findByHousingBenefitCase(hbce).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Person p = personRepo.findById(hba.getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hba.getApplicant().getId()));
		hba.setApplicant(p);
    	return ResponseEntity.ok(hba);
	}
 
    @Override
	public ResponseEntity<HousingBenefitApplication> addApplication(Integer caseId, HousingBenefitApplication hba) {
       	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
        HousingBenefitApplicationEntity hbae = MappingUtil.modelMapperInsert.map(hba, HousingBenefitApplicationEntity.class);
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Person p = personRepo.findById(hba.getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hba.getApplicant().getId()));
		hba.setApplicant(p);
		return ResponseEntity.ok(hbaRepo.save(hba, hbce));
	}

    @Override
	public ResponseEntity<HousingBenefitApplication> checkApplication(Integer caseId) {
       	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HousingBenefitApplication hba = hbaRepo.findByHousingBenefitCase(hbce).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
        HousingBenefitApplicationEntity hbae = MappingUtil.modelMapperInsert.map(hba, HousingBenefitApplicationEntity.class);
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(hba);
	}

    @Override
	public ResponseEntity<HousingBenefitApplication> updateApplication(
			Integer caseId,
			HousingBenefitApplication hba) {
       	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HousingBenefitApplicationEntity hbae = MappingUtil.modelMapper.map(hba, HousingBenefitApplicationEntity.class);
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(hbaRepo.save(hba, hbce));
	}
	
    @Override
	public ResponseEntity<Void> deleteApplication(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HousingBenefitApplication hba = hbaRepo.findByHousingBenefitCase(hbce).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
 		hbaRepo.delete(hba, hbce);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
