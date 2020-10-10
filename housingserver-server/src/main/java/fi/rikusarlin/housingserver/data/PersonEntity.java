package fi.rikusarlin.housingserver.data;

import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import fi.rikusarlin.housingserver.model.Person;
import fi.rikusarlin.housingserver.validation.InputChecks;
import fi.rikusarlin.housingserver.validation.Severity;
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
    @Column(name = "birthDate", nullable = true)	
	@NotNull(groups=InputChecks.class, payload={Severity.Error.class})
	@DateTimeFormat(pattern="dd.MM.yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	LocalDate birthDate;
	
	public PersonEntity() {
	}

	public PersonEntity(Person person) {
		this.id = person.getId();
		this.birthDate = person.getBirthDate();
		this.personNumber = person.getPersonNumber();
		this.lastName = person.getLastName();
		this.firstName = person.getFirstName();
	}

	public Person toPerson() {
		Person person = new Person();
		person.setId(this.id);
		person.setBirthDate(this.birthDate);
		person.setFirstName(this.firstName);
		person.setLastName(this.lastName);
		person.setPersonNumber(this.personNumber);
		return person;
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
}
