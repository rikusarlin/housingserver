package fi.rikusarlin.housingserver.controllerimpl;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.ApplicationApi;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.repository.CaseRepository;
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
    HousingBenefitApplicationRepository hbaRepo;
    @Autowired
    CaseRepository caseRepo;
    @Autowired
    PersonRepository personRepo;
    

    @Override
	public ResponseEntity<HousingBenefitApplication> fetchApplication(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HousingBenefitApplicationEntity hbae = hbaRepo.findByHousingBenefitCase(hbce).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		PersonEntity p = personRepo.findById(hbae.getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hbae.getApplicant().getId()));
		hbae.setApplicant(p);
    	return ResponseEntity.ok(MappingUtil.modelMapper.map(hbae, HousingBenefitApplication.class));
	}
 
    @Override
	public ResponseEntity<HousingBenefitApplication> addApplication(Integer caseId, HousingBenefitApplication hba) {
       	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
        HousingBenefitApplicationEntity hbae = new HousingBenefitApplicationEntity();
        MappingUtil.modelMapperInsert.map(hba, hbae);
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		PersonEntity pe = personRepo.findById(hba.getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hba.getApplicant().getId()));
		hbae.setApplicant(pe);
		hbae.setHousingBenefitCase(hbce);
		HousingBenefitApplicationEntity hbaeSaved = hbaRepo.save(hbae);
		return ResponseEntity.ok(MappingUtil.modelMapper.map(hbaeSaved, HousingBenefitApplication.class));
	}

    @Override
	public ResponseEntity<HousingBenefitApplication> checkApplication(Integer caseId) {
       	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	HousingBenefitApplicationEntity hbae = hbaRepo.findByHousingBenefitCase(hbce).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(MappingUtil.modelMapper.map(hbae, HousingBenefitApplication.class));
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
		Optional<HousingBenefitApplicationEntity> previousHbae = hbaRepo.findById(caseId);
		previousHbae.ifPresentOrElse(
				(value) 
					-> {
						BeanUtils.copyProperties(hba, value, "id");
						PersonEntity pe = personRepo.findById(hba.getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hba.getApplicant().getId()));
						value.setApplicant(pe);
				 		value.setHousingBenefitCase(hbce);
						value = hbaRepo.save(value);
					},
				()
				 	-> {
				 		hbaRepo.save(hbae);
				 	});
		return fetchApplication(caseId);
	}
	
    @Override
	public ResponseEntity<Void> deleteApplication(Integer caseId) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
 		hbaRepo.delete(hba);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
