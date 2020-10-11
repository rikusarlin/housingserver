package fi.rikusarlin.housingserver.controllerimpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.HousingsApi;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;

@RestController
@Validated
public class HousingBenefitApplicationsControllerImpl implements HousingsApi{
	
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;
    
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public ResponseEntity<List<HousingBenefitApplication>> fetchHousingBenefitApplications() {
    	return ResponseEntity.ok(
    			StreamSupport.stream(hbaRepo.findAll().spliterator(), false)
    			.map(hba -> modelMapper.map(hba, HousingBenefitApplication.class))
    			.collect(Collectors.toList()));
    }    

}
