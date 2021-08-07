package fi.rikusarlin.housingserver.testdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import fi.rikusarlin.housingserver.model.CaseState;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.model.Income;

public class HousingBenefitCaseData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static HousingBenefitCase getCase1() {
		HousingBenefitCase hbc = new HousingBenefitCase();
    	hbc.setId(1);
    	
    	HousingBenefitApplication hba = new HousingBenefitApplication();
    	hba.setId(1);
    	hba.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hba.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hba.setApplicant(PersonData.getPerson1());;
    	hba.setReceived(LocalDateTime.parse("2020-10-12T07:29:44.907Z"));
    	
    	hbc.setCaseState(CaseState.ACTIVE);
    	hbc.setApplication(hba);
    	hbc.setCustomer(PersonData.getPerson1());
    	
    	hbc.setHousingExpenses(new ArrayList<Expense>());
    	hbc.setIncomes(new ArrayList<Income>());
    	hbc.setHouseholdMembers(new ArrayList<HouseholdMember>());
    	
    	hbc.getHousingExpenses().add(ExpenseData.getExpense1());
    	hbc.getHousingExpenses().add(ExpenseData.getExpense1());
    	//hbc.getIncomes().add(IncomeData.getIncome1());
    	//hbc.getIncomes().add(IncomeData.getIncome2());
    	hbc.getHouseholdMembers().add(HouseholdMemberData.getHouseholdMember1());
    	hbc.getHouseholdMembers().add(HouseholdMemberData.getHouseholdMember2());
    	hbc.getHouseholdMembers().add(HouseholdMemberData.getHouseholdMember3());
    	hbc.getHouseholdMembers().add(HouseholdMemberData.getHouseholdMember4());

    	return hbc;
	}
}
