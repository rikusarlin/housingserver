package fi.rikusarlin.housingserver.data;

import java.time.OffsetDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="application")
@NoArgsConstructor
@Getter
@Setter
public class HousingBenefitApplicationEntity extends DateRangedEntity {
	@JsonIgnore
	@OneToOne
    @JoinColumn(name="case_id", nullable=false, referencedColumnName = "id")
    HousingBenefitCaseEntity housingBenefitCase;

	@Basic
    @Column(name = "received", nullable = true)
	OffsetDateTime received;

	@Valid
	@OneToOne
    @JoinColumn(name="applicant_id", nullable=false)
    PersonEntity applicant;

}
