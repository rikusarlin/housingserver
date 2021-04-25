package fi.rikusarlin.housingserver.data;

import java.time.OffsetDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="application")
public class HousingBenefitApplicationEntity extends DateRangedEntity {
	@JsonIgnore
	@OneToOne
    @JoinColumn(name="case_id", nullable=false, referencedColumnName = "id")
    HousingBenefitCaseEntity housingBenefitCase;

	@Basic
    @Column(name = "received", nullable = true)
	OffsetDateTime received;

	@OneToOne
    @JoinColumn(name="applicant_id", nullable=false)
    PersonEntity applicant;

	public HousingBenefitApplicationEntity() {
	}

	public HousingBenefitCaseEntity getHousingBenefitCase() {
		return housingBenefitCase;
	}

	public void setHousingBenefitCase(HousingBenefitCaseEntity housingBenefitCase) {
		this.housingBenefitCase = housingBenefitCase;
	}

	public OffsetDateTime getReceived() {
		return received;
	}

	public void setReceived(OffsetDateTime received) {
		this.received = received;
	}

	public PersonEntity getApplicant() {
		return applicant;
	}

	public void setApplicant(PersonEntity applicant) {
		this.applicant = applicant;
	}
	
}
