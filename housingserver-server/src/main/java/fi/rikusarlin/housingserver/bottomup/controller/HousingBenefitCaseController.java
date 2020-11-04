package fi.rikusarlin.housingserver.bottomup.controller;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.ApiErrorMessage;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.IncomeRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Validated
public class HousingBenefitCaseController {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    CaseRepository caseRepo;
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;
    @Autowired
    HouseholdMemberRepository hmRepo;
    @Autowired
    IncomeRepository incomeRepo;
    @Autowired
    ExpenseRepository expenseRepo;
    @Autowired
    PersonRepository personRepo;
    
    @GetMapping("/api/v1/housings")
    public @ResponseBody Iterable<HousingBenefitCaseEntity> findHousingBenefitCases() {
        return caseRepo.findAll();
    }

    @GetMapping("/api/v1/housings/{personNumber}")
    public @ResponseBody Iterable<HousingBenefitCaseEntity> findHousingBenefitCasesForPerson(
    		 @PathVariable String personNumber) {
    	return caseRepo.findAllById(caseRepo.findByPersonNumber(personNumber));
    }

	@GetMapping(value = "/api/v1/housing/{caseId}")
	public HousingBenefitCaseEntity findHousingBenefitCaseById(
			@PathVariable int caseId) {
    	return caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
	}
 
	@ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "add housing benefit case based on application", operationId = "addHousingBenefitCase", tags={ "developers"})
	@ApiResponses(value = {
		      @ApiResponse(responseCode = "201", description = "housing benefit case and application created", content = @Content(schema = @Schema(implementation = HousingBenefitCaseEntity.class))),
		      @ApiResponse(responseCode = "400", description = "housing benefit case and application creation not ok", content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))),
		      @ApiResponse(responseCode = "404", description = "person referenced in application or case not found", content = @Content(schema = @Schema(implementation = ApiErrorMessage.class))) })
    @PostMapping(value = "/api/v2/housing/cases",
        produces = { "application/json" }, 
        consumes = { "application/json" })
	public HousingBenefitCaseEntity addHousingBenefitCase(
			@RequestBody @Validated(InputChecks.class) HousingBenefitCaseEntity hbc) {
		// This deletes the potential ids
		//HousingBenefitCaseEntity hbc = MappingUtil.modelMapperInsert.map(hbcInput, HousingBenefitCaseEntity.class);
		PersonEntity customer = personRepo.findById(hbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer", hbc.getCustomer().getId()));
		hbc.setCustomer(customer);
		for(HouseholdMemberEntity hm:hbc.getHouseholdMembers()) {
			PersonEntity p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
			hm.setHousingBenefitCase(hbc);
			hm.setPerson(p);
		}
		for(ExpenseEntity e:hbc.getHousingExpenses()) {
			e.setHousingBenefitCase(hbc);
		}
		for(IncomeEntity i:hbc.getIncomes()) {
			i.setHousingBenefitCase(hbc);
		}
		hbc.getApplication().setHousingBenefitCase(hbc);
		PersonEntity applicant = personRepo.findById(hbc.getApplication().getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hbc.getApplication().getApplicant().getId()));
		hbc.getApplication().setApplicant(applicant);
		hbc.getApplication().setHousingBenefitCase(hbc);
		HousingBenefitCaseEntity hbcSaved = caseRepo.save(hbc);
		return hbcSaved;
	}

	@GetMapping(value = "/api/v1/housing/{caseId}/check")
	public HousingBenefitCaseEntity checkHousingCaseApplication(
			@PathVariable int caseId) {
		HousingBenefitCaseEntity hbc = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbc, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return hbc;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/api/v1/housing/{caseId}")
	public HousingBenefitCaseEntity updateHousingBenefitCase(
			@PathVariable int caseId,
			@RequestBody @Validated(InputChecks.class) HousingBenefitCaseEntity hbc) {
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbc, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HousingBenefitCaseEntity> previousHbc = caseRepo.findById(caseId);
		previousHbc.ifPresentOrElse(
				(value) 
					-> {
						BeanUtils.copyProperties(hbc, value, "id");
						BeanUtils.copyProperties(hbc.getApplication(), value.getApplication(), "id");
						PersonEntity customer = personRepo.findById(hbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer", hbc.getCustomer().getId()));
						value.setCustomer(customer);
						PersonEntity applicant = personRepo.findById(hbc.getApplication().getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hbc.getApplication().getApplicant().getId()));
						value.getApplication().setApplicant(applicant);
						value.getApplication().setHousingBenefitCase(value);
						value.setHouseholdMembers(hbc.getHouseholdMembers());
				 		value.setHousingExpenses(hbc.getHousingExpenses());
				 		value.setIncomes(hbc.getIncomes());
						value = caseRepo.save(value);
						hbaRepo.save(value.getApplication());
						for(HouseholdMemberEntity hm:hbc.getHouseholdMembers()) {
							PersonEntity p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
							hm.setHousingBenefitCase(value);
							hm.setPerson(p);
							hmRepo.save(hm);
						}
						for(ExpenseEntity e:hbc.getHousingExpenses()) {
							e.setHousingBenefitCase(value);
							expenseRepo.save(e);
						}
						for(IncomeEntity i:hbc.getIncomes()) {
							i.setHousingBenefitCase(value);
							incomeRepo.save(i);
						}
					},
				()
				 	-> {
				 		caseRepo.save(hbc);
				 	});
		return findHousingBenefitCaseById(caseId);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/api/v1/housing/{caseId}")
	public void deleteHousingBenefitCase(
			@PathVariable int caseId) {
    	HousingBenefitCaseEntity hbc = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	caseRepo.delete(hbc);
	}

}
