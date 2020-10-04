package fi.rikusarlin.housingserver.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

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
public class HousingBenefitApplication extends DateRangedEntity {
	@Valid
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="application")
	List<HouseholdMember> householdMembers = new ArrayList<HouseholdMember>();
	@Valid
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="application")
	List<Income> incomes = new ArrayList<Income>();
	@Valid
	@OneToMany(cascade=CascadeType.REMOVE, mappedBy="application")
	List<Expense> housingExpenses = new ArrayList<Expense>();
	
	public List<HouseholdMember> getHouseholdMembers() {
		return householdMembers;
	}
	public void setHouseholdMembers(List<HouseholdMember> householdMembers) {
		this.householdMembers = householdMembers;
	}
	public void addHouseholdMember(HouseholdMember hm) {
		this.householdMembers.add(hm);		
	}
	public List<Income> getIncomes() {
		return incomes;
	}
	public void setIncomes(List<Income> incomes) {
		this.incomes = incomes;
	}
	public void addIncome(Income income) {
		this.incomes.add(income);		
	}
	public List<Expense> getHousingExpenses() {
		return housingExpenses;
	}
	public void setHousingExpenses(List<Expense> housingExpenses) {
		this.housingExpenses = housingExpenses;
	}
	public void addExpense(Expense expense) {
		this.housingExpenses.add(expense);		
	}
	
}
