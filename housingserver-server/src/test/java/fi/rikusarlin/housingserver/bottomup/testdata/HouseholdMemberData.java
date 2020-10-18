package fi.rikusarlin.housingserver.bottomup.testdata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fi.rikusarlin.housingserver.data.HouseholdMemberEntity;

public class HouseholdMemberData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static HouseholdMemberEntity getHouseholdMember1() {
    	HouseholdMemberEntity hm = new HouseholdMemberEntity();
    	hm.setId(1);
    	hm.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm.setPerson(PersonData.getPerson1());
    	return hm;
	}
	
	public static HouseholdMemberEntity getHouseholdMember2() {
    	HouseholdMemberEntity hm = new HouseholdMemberEntity();
    	hm.setId(2);
    	hm.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm.setPerson(PersonData.getPerson2());
    	return hm;
	}

	public static HouseholdMemberEntity getHouseholdMember3() {
    	HouseholdMemberEntity hm = new HouseholdMemberEntity();
    	hm.setId(3);
    	hm.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm.setPerson(PersonData.getPerson3());
    	return hm;
	}
	
	public static HouseholdMemberEntity getHouseholdMember4() {
    	HouseholdMemberEntity hm = new HouseholdMemberEntity();
    	hm.setId(4);
    	hm.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	hm.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	hm.setPerson(PersonData.getPerson4());
    	return hm;
	}
}
