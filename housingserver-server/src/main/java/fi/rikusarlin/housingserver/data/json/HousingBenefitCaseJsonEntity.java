package fi.rikusarlin.housingserver.data.json;

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import fi.rikusarlin.housingserver.data.EntityClass;
import fi.rikusarlin.housingserver.data.PersonEntity;
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
public class HousingBenefitCaseJsonEntity extends EntityClass{
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy="housingBenefitCase")
	Set<HouseholdMemberJsonEntity> householdMembers = new LinkedHashSet<HouseholdMemberJsonEntity>();
	
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy="housingBenefitCase")
	Set<ExpenseJsonEntity> housingExpenses = new LinkedHashSet<ExpenseJsonEntity>();

	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy="housingBenefitCase")
	Set<IncomeJsonEntity> incomes = new LinkedHashSet<IncomeJsonEntity>();
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL, mappedBy="housingBenefitCase")
    private HousingBenefitApplicationJsonEntity application;
	
	@Valid
	@OneToOne
    @JoinColumn(name="customer_id", nullable=false)
    private PersonEntity customer;

	@Basic
	@Valid
    private CaseState caseState;
	
	public HousingBenefitCaseJsonEntity() {
	}

	public Set<HouseholdMemberJsonEntity> getHouseholdMembers() {
		return householdMembers;
	}

	public void setHouseholdMembers(Set<HouseholdMemberJsonEntity> householdMembers) {
		this.householdMembers = householdMembers;
	}

	public Set<IncomeJsonEntity> getIncomes() {
		return incomes;
	}

	public void setIncomes(Set<IncomeJsonEntity> incomes) {
		this.incomes = incomes;
	}

	public Set<ExpenseJsonEntity> getHousingExpenses() {
		return housingExpenses;
	}

	public void setHousingExpenses(Set<ExpenseJsonEntity> housingExpenses) {
		this.housingExpenses = housingExpenses;
	}

	public HousingBenefitApplicationJsonEntity getApplication() {
		return application;
	}

	public void setApplication(HousingBenefitApplicationJsonEntity application) {
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
