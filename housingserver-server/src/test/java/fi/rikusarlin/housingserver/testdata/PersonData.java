package fi.rikusarlin.housingserver.testdata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fi.rikusarlin.housingserver.model.Gender;
import fi.rikusarlin.housingserver.model.Person;

public class PersonData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static Person getPerson1() {
		Person person1 = new Person();
    	person1.setId(1);
    	person1.setPersonNumber("010170-901K");
    	person1.setFirstName("Rauli");
    	person1.setLastName("Wnape");
    	person1.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	person1.setGender(Gender.MAN); 
    	person1.setEmail("rauliwnape2@hotmail.com");
    	return person1;
	}
	
	public static Person getPerson2() {
		Person person2 = new Person();
    	person2.setId(2);
    	person2.setPersonNumber("010170-902L");
    	person2.setFirstName("Marke");
    	person2.setLastName("Peerakpe");
    	person2.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	person2.setGender(Gender.WOMAN); 
    	person2.setEmail("marke.peerakpe@yahoo.com");
    	return person2;
	}

	public static Person getPerson3() {
		Person person3 = new Person();
    	person3.setId(3);
    	person3.setPersonNumber("010170-903M");
    	person3.setFirstName("Walter");
    	person3.setLastName("Nutbekk");
    	person3.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	person3.setGender(Gender.MAN); 
    	person3.setEmail("walter.nutbekk@welho.com");
    	return person3;
	}
	
	public static Person getPerson4() {
		Person person4 = new Person();
    	person4.setId(4);
    	person4.setPersonNumber("010170-904N");
    	person4.setFirstName("Suvi-Tuulia");
    	person4.setLastName("Retsetenpe");
    	person4.setBirthDate(LocalDate.parse("01.01.1970", formatter));
    	person4.setGender(Gender.WOMAN);
    	person4.setEmail("suvi-tuuli.retsenape@gmail.com");
    	return person4;
	}
}
