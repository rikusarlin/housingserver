package fi.rikusarlin.housingserver.topdown.controller;

import java.time.Period;
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

import fi.rikusarlin.housingserver.api.HouseholdmembersApiService;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.api.NotFoundException;
import fi.rikusarlin.housingserver.exception.TooLongRangeException;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

public class HouseholdMembersControllerImpl implements HouseholdmembersApiService {
	
	@Inject
    CaseRepository caseRepo;
    @Inject
    HouseholdMemberRepository householdMemberRepo;
    @Inject
    PersonRepository personRepo;
    @Inject
    Validator validator;

    @Override
	public Response fetchHouseholdMemberById(Integer caseId, Integer id, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
		HouseholdMemberEntity hme = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
		return Response.ok(MappingUtil.modelMapper.map(hme, HouseholdMember.class)).build();
	}
 
    @Override
	public Response addHouseholdMember(
			Integer caseId,
			HouseholdMember hm, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	PersonEntity pe = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
    	HouseholdMemberEntity hme = new HouseholdMemberEntity();
    	MappingUtil.modelMapperInsert.map(hm,  hme);
    	hme.setHousingBenefitCase(hbce);
    	hme.setPerson(pe);
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		hme.setId(null);
		return Response.ok(MappingUtil.modelMapper.map(householdMemberRepo.save(hme), HouseholdMember.class)).build();
	}

	/**
	 * Cross validate a household member
	 * Note how Spring validation checks and manual checks are combined
	 */
    @Override
	public Response checkHouseholdMemberById(Integer caseId, Integer id, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
		HouseholdMemberEntity hme = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
		Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, HouseholdChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		/*
		 * Period max one year in this case
		 * Yes, we could have written a validator for this, too, but wanted to show
		 * manual checks are added to Spring Validation checks
		 */
		if(hme.getStartDate() != null && hme.getEndDate() != null){
			Period  period = Period.between(hme.getStartDate(), hme.getEndDate());
			if(period.getYears() >= 1) {
				throw new TooLongRangeException(hme.getStartDate(), hme.getEndDate());
			}
		}
		return Response.ok(MappingUtil.modelMapper.map(hme, HouseholdMember.class)).build();
	}

	
    @Override
	public Response updateHouseholdMember(
			Integer caseId, 
			Integer id, 
			HouseholdMember hm, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
		HouseholdMemberEntity hme = MappingUtil.modelMapper.map(hm, HouseholdMemberEntity.class);
    	Set<ConstraintViolation<HouseholdMemberEntity>> violations =  validator.validate(hme, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HouseholdMemberEntity> previousHme = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id);
		previousHme.ifPresentOrElse(
				(value) 
					-> {
						MappingUtil.modelMapper.map(hm, value);
				 		value.setHousingBenefitCase(hbce);
						householdMemberRepo.save(value);
					},
				()
				 	-> {
				 		householdMemberRepo.save(hme);
				 	});
		return fetchHouseholdMemberById(caseId, id, securityContext);
	}

    @Override
	public Response deleteHouseholdMember(
			Integer caseId, 
			Integer id, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	HouseholdMemberEntity hm = householdMemberRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException("Household member", id));
 		householdMemberRepo.delete(hm);
 		return Response.ok().build();
	}
    
    @Override
    public Response fetchHouseholdMembers(Integer caseId, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	Iterable<HouseholdMemberEntity> householdMembers = householdMemberRepo.findByHousingBenefitCase(hbce);
    	return Response.ok(
    			StreamSupport.stream(householdMembers.spliterator(), false)
    			.map(hm -> MappingUtil.modelMapper.map(hm, HouseholdMember.class))
    			.collect(Collectors.toList())).build();
    }
 }
