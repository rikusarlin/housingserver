package fi.rikusarlin.housingserver.data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.validation.ExpenseChecks;
import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.IncomeChecks;
import fi.rikusarlin.housingserver.validation.Severity;
import fi.rikusarlin.housingserver.validation.SubCollectionOverlappingDateRange;

@SubCollectionOverlappingDateRange.List({
  @SubCollectionOverlappingDateRange(collectionName = "householdMembers", groups= {HouseholdChecks.class}, payload={Severity.Info.class}),
  @SubCollectionOverlappingDateRange(collectionName = "incomes", groups= {IncomeChecks.class}, payload={Severity.Info.class}),
  @SubCollectionOverlappingDateRange(collectionName = "housingExpenses", groups= {ExpenseChecks.class}, payload={Severity.Info.class})
})
@Entity
@Table(name="application")
public class HousingBenefitApplicationEntity extends DateRangedEntity {
	@Valid
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="application")
	Set<HouseholdMemberEntity> householdMembers = new HashSet<HouseholdMemberEntity>();
	@Valid
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="application")
	Set<IncomeEntity> incomes = new HashSet<IncomeEntity>();
	@Valid
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="application")
	Set<ExpenseEntity> housingExpenses = new HashSet<ExpenseEntity>();
	
	public HousingBenefitApplicationEntity() {
	}

	public HousingBenefitApplicationEntity(HousingBenefitApplication hba) {
		this.id = hba.getId();
		this.startDate = hba.getStartDate();
		this.endDate = hba.getEndDate();
		for(Income income:hba.getIncomes()) {
			IncomeEntity ie = new IncomeEntity(income);
			ie.setApplication(this);
			this.addIncome(ie);
		}
		for(Expense expense:hba.getHousingExpenses()) {
			ExpenseEntity ee = new ExpenseEntity(expense);
			ee.setApplication(this);
			this.addExpense(ee);
		}
		for(HouseholdMember hm:hba.getHouseholdMembers()) {
			HouseholdMemberEntity hme = new HouseholdMemberEntity(hm);
			hme.setApplication(this);
			this.addHouseholdMember(hme);
		}
	}

	public HousingBenefitApplication toHousingBenefitApplication() {
		HousingBenefitApplication hba = new HousingBenefitApplication();
		hba.setId(this.id);
		hba.setStartDate(this.startDate);
		hba.setEndDate(this.endDate);
		hba.setIncomes(this.getIncomes()
				.stream()
				.map(ie -> ie.toIncome())
				.collect(Collectors.toList()));
		hba.setHousingExpenses(this.getHousingExpenses()
				.stream()
				.map(ee -> ee.toExpense())
				.collect(Collectors.toList()));
		hba.setHouseholdMembers(this.getHouseholdMembers()
				.stream()
				.map(hme -> hme.toHouseholdMember())
				.collect(Collectors.toList()));
		return hba;
	}
	
	public Set<HouseholdMemberEntity> getHouseholdMembers() {
		return householdMembers;
	}
	public void setHouseholdMembers(Set<HouseholdMemberEntity> householdMembers) {
		this.householdMembers = householdMembers;
	}
	public void addHouseholdMember(HouseholdMemberEntity hm) {
		this.householdMembers.add(hm);		
	}
	public Set<IncomeEntity> getIncomes() {
		return incomes;
	}
	public void setIncomes(Set<IncomeEntity> incomes) {
		this.incomes = incomes;
	}
	public void addIncome(IncomeEntity income) {
		this.incomes.add(income);		
	}
	public Set<ExpenseEntity> getHousingExpenses() {
		return housingExpenses;
	}
	public void setHousingExpenses(Set<ExpenseEntity> housingExpenses) {
		this.housingExpenses = housingExpenses;
	}
	public void addExpense(ExpenseEntity expense) {
		this.housingExpenses.add(expense);		
	}
	
}
