package fi.rikusarlin.housingserver.data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.model.IncomeType;
import fi.rikusarlin.housingserver.validation.IncomeChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;
import fi.rikusarlin.housingserver.validation.NotNullIfAnotherFieldHasValue;
import fi.rikusarlin.housingserver.validation.Severity;

@NotNullIfAnotherFieldHasValue(
	    fieldName = "incomeType",
	    fieldValue = "OTHER",
	    dependFieldName = "otherIncomeDescription",
	    groups=IncomeChecks.class,
	    payload={Severity.Error.class})
@Entity
@Table(name = "income")
public class IncomeEntity extends DateRangedEntity{
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="application_id", nullable=false)
    private HousingBenefitApplicationEntity application;
	@Basic
    @Column(name = "incomeType")	
	IncomeType incomeType;
	@Basic
    @Column(name = "otherIncomeDescription")	
	String otherIncomeDescription;
	@Basic
    @Column(name = "amount", nullable=false)	
	@Min(value = 0, message = "Amount must be greater than zero", groups=InputChecks.class, payload={Severity.Error.class})
    Double amount;
	
	public IncomeEntity() {
	}

	public IncomeEntity(Income income) {
		this.id = income.getId();
		this.startDate = income.getStartDate();
		this.endDate = income.getEndDate();
		this.amount = income.getAmount();
		this.incomeType = income.getIncomeType();
		this.otherIncomeDescription = income.getOtherIncomeDescription();
	}

	public Income toIncome() {
		Income income = new Income();
		income.setId(this.id);
		income.setStartDate(this.startDate);
		income.setEndDate(this.endDate);
		income.setAmount(this.amount);
		income.setIncomeType(this.incomeType);
		income.setOtherIncomeDescription(this.otherIncomeDescription);
		return income;
	}

    
	public IncomeType getIncomeType() {
		return incomeType;
	}
	public void setIncomeType(IncomeType incomeType) {
		this.incomeType = incomeType;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getOtherIncomeDescription() {
		return otherIncomeDescription;
	}
	public void setOtherIncomeDescription(String otherIncomeDescription) {
		this.otherIncomeDescription = otherIncomeDescription;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public HousingBenefitApplicationEntity getApplication() {
		return application;
	}
	public void setApplication(HousingBenefitApplicationEntity application) {
		this.application = application;
	}

}
