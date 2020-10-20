package fi.rikusarlin.housingserver.topdown.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.ExpensesApi;
import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.repository.basic.CaseRepository;
import fi.rikusarlin.housingserver.repository.basic.ExpenseJpaRepository;

@RestController
@Service
@Validated
public class ExpensesControllerImpl implements ExpensesApi {
	
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    ExpenseJpaRepository expenseRepo;
    @Autowired
    CaseRepository caseRepo;
    
    @Override
    public ResponseEntity<List<Expense>> fetchExpenses(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	Iterable<ExpenseEntity> ees = expenseRepo.findByHousingBenefitCase(hbce);
    	return ResponseEntity.ok(
    			StreamSupport.stream(ees.spliterator(), false)
    			.map(ee -> modelMapper.map(ee, Expense.class))
    			.collect(Collectors.toList()));
    }
}
