package fi.rikusarlin.housingserver.bottomup.testdata;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import fi.rikusarlin.housingserver.data.ExpenseEntity;
import fi.rikusarlin.housingserver.data.ExpenseType;

public class ExpenseData {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static ExpenseEntity getExpense1() {
    	ExpenseEntity e = new ExpenseEntity();
    	e.setId(1);
    	e.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	e.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	e.setAmount(782.52);
    	e.setExpenseType(ExpenseType.RENT);
    	e.setOtherExpenseDescription(null);
    	return e;
	}
	
	public static ExpenseEntity getExpense2() {
    	ExpenseEntity e = new ExpenseEntity();
    	e.setId(2);
    	e.setStartDate(LocalDate.parse("01.09.2020", formatter));
    	e.setEndDate(LocalDate.parse("01.10.2020", formatter));
    	e.setAmount(782.52);
    	e.setExpenseType(ExpenseType.OTHER);
    	e.setOtherExpenseDescription("Futter for my bed");
    	return e;
	}
}
