package fi.rikusarlin.housingserver.bottomup.testdata;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitApplicationEntity;
import fi.rikusarlin.housingserver.data.HousingBenefitCaseEntity;
import fi.rikusarlin.housingserver.data.IncomeEntity;
import fi.rikusarlin.housingserver.data.CaseState;

public class HousingBenefitCaseData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static HousingBenefitCaseEntity getCase1() {
		HousingBenefitCaseEntity hbc = new HousingBenefitCaseEntity();
    	hbc.setId(1);
    	
    	HousingBenefitApplicationEntity hba = new HousingBenefitApplicationEntity();
    	hba.setId(1);
    	hba.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hba.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hba.setApplicant(PersonData.getPerson1());;
    	hba.setReceived(OffsetDateTime.parse("2020-10-12T07:29:44.907Z"));
    	hba.setHousingBenefitCase(hbc);
    	hbc.setApplication(hba);
    	
    	hbc.setCaseState(CaseState.ACTIVE);
    	hbc.setCustomer(PersonData.getPerson1());
    	
    	hbc.setHousingExpenses(new LinkedHashSet<ExpenseEntity>());
    	hbc.setIncomes(new LinkedHashSet<IncomeEntity>());
    	hbc.setHouseholdMembers(new LinkedHashSet<HouseholdMemberEntity>());
    	
    	
    	ExpenseData.getExpense1().setHousingBenefitCase(hbc);
    	hbc.getHousingExpenses().add(ExpenseData.getExpense1());
    	ExpenseData.getExpense2().setHousingBenefitCase(hbc);
    	hbc.getHousingExpenses().add(ExpenseData.getExpense2());
    	IncomeData.getIncome1().setHousingBenefitCase(hbc);
    	hbc.getIncomes().add(IncomeData.getIncome1());
    	IncomeData.getIncome2().setHousingBenefitCase(hbc);
    	hbc.getIncomes().add(IncomeData.getIncome2());
    	HouseholdMemberData.getHouseholdMember1().setHousingBenefitCase(hbc);
    	hbc.getHouseholdMembers().add(HouseholdMemberData.getHouseholdMember1());
    	HouseholdMemberData.getHouseholdMember2().setHousingBenefitCase(hbc);
    	hbc.getHouseholdMembers().add(HouseholdMemberData.getHouseholdMember2());
    	HouseholdMemberData.getHouseholdMember3().setHousingBenefitCase(hbc);
    	hbc.getHouseholdMembers().add(HouseholdMemberData.getHouseholdMember3());
    	HouseholdMemberData.getHouseholdMember4().setHousingBenefitCase(hbc);
    	hbc.getHouseholdMembers().add(HouseholdMemberData.getHouseholdMember4());

    	return hbc;
	}
}
