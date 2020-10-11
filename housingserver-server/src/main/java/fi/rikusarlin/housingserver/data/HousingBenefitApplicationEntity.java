package fi.rikusarlin.housingserver.data;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

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
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate received;

	@Valid
	@OneToOne
    @JoinColumn(name="applicant_id", nullable=false)
    private PersonEntity applicant;

}
