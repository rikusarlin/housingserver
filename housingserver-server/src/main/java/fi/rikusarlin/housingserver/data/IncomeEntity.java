package fi.rikusarlin.housingserver.data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.rikusarlin.housingserver.model.IncomeType;
import fi.rikusarlin.housingserver.validation.IncomeChecks;
import fi.rikusarlin.housingserver.validation.InputChecks;
import fi.rikusarlin.housingserver.validation.NotNullIfAnotherFieldHasValue;
import fi.rikusarlin.housingserver.validation.Severity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NotNullIfAnotherFieldHasValue(
	    fieldName = "incomeType",
	    fieldValue = "OTHER",
	    dependFieldName = "otherIncomeDescription",
	    groups=IncomeChecks.class,
	    payload={Severity.Error.class})
@Entity
@Table(name = "income")
@NoArgsConstructor
@Getter
@Setter
public class IncomeEntity extends DateRangedEntity{
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="case_id", nullable=false)
    HousingBenefitCaseEntity housingBenefitCase;

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
}
