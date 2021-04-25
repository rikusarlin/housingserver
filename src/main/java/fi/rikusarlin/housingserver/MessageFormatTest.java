package fi.rikusarlin.housingserver;

import java.text.MessageFormat;

public class MessageFormatTest {

	public static void main(String[] args) {
		String format = "startEndDate: start date must be less than end date if both are given, here start date is {0} and end date {1}";
		String[] data = {"01.09.2020","01.01.2020"};
		System.out.println("Format: "+format);
		for(String merkkijono:data) {
			System.out.println("data: "+merkkijono);
		}
		System.out.println("Muotoiltu viesti: "+ MessageFormat.format(format, (Object[]) data));
		
	}

}
