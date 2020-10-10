package fi.rikusarlin.housingserver.controllerimpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.HousingsApi;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;

@RestController
@Validated
public class HousingBenefitApplicationsControllerImpl implements HousingsApi{
	
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;

    @Override
    public ResponseEntity<List<HousingBenefitApplication>> fetchHousingBenefitApplications() {
    	Iterable<HousingBenefitApplicationEntity> hbas = hbaRepo.findAll();
    	return ResponseEntity.ok(
    			StreamSupport.stream(hbas.spliterator(), false)
    			.map(hba -> hba.toHousingBenefitApplication())
    			.collect(Collectors.toList()));
    }    

}
