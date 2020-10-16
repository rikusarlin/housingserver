package fi.rikusarlin.housingserver.testdata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.model.IncomeType;

public class IncomeData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static Income getIncome1() {
    	Income i = new Income();
    	i.setId(1);
    	i.setStartDate(LocalDate.parse("01.08.2020", formatter));
    	i.setEndDate(LocalDate.parse("15.10.2020", formatter));
    	i.setAmount(1520.0);
    	i.setIncomeType(IncomeType.SALARY);
    	i.setOtherIncomeDescription(null);
    	return i;
	}
	
	public static Income getIncome2() {
    	Income i = new Income();
    	i.setId(2);
    	i.setStartDate(LocalDate.parse("01.08.2020", formatter));
    	i.setEndDate(LocalDate.parse("15.10.2020", formatter));
    	i.setAmount(270.2);
    	i.setIncomeType(IncomeType.OTHER);
    	i.setOtherIncomeDescription("Sold 10 liters of moonshine");
    	return i;
	}
}
