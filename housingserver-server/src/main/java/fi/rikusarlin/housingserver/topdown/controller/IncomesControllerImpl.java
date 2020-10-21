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

import fi.rikusarlin.housingserver.api.IncomesApi;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.jparepository.CaseRepository;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.repository.IncomeRepository;

@RestController
@Service
@Validated
public class IncomesControllerImpl implements IncomesApi {
	
    ModelMapper modelMapper = new ModelMapper();

	@Autowired
	@Qualifier("incomeRepositoryJson")
	IncomeRepository incomeRepo;
    @Autowired
    CaseRepository caseRepo;

    @Override
    public ResponseEntity<List<Income>> fetchIncomes(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	Iterable<Income> incomes = incomeRepo.findByHousingBenefitCase(hbce);
    	return ResponseEntity.ok(
    			StreamSupport.stream(incomes.spliterator(), false)
    			.collect(Collectors.toList()));
    }
}
