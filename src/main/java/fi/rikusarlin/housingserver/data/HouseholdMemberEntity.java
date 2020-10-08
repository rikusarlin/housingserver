package fi.rikusarlin.housingserver.data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "householdmember")
public class HouseholdMemberEntity extends DateRangedEntity{
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="application_id", nullable=false)
    private HousingBenefitApplicationEntity application;
	@Valid
	@OneToOne
    @JoinColumn(name="customer_id", nullable=false)
    private PersonEntity person;
	
	public HousingBenefitApplicationEntity getApplication() {
		return application;
	}
	public void setApplication(HousingBenefitApplicationEntity application) {
		this.application = application;
	}
	public PersonEntity getPerson() {
		return person;
	}
	public void setPerson(PersonEntity person) {
		this.person = person;
	}
}
