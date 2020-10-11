package fi.rikusarlin.housingserver.data;

import java.util.HashSet;
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
import fi.rikusarlin.housingserver.validation.ValidApplicationDataRanges;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ValidApplicationDataRanges(groups= {HouseholdChecks.class,IncomeChecks.class,ExpenseChecks.class},payload={Severity.Info.class})
@Entity
@Table(name = "cases")
@NoArgsConstructor
@Getter
@Setter
public class HousingBenefitCaseEntity extends EntityClass{
	@Valid
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="housingBenefitCase")
	Set<HouseholdMemberEntity> householdMembers = new HashSet<HouseholdMemberEntity>();
	
	@Valid
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="housingBenefitCase")
	Set<IncomeEntity> incomes = new HashSet<IncomeEntity>();
	
	@Valid
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="housingBenefitCase")
	Set<ExpenseEntity> housingExpenses = new HashSet<ExpenseEntity>();
	
	@Valid
	@OneToOne(mappedBy = "housingBenefitCase")
    private HousingBenefitApplicationEntity application;
	
	@Valid
	@OneToOne
    @JoinColumn(name="customer_id", nullable=false)
    private PersonEntity customer;

	@Basic
	@Valid
    private CaseState caseState;

}
