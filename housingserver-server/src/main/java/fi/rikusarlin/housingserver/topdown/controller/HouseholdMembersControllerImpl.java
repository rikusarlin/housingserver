package fi.rikusarlin.housingserver.topdown.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.HouseholdmembersApi;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.jparepository.CaseRepository;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;

@RestController
@Service
@Validated
public class HouseholdMembersControllerImpl implements HouseholdmembersApi {
	
	ModelMapper modelMapper = new ModelMapper();

    @Autowired
    CaseRepository caseRepo;
    @Autowired
	@Qualifier("householdMemberRepositoryJson")
    HouseholdMemberRepository householdMemberRepo;
    
    @Override
    public ResponseEntity<List<HouseholdMember>> fetchHouseholdMembers(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	Iterable<HouseholdMember> householdMembers = householdMemberRepo.findByHousingBenefitCase(hbce);
    	return ResponseEntity.ok(
    			StreamSupport.stream(householdMembers.spliterator(), false)
    			.collect(Collectors.toList()));
    }
 }
