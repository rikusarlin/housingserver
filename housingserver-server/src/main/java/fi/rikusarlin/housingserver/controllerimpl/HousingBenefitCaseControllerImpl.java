package fi.rikusarlin.housingserver.controllerimpl;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fi.rikusarlin.housingserver.api.CaseApi;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.model.NewHousingBenefitCase;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@Service
@Validated
public class HousingBenefitCaseControllerImpl implements CaseApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    CaseRepository caseRepo;
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;
    @Autowired
    PersonRepository personRepo;
    
    @Override
	public ResponseEntity<HousingBenefitCase> fetchHousingBenefitCaseById(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	return ResponseEntity.ok(MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class));
	}
 
    @Override
	public ResponseEntity<HousingBenefitCase> addHousingBenefitCase(NewHousingBenefitCase nhbc) {
    	HousingBenefitCaseEntity hbce = new HousingBenefitCaseEntity();
    	MappingUtil.modelMapperInsert.map(nhbc, hbce);
		PersonEntity customer = personRepo.findById(nhbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer", nhbc.getCustomer().getId()));
		hbce.setCustomer(customer);
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		hbce.setApplication(null);
		HousingBenefitCaseEntity hbceSaved = caseRepo.save(hbce);
		HousingBenefitApplicationEntity hbae = new HousingBenefitApplicationEntity();
		MappingUtil.modelMapperInsert.map(nhbc.getApplication(), hbae);
		PersonEntity applicant = personRepo.findById(nhbc.getApplication().getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", nhbc.getApplication().getApplicant().getId()));
		hbae.setApplicant(applicant);
		hbae.setHousingBenefitCase(hbceSaved);
		HousingBenefitApplicationEntity hbaeSaved = hbaRepo.save(hbae);
		hbceSaved.setApplication(hbaeSaved);
		HousingBenefitCaseEntity hbceSaved2 = caseRepo.save(hbceSaved);
		HousingBenefitCase hbcSaved = MappingUtil.modelMapper.map(hbceSaved2, HousingBenefitCase.class);
		return ResponseEntity.ok(hbcSaved);
	}

    @Override
	public ResponseEntity<HousingBenefitCase> checkHousingBenefitCase(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class));
	}

    @Override
	public ResponseEntity<HousingBenefitCase> updateHousingBenefitCase(
			Integer caseId,
			HousingBenefitCase hbc) {
		HousingBenefitCaseEntity hbce = MappingUtil.modelMapper.map(hbc, HousingBenefitCaseEntity.class);
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HousingBenefitCaseEntity> previousHbce = caseRepo.findById(caseId);
		previousHbce.ifPresentOrElse(
				(value) 
					-> {
						MappingUtil.modelMapper.map(hbc, value);
						PersonEntity customer = personRepo.findById(hbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer", hbc.getCustomer().getId()));
						value.setCustomer(customer);
						value = caseRepo.save(value);
					},
				()
				 	-> {
				 		caseRepo.save(hbce);
				 	});
		return fetchHousingBenefitCaseById(caseId);
	}
	
    @Override
	public ResponseEntity<Void> deleteHousingBenefitCase(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	caseRepo.delete(hbce);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
