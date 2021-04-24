package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.BeanUtils;

import fi.rikusarlin.housingserver.api.ApplicationsApiService;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.api.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

public class HousingBenefitApplicationControllerImpl implements ApplicationsApiService {
	
    @Inject
    HousingBenefitApplicationRepository hbaRepo;
    @Inject
    CaseRepository caseRepo;
    @Inject
    PersonRepository personRepo;
    @Inject
    Validator validator;
    

    @Override
	public Response fetchApplication(Integer caseId, SecurityContext securityContext) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	HousingBenefitApplicationEntity hbae = hbaRepo.findByHousingBenefitCase(hbce).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		PersonEntity p = personRepo.findById(hbae.getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hbae.getApplicant().getId()));
		hbae.setApplicant(p);
    	return Response.ok(MappingUtil.modelMapper.map(hbae, HousingBenefitApplication.class)).build();
	}
 
    @Override
	public Response addApplication(Integer caseId, HousingBenefitApplication hba, SecurityContext securityContext) {
       	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
        HousingBenefitApplicationEntity hbae = new HousingBenefitApplicationEntity();
        MappingUtil.modelMapperInsert.map(hba, hbae);
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		PersonEntity pe = personRepo.findById(hba.getApplicant().getId()).orElseThrow(() -> new NotFoundException(hba.getApplicant().getId(), "Applicant"));
		hbae.setApplicant(pe);
		hbae.setHousingBenefitCase(hbce);
		HousingBenefitApplicationEntity hbaeSaved = hbaRepo.save(hbae);
		return Response.ok(MappingUtil.modelMapper.map(hbaeSaved, HousingBenefitApplication.class)).build();
	}

    @Override
	public Response checkApplication(Integer caseId, SecurityContext securityContext) {
       	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	HousingBenefitApplicationEntity hbae = hbaRepo.findByHousingBenefitCase(hbce).orElseThrow(() -> new NotFoundException(hbce.getApplication().getId(), "Housing benefit application"));
		Set<ConstraintViolation<HousingBenefitApplicationEntity>> violations =  validator.validate(hbae, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return Response.ok(MappingUtil.modelMapper.map(hbae, HousingBenefitApplication.class)).build();
	}

    @Override
	public Response updateApplication(
			Integer caseId,
			HousingBenefitApplication hba, SecurityContext securityContext) {
       	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
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
						PersonEntity pe = personRepo.findById(hba.getApplicant().getId()).orElseThrow(() -> new NotFoundException(hba.getApplicant().getId(), "Applicant"));
						value.setApplicant(pe);
				 		value.setHousingBenefitCase(hbce);
						value = hbaRepo.save(value);
					},
				()
				 	-> {
				 		hbaRepo.save(hbae);
				 	});
		return fetchApplication(caseId, securityContext);
	}
	
    @Override
	public Response deleteApplication(Integer caseId, SecurityContext securityContext) {
    	HousingBenefitApplicationEntity hba = hbaRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit application"));
 		hbaRepo.delete(hba);
 		return Response.ok().build();
	}

}
