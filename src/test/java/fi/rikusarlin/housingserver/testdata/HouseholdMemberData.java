package fi.rikusarlin.housingserver.testdata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fi.rikusarlin.housingserver.model.HouseholdMember;

public class HouseholdMemberData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static HouseholdMember getHouseholdMember1() {
    	HouseholdMember hm = new HouseholdMember();
    	hm.setId(1);
    	hm.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm.setPerson(PersonData.getPerson1());
    	return hm;
	}
	
	public static HouseholdMember getHouseholdMember2() {
    	HouseholdMember hm = new HouseholdMember();
    	hm.setId(2);
    	hm.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm.setPerson(PersonData.getPerson2());
    	return hm;
	}

	public static HouseholdMember getHouseholdMember3() {
    	HouseholdMember hm = new HouseholdMember();
    	hm.setId(3);
    	hm.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm.setPerson(PersonData.getPerson3());
    	return hm;
	}
	
	public static HouseholdMember getHouseholdMember4() {
    	HouseholdMember hm = new HouseholdMember();
    	hm.setId(4);
    	hm.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm.setPerson(PersonData.getPerson4());
    	return hm;
	}
}
