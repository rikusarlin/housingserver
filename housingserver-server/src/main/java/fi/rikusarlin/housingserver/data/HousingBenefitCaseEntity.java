package fi.rikusarlin.housingserver.data;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import fi.rikusarlin.housingserver.model.CaseState;
import fi.rikusarlin.housingserver.validation.ExpenseChecks;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.IncomeChecks;
import fi.rikusarlin.housingserver.validation.Severity;
import fi.rikusarlin.housingserver.validation.SubCollectionOverlappingDateRange;

@SubCollectionOverlappingDateRange.List({
	  @SubCollectionOverlappingDateRange(fieldName = "application", collectionName = "householdMembers", groups= {HouseholdChecks.class}, payload={Severity.Info.class}),
	  @SubCollectionOverlappingDateRange(fieldName = "application", collectionName = "incomes", groups= {IncomeChecks.class}, payload={Severity.Info.class}),
	  @SubCollectionOverlappingDateRange(fieldName = "application", collectionName = "housingExpenses", groups= {ExpenseChecks.class}, payload={Severity.Info.class})
})
@Entity
@Table(name = "cases")
public class HousingBenefitCaseEntity extends EntityClass{
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy="housingBenefitCase")
	Set<HouseholdMemberEntity> householdMembers = new LinkedHashSet<HouseholdMemberEntity>();
	
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy="housingBenefitCase")
	Set<IncomeEntity> incomes = new LinkedHashSet<IncomeEntity>();
	
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy="housingBenefitCase")
	Set<ExpenseEntity> housingExpenses = new LinkedHashSet<ExpenseEntity>();
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "housingBenefitCase")
    private HousingBenefitApplicationEntity application;
	
	@Valid
	@OneToOne
    @JoinColumn(name="customer_id", nullable=false)
    private PersonEntity customer;

	@Basic
	@Valid
    private CaseState caseState;
	
	public HousingBenefitCaseEntity() {
	}

	public Set<HouseholdMemberEntity> getHouseholdMembers() {
		return householdMembers;
	}

	public void setHouseholdMembers(Set<HouseholdMemberEntity> householdMembers) {
		this.householdMembers = householdMembers;
	}

	public Set<IncomeEntity> getIncomes() {
		return incomes;
	}

	public void setIncomes(Set<IncomeEntity> incomes) {
		this.incomes = incomes;
	}

	public Set<ExpenseEntity> getHousingExpenses() {
		return housingExpenses;
	}

	public void setHousingExpenses(Set<ExpenseEntity> housingExpenses) {
		this.housingExpenses = housingExpenses;
	}

	public HousingBenefitApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(HousingBenefitApplicationEntity application) {
		this.application = application;
	}

	public PersonEntity getCustomer() {
		return customer;
	}

	public void setCustomer(PersonEntity customer) {
		this.customer = customer;
	}

	public CaseState getCaseState() {
		return caseState;
	}

	public void setCaseState(CaseState caseState) {
		this.caseState = caseState;
	}
}
