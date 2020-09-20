package fi.rikusarlin.housingserver.data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fi.rikusarlin.housingserver.validation.HouseholdChecks;
import fi.rikusarlin.housingserver.validation.Severity;
import fi.rikusarlin.housingserver.validation.ValidPersonNumber;

@Entity
@Table(name = "householdmember", catalog = "")
public class HouseholdMember extends DateRangedEntity{
	@Basic
    @Column(name = "personNumber", nullable = false)	
	@NotNull(groups=HouseholdChecks.class, payload={Severity.Error.class})
	@Size(min = 11, max = 11,groups=HouseholdChecks.class, payload={Severity.Error.class})
	@ValidPersonNumber(groups=HouseholdChecks.class, payload={Severity.Error.class})
	String personNumber;
	
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
}
