package fi.rikusarlin.housingserver.data;

import javax.validation.constraints.Min;

import fi.rikusarlin.housingserver.validation.ExpenseChecks;
import fi.rikusarlin.housingserver.validation.NotNullIfAnotherFieldHasValue;
import fi.rikusarlin.housingserver.validation.Severity;

@NotNullIfAnotherFieldHasValue(
	    fieldName = "expenseType",
	    fieldValue = "OTHER",
	    dependFieldName = "otherExpenseDescription",
	    groups=ExpenseChecks.class,
	    payload={Severity.Error.class})
public class Expense extends DateRangedEntity{
	ExpenseType expenseType;
	String otherExpenseDescription;
	@Min(value = 0, message = "Amount must be greater than zero", groups=ExpenseChecks.class, payload={Severity.Error.class})
    Double amount;
    
	public ExpenseType getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getOtherExpenseDescription() {
		return otherExpenseDescription;
	}
	public void setOtherExpenseDescription(String otherExpenseDescription) {
		this.otherExpenseDescription = otherExpenseDescription;
	}
}
