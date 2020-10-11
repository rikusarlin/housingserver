package fi.rikusarlin.housingserver.controllerimpl;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import fi.rikusarlin.housingserver.api.CaseApi;
import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.data.PersonEntity;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.ExpenseRepository;
import fi.rikusarlin.housingserver.repository.HouseholdMemberRepository;
import fi.rikusarlin.housingserver.repository.HousingBenefitApplicationRepository;
import fi.rikusarlin.housingserver.repository.IncomeRepository;
import fi.rikusarlin.housingserver.repository.PersonRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@RestController
@Service
@Validated
public class HousingBenefitCaseControllerImpl implements CaseApi {
	
	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    CaseRepository caseRepo;
    @Autowired
    HousingBenefitApplicationRepository hbaRepo;
    @Autowired
    HouseholdMemberRepository householdMemberRepo;
    @Autowired
    IncomeRepository incomeRepo;
    @Autowired
    ExpenseRepository expenseRepo;
    @Autowired
    PersonRepository personRepo;
    
    @Override
	public ResponseEntity<HousingBenefitCase> fetchHousingBenefitCaseById(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	return ResponseEntity.ok(modelMapper.map(hbce, HousingBenefitCase.class));
	}
 
    @Override
	public ResponseEntity<HousingBenefitCase> addHousingBenefitCase(HousingBenefitCase hbc) {
    	HousingBenefitCaseEntity hbce = new HousingBenefitCaseEntity();
    	BeanUtils.copyProperties(hbc, hbce, "id");
		PersonEntity customer = personRepo.findById(hbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer", hbc.getCustomer().getId()));
		hbce.setCustomer(customer);
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		HousingBenefitCaseEntity hbceSaved = caseRepo.save(hbce);
		HousingBenefitApplicationEntity hbae = new HousingBenefitApplicationEntity();
    	BeanUtils.copyProperties(hbc.getHousingBenefitApplication(), hbae, "id");
		PersonEntity applicant = personRepo.findById(hbc.getHousingBenefitApplication().getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hbc.getHousingBenefitApplication().getApplicant().getId()));
		hbae.setApplicant(applicant);
		hbaRepo.save(hbae);
		for(HouseholdMember hm:hbc.getHouseholdMembers()) {
			PersonEntity p = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
	    	HouseholdMemberEntity hme = new HouseholdMemberEntity();
	    	BeanUtils.copyProperties(hm, hme, "id");
			hme.setHousingBenefitCase(hbceSaved);
			hme.setPerson(p);
			householdMemberRepo.save(hme);
			hbce.getHouseholdMembers().add(hme);
		}
		for(Expense e:hbc.getHousingExpenses()) {
			ExpenseEntity ee = new ExpenseEntity();
			BeanUtils.copyProperties(e, ee, "id");
			ee.setHousingBenefitCase(hbceSaved);
			expenseRepo.save(ee);
			hbce.getHousingExpenses().add(ee);
		}
		for(Income i:hbc.getIncomes()) {
			IncomeEntity ie = new IncomeEntity();
			BeanUtils.copyProperties(i, ie, "id");
			ie.setHousingBenefitCase(hbceSaved);
			incomeRepo.save(ie);
			hbce.getIncomes().add(ie);
		}
		return ResponseEntity.ok(modelMapper.map(hbceSaved, HousingBenefitCase.class));
	}

    @Override
	public ResponseEntity<HousingBenefitCase> checkHousingBenefitCase(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit application", caseId));
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return ResponseEntity.ok(modelMapper.map(hbce, HousingBenefitCase.class));
	}

    @Override
	public ResponseEntity<HousingBenefitCase> updateHousingBenefitCase(
			Integer caseId,
			HousingBenefitCase hbc) {
		HousingBenefitCaseEntity hbce = modelMapper.map(hbc, HousingBenefitCaseEntity.class);
		Set<ConstraintViolation<HousingBenefitCaseEntity>> violations =  validator.validate(hbce, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<HousingBenefitCaseEntity> previousHbce = caseRepo.findById(caseId);
		previousHbce.ifPresentOrElse(
				(value) 
					-> {
						BeanUtils.copyProperties(hbc, value, "id");
						PersonEntity customer = personRepo.findById(hbc.getCustomer().getId()).orElseThrow(() -> new NotFoundException("Customer", hbc.getCustomer().getId()));
						value.setCustomer(customer);
						value = caseRepo.save(value);

						HousingBenefitApplicationEntity hbae = new HousingBenefitApplicationEntity();
				    	BeanUtils.copyProperties(hbc.getHousingBenefitApplication(), hbae, "id");
						PersonEntity applicant = personRepo.findById(hbc.getHousingBenefitApplication().getApplicant().getId()).orElseThrow(() -> new NotFoundException("Applicant", hbc.getHousingBenefitApplication().getApplicant().getId()));
						hbae.setApplicant(applicant);
						hbaRepo.save(hbae);
						
						// Remove all children and add those received in parameter
						value.getHouseholdMembers().removeAll(value.getHouseholdMembers());
						for(HouseholdMember hm:hbc.getHouseholdMembers()) {
							HouseholdMemberEntity hme = new HouseholdMemberEntity();
					    	BeanUtils.copyProperties(hm, hme, "id");
							PersonEntity pe = personRepo.findById(hm.getPerson().getId()).orElseThrow(() -> new NotFoundException("Person", hm.getPerson().getId()));
							hme.setHousingBenefitCase(value);
							hme.setPerson(pe);
							value.getHouseholdMembers().add(hme);
							householdMemberRepo.save(hme);
						}
						value.getHousingExpenses().removeAll(value.getHousingExpenses());
						for(Expense e:hbc.getHousingExpenses()) {
							ExpenseEntity ee = new ExpenseEntity();
					    	BeanUtils.copyProperties(e, ee, "id");
							ee.setHousingBenefitCase(value);
							value.getHousingExpenses().add(ee);
							expenseRepo.save(ee);
						}
						value.getIncomes().removeAll(value.getIncomes());
						for(Income i:hbc.getIncomes()) {
							IncomeEntity ie = new IncomeEntity();
					    	BeanUtils.copyProperties(i, ie, "id");
							ie.setHousingBenefitCase(value);
							value.getIncomes().add(ie);
							incomeRepo.save(ie);
						}
					},
				()
				 	-> {
				 		caseRepo.save(hbce);
				 	});
		return fetchHousingBenefitCaseById(caseId);
	}
	
    @Override
	public ResponseEntity<Void> deleteHousingBenefitCase(Integer caseId) {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException("Housing benefit case", caseId));
    	caseRepo.delete(hbce);
 		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
