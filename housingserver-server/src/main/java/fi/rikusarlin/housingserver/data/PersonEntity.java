package fi.rikusarlin.housingserver.data;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import fi.rikusarlin.housingserver.model.Gender;
import fi.rikusarlin.housingserver.validation.InputChecks;
import fi.rikusarlin.housingserver.validation.Severity;
import fi.rikusarlin.housingserver.validation.ValidEmailAddress;
import fi.rikusarlin.housingserver.validation.ValidPersonNumber;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@EqualsAndHashCode(callSuper=false)
@Table(value="person")
public class PersonEntity extends EntityClass {
	@Column(value = "personNumber")	
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@Size(min = 11, max = 11,groups=InputChecks.class, payload={Severity.Error.class})
	@ValidPersonNumber(groups=InputChecks.class, payload={Severity.Error.class})
	String personNumber;
	
    @Column(value = "firstName")
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@Size(max = 80,groups=InputChecks.class, payload={Severity.Error.class})
	String firstName;
	
    @Column(value = "lastName")
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@Size(max = 80,groups=InputChecks.class, payload={Severity.Error.class})
	String lastName;
	
    @Column(value = "birthDate")
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@DateTimeFormat(pattern="dd.MM.yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate birthDate;

    @Column(value = "gender")	
	Gender gender;

    @Column(value = "email")	
	@Size(max = 80,groups=InputChecks.class, payload={Severity.Error.class})
	@ValidEmailAddress(groups=InputChecks.class, payload={Severity.Error.class})
	String email;
    
    @Builder
    public PersonEntity(
			Integer id,
			String createdByUser,
			String modifiedByUser,
			LocalDateTime creationTime,
			LocalDateTime modificationTime,
			String personNumber,
			String firstName,
			String lastName,
			LocalDate birthDate,
			Gender gender,
			String email) {
		super(id, createdByUser, modifiedByUser, creationTime, modificationTime);
		this.personNumber = personNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.gender = gender;
		this.email = email;
	}
    
    public PersonEntity() {
		super();
	}

	
}
