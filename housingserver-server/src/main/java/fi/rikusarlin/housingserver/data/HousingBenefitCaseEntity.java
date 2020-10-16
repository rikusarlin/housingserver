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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SubCollectionOverlappingDateRange.List({
	  @SubCollectionOverlappingDateRange(fieldName = "application", collectionName = "householdMembers", groups= {HouseholdChecks.class}, payload={Severity.Info.class}),
	  @SubCollectionOverlappingDateRange(fieldName = "application", collectionName = "incomes", groups= {IncomeChecks.class}, payload={Severity.Info.class}),
	  @SubCollectionOverlappingDateRange(fieldName = "application", collectionName = "housingExpenses", groups= {ExpenseChecks.class}, payload={Severity.Info.class})
})
@Entity
@Table(name = "cases")
@NoArgsConstructor
@Getter
@Setter
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
}
