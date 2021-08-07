package fi.rikusarlin.housingserver.testdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import fi.rikusarlin.housingserver.model.Gender;
import fi.rikusarlin.housingserver.model.Person;


public class PersonData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static String userName = "User";

	public static Person getPerson1() {
		Person p = new Person();
		p.setId(1);
		p.setPersonNumber("010170-901K");
		p.setFirstName("Rauli");
		p.setLastName("Wnape");
		p.setBirthDate(LocalDate.parse("01.01.1970", formatter));
		p.setGender(Gender.MAN);
		p.setEmail("rauliwnape2@hotmail.com");
		p.setCreationTime(LocalDateTime.now());
		p.setCreatedByUser(userName);
		p.setModificationTime(LocalDateTime.now());
		p.setModifiedByUser(userName);
		return p;
	}
	
	public static Person getPerson2() {
		Person p = new Person();
		p.setId(2);
		p.setPersonNumber("010170-902L");
		p.setFirstName("Marke");
		p.setLastName("Peerakpe");
		p.setBirthDate(LocalDate.parse("01.01.1970", formatter));
		p.setGender(Gender.WOMAN);
		p.setEmail("marke.peerakpe@yahoo.com");
		p.setCreationTime(LocalDateTime.now());
		p.setCreatedByUser(userName);
		p.setModificationTime(LocalDateTime.now());
		p.setModifiedByUser(userName);
    	return p;
	}

	public static Person getPerson3() {
		Person p = new Person();
		p.setId(3);
		p.setPersonNumber("010170-903M");
		p.setFirstName("Walter");
		p.setLastName("Nutbekk");
		p.setBirthDate(LocalDate.parse("01.01.1970", formatter));
		p.setGender(Gender.MAN);
		p.setEmail("walter.nutbekk@welho.com");
		p.setCreationTime(LocalDateTime.now());
		p.setCreatedByUser(userName);
		p.setModificationTime(LocalDateTime.now());
		p.setModifiedByUser(userName);
		return p;
	}
	
	public static Person getPerson4() {
		Person p = new Person();
		p.setId(4);
		p.setPersonNumber("010170-904N");
		p.setFirstName("Suvi-Tuulia");
		p.setLastName("Retsetenpe");
		p.setBirthDate(LocalDate.parse("01.01.1970", formatter));
		p.setGender(Gender.WOMAN);
		p.setEmail("suvi-tuuli.retsenape@gmail.com");
		p.setCreationTime(LocalDateTime.now());
		p.setCreatedByUser(userName);
		p.setModificationTime(LocalDateTime.now());
		p.setModifiedByUser(userName);
		return p;
	}
}
