package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import fi.rikusarlin.housingserver.api.CasesApiService;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.api.NotFoundException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.model.NewHousingBenefitCase;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

public class HousingBenefitCasesControllerImpl implements CasesApiService{
	
    @Inject
    CaseRepository caseRepo;
    @Inject
    HousingBenefitApplicationRepository hbaRepo;
    @Inject
    PersonRepository personRepo;
    @Inject
    Validator validator;
    
    @Override
	public Response fetchHousingBenefitCaseById(Integer caseId, SecurityContext securityContext) throws NotFoundException {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	return Response.ok(MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class)).build();
	}
 
    @Override
	public Response addHousingBenefitCase(NewHousingBenefitCase nhbc, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = new HousingBenefitCaseEntity();
    	MappingUtil.modelMapperInsert.map(nhbc, hbce);
		PersonEntity customer = personRepo.findById(nhbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException(nhbc.getCustomer().getId(), "Customer"));
		hbce.setCustomer(customer);
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		hbce.setApplication(null);
		HousingBenefitCaseEntity hbceSaved = caseRepo.save(hbce);
		HousingBenefitApplicationEntity hbae = new HousingBenefitApplicationEntity();
		MappingUtil.modelMapperInsert.map(nhbc.getApplication(), hbae);
		PersonEntity applicant = personRepo.findById(nhbc.getApplication().getApplicant().getId()).orElseThrow(() -> new NotFoundException(nhbc.getApplication().getApplicant().getId(), "Applicant"));
		hbae.setApplicant(applicant);
		hbae.setHousingBenefitCase(hbceSaved);
		HousingBenefitApplicationEntity hbaeSaved = hbaRepo.save(hbae);
		hbceSaved.setApplication(hbaeSaved);
		HousingBenefitCaseEntity hbceSaved2 = caseRepo.save(hbceSaved);
		HousingBenefitCase hbcSaved = MappingUtil.modelMapper.map(hbceSaved2, HousingBenefitCase.class);
		return Response.ok(hbcSaved).build();
	}

    @Override
	public Response checkHousingBenefitCase(Integer caseId, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit application"));
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}		
		return Response.ok(MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class)).build();
	}

    @Override
	public Response updateHousingBenefitCase(
			Integer caseId,
			HousingBenefitCase hbc, SecurityContext securityContext) throws NotFoundException{
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
						PersonEntity customer = personRepo.findById(hbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException(bc.getCustomer().getId(), "Customer"));
						value.setCustomer(customer);
						value = caseRepo.save(value);
					},
				()
				 	-> {
				 		caseRepo.save(hbce);
				 	});
		return fetchHousingBenefitCaseById(caseId, securityContext);
	}
	
    @Override
	public Response deleteHousingBenefitCase(Integer caseId, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	caseRepo.delete(hbce);
 		return Response.ok().build();
	}

    @Override
    public Response fetchHousingBenefitCases(SecurityContext securityContext) throws NotFoundException{
    	return Response.ok(
    			StreamSupport.stream(caseRepo.findAll().spliterator(), false)
    			.map(hbce -> MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class))
    			.collect(Collectors.toList())).build();
    }    

    @Override
    public Response fetchHousingBenefitCasesByPersonNumber(String personNumber, SecurityContext securityContext) throws NotFoundException{
    	return Response.ok(
    			StreamSupport.stream(caseRepo.findAllById(caseRepo.findByPersonNumber(personNumber)).spliterator(), false)
    			.map(hbce -> MappingUtil.modelMapper.map(hbce, HousingBenefitCase.class))
    			.collect(Collectors.toList())).build();
    }    

}
