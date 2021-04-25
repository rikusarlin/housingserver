package fi.rikusarlin.housingserver.data;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import fi.rikusarlin.housingserver.model.Gender;
import fi.rikusarlin.housingserver.validation.InputChecks;
import fi.rikusarlin.housingserver.validation.Severity;
import fi.rikusarlin.housingserver.validation.ValidEmailAddress;
import fi.rikusarlin.housingserver.validation.ValidPersonNumber;

@Entity
@Table(name = "person")
public class PersonEntity extends EntityClass {
	@Basic
    @Column(name = "personNumber", nullable = false)	
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@Size(min = 11, max = 11,groups=InputChecks.class, payload={Severity.Error.class})
	@ValidPersonNumber(groups=InputChecks.class, payload={Severity.Error.class})
	String personNumber;
	
	@Basic
    @Column(name = "firstName", nullable = false)	
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@Size(max = 80,groups=InputChecks.class, payload={Severity.Error.class})
	String firstName;
	
	@Basic
    @Column(name = "lastName", nullable = false)	
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@Size(max = 80,groups=InputChecks.class, payload={Severity.Error.class})
	String lastName;
	
	@Basic
    @Column(name = "birthDate", nullable = false)	
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate birthDate;

	@Basic
    @Column(name = "gender")	
	Gender gender;

	@Basic
    @Column(name = "email", nullable = true)	
	@Size(max = 80,groups=InputChecks.class, payload={Severity.Error.class})
	@ValidEmailAddress(groups=InputChecks.class, payload={Severity.Error.class})
	String email;
	
	public PersonEntity() {
	}

	public String getPersonNumber() {
		return personNumber;
	}

	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
