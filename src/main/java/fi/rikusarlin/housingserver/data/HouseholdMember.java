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
public class HouseholdMember extends DateRangedEntity{
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="application_id", nullable=false)
    private HousingBenefitApplication application;
	@Valid
	@OneToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Person person;
	
	public HousingBenefitApplication getApplication() {
		return application;
	}
	public void setApplication(HousingBenefitApplication application) {
		this.application = application;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
}
