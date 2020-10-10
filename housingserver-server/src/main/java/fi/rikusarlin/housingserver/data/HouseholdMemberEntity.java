package fi.rikusarlin.housingserver.data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.rikusarlin.housingserver.model.HouseholdMember;

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
	
	public HouseholdMemberEntity() {
	}

	public HouseholdMemberEntity(HouseholdMember hm) {
		this.id = hm.getId();
		this.startDate = hm.getStartDate();
		this.endDate = hm.getEndDate();
		this.setPerson(new PersonEntity(hm.getPerson()));
	}

	public HouseholdMember toHouseholdMember() {
		HouseholdMember hm = new HouseholdMember();
		hm.setId(this.id);
		hm.setStartDate(this.startDate);
		hm.setEndDate(this.endDate);
		hm.setPerson(person.toPerson());
		return hm;
	}

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
