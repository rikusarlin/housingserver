package fi.rikusarlin.housingserver.data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "householdmember")
@NoArgsConstructor
@Getter
@Setter
public class HouseholdMemberEntity extends DateRangedEntity{
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="case_id", nullable=false)
    HousingBenefitCaseEntity housingBenefitCase;
	
	@Valid
	@OneToOne
    @JoinColumn(name="customer_id", nullable=false)
    private PersonEntity person;
}
