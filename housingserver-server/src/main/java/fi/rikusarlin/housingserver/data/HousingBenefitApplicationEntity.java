package fi.rikusarlin.housingserver.data;

import java.util.HashSet;
import java.util.Set;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SubCollectionOverlappingDateRange.List({
  @SubCollectionOverlappingDateRange(collectionName = "householdMembers", groups= {HouseholdChecks.class}, payload={Severity.Info.class}),
  @SubCollectionOverlappingDateRange(collectionName = "incomes", groups= {IncomeChecks.class}, payload={Severity.Info.class}),
  @SubCollectionOverlappingDateRange(collectionName = "housingExpenses", groups= {ExpenseChecks.class}, payload={Severity.Info.class})
})
@Entity
@Table(name="application")
@NoArgsConstructor
@Getter
@Setter
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
	
}
