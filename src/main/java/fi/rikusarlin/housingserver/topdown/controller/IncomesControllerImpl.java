package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import fi.rikusarlin.housingserver.api.IncomesApiService;
import fi.rikusarlin.housingserver.api.NotFoundException;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.mapping.MappingUtil;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.repository.CaseRepository;
import fi.rikusarlin.housingserver.repository.IncomeRepository;
import fi.rikusarlin.housingserver.validation.AllChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;

@ApplicationScoped
public class IncomesControllerImpl implements IncomesApiService {
	

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Inject
    CaseRepository caseRepo;
    @Inject
    IncomeRepository incomeRepo;

	@Override
	public Response fetchIncomeById(Integer caseId, Integer id, SecurityContext securityContext) throws NotFoundException {
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
		IncomeEntity ie = incomeRepo.findById(id).orElseThrow(() -> new NotFoundException(id, "Income"));
		ie.setHousingBenefitCase(hbce);
		return Response.ok(MappingUtil.modelMapper.map(ie, Income.class)).build();
	}
 
	@Override
	public Response addIncome(Integer caseId, Income income, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
		IncomeEntity ie = new IncomeEntity();
		MappingUtil.modelMapperInsert.map(income, ie);
		ie.setHousingBenefitCase(hbce);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return Response.ok(MappingUtil.modelMapper.map(incomeRepo.save(ie), Income.class)).build();
	}

	@Override
	public Response checkIncomeById(Integer caseId, Integer id, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	IncomeEntity ie = incomeRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException(id, "Income"));
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, AllChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		return fetchIncomeById(id, caseId, securityContext);
	}

	@Override
	public Response updateIncome(Integer caseId, Integer id, Income income, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
		IncomeEntity ie = MappingUtil.modelMapper.map(income, IncomeEntity.class);
		Set<ConstraintViolation<IncomeEntity>> violations =  validator.validate(ie, InputChecks.class);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		Optional<IncomeEntity> previousIncome = incomeRepo.findByHousingBenefitCaseAndId(hbce, id);
		previousIncome.ifPresentOrElse(
			(value) 
				-> {
					MappingUtil.modelMapper.map(income, value);
					value.setHousingBenefitCase(hbce);
					incomeRepo.save(value);
				},
			()
				-> {
			 		incomeRepo.save(ie);
				});
		return fetchIncomeById(caseId, id, securityContext);
	}
	
	@Override
	public Response deleteIncome(Integer caseId, Integer id, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
		IncomeEntity ie = incomeRepo.findByHousingBenefitCaseAndId(hbce, id).orElseThrow(() -> new NotFoundException(id, "Income"));
 		incomeRepo.delete(ie);
 		return Response.ok().build();
	}


    @Override
    public Response fetchIncomes(Integer caseId, SecurityContext securityContext) throws NotFoundException{
    	HousingBenefitCaseEntity hbce = caseRepo.findById(caseId).orElseThrow(() -> new NotFoundException(caseId, "Housing benefit case"));
    	Iterable<IncomeEntity> incomes = incomeRepo.findByHousingBenefitCase(hbce);
    	return Response.ok(
    			StreamSupport.stream(incomes.spliterator(), false)
    			.map(ie -> MappingUtil.modelMapper.map(ie, Income.class))
    			.collect(Collectors.toList())).build();
    }
}
