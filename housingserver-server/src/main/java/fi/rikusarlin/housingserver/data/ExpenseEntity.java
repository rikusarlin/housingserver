package fi.rikusarlin.housingserver.data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.rikusarlin.housingserver.model.ExpenseType;
import fi.rikusarlin.housingserver.validation.ExpenseChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;
import fi.rikusarlin.housingserver.validation.NotNullIfAnotherFieldHasValue;
import fi.rikusarlin.housingserver.validation.Severity;

@NotNullIfAnotherFieldHasValue(
	    fieldName = "expenseType",
	    fieldValue = "OTHER",
	    dependFieldName = "otherExpenseDescription",
	    groups=ExpenseChecks.class,
	    payload={Severity.Error.class})
@Entity
@Table(name = "expense")
public class ExpenseEntity extends DateRangedEntity{
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name="case_id", nullable=false)
    HousingBenefitCaseEntity housingBenefitCase;
	
	@Basic
    @Column(name = "expenseType")	
	ExpenseType expenseType;
	
	@Basic
    @Column(name = "otherExpenseDescription")	
	String otherExpenseDescription;
	
	@Basic
    @Column(name = "amount", nullable=false)	
	@Min(value = 0, message = "Amount must be greater than zero", groups=InputChecks.class, payload={Severity.Error.class})
    Double amount;
	
	public ExpenseEntity() {
	}

	public HousingBenefitCaseEntity getHousingBenefitCase() {
		return housingBenefitCase;
	}

	public void setHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase) {
		this.housingBenefitCase = housingBenefitCase;
	}

	public ExpenseType getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}

	public String getOtherExpenseDescription() {
		return otherExpenseDescription;
	}

	public void setOtherExpenseDescription(String otherExpenseDescription) {
		this.otherExpenseDescription = otherExpenseDescription;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
