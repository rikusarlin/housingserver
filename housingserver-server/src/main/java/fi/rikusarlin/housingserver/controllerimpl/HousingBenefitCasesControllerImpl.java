package fi.rikusarlin.housingserver.controllerimpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.CasesApi;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.repository.CaseRepository;

@RestController
@Validated
public class HousingBenefitCasesControllerImpl implements CasesApi{
	
    @Autowired
    CaseRepository caseRepo;
    
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public ResponseEntity<List<HousingBenefitCase>> fetchHousingBenefitCases() {
    	return ResponseEntity.ok(
    			StreamSupport.stream(caseRepo.findAll().spliterator(), false)
    			.map(hbce -> modelMapper.map(hbce, HousingBenefitCase.class))
    			.collect(Collectors.toList()));
    }    

    @Override
    public ResponseEntity<List<HousingBenefitCase>> fetchHousingBenefitCasesByPersonNumber(String personNumber) {
    	return ResponseEntity.ok(
    			StreamSupport.stream(caseRepo.findAllById(caseRepo.findByPersonNumber(personNumber)).spliterator(), false)
    			.map(hbce -> modelMapper.map(hbce, HousingBenefitCase.class))
    			.collect(Collectors.toList()));
    }    

}
