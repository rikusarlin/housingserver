package fi.rikusarlin.housingserver.data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.rikusarlin.housingserver.validation.InputChecks;
import fi.rikusarlin.housingserver.validation.Severity;
import fi.rikusarlin.housingserver.validation.ValidPersonNumber;

@Entity
@Table(name = "householdmember")
public class HouseholdMember extends DateRangedEntity{
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="application_id", nullable=false)
    private HousingBenefitApplication application;  
	@Basic
    @Column(name = "personNumber", nullable = false)	
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@Size(min = 11, max = 11,groups=InputChecks.class, payload={Severity.Error.class})
	@ValidPersonNumber(groups=InputChecks.class, payload={Severity.Error.class})
	String personNumber;
	
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	public HousingBenefitApplication getApplication() {
		return application;
	}
	public void setApplication(HousingBenefitApplication application) {
		this.application = application;
	}
}
