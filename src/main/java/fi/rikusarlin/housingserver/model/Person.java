package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fi.rikusarlin.housingserver.model.Gender;
import fi.rikusarlin.housingserver.model.IdAndAuditing;
import fi.rikusarlin.housingserver.model.PersonAllOf;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import javax.validation.constraints.*;
import io.swagger.annotations.*;

@ApiModel(description="A natural person residing in Finland")@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class Person   {
  
  private Integer id;
  private OffsetDateTime creationTime;
  private String createdByUser;
  private OffsetDateTime modificationTime;
  private String modifiedByUser;
  private String personNumber;
  private String firstName;
  private String lastName;
  private LocalDate birthDate;
  private Gender gender;
  private String email;

  /**
   * Unique identifier of a resource
   **/
  
  @ApiModelProperty(value = "Unique identifier of a resource")
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Timestamp of last creation
   **/
  
  @ApiModelProperty(value = "Timestamp of last creation")
  @JsonProperty("creationTime")
  public OffsetDateTime getCreationTime() {
    return creationTime;
  }
  public void setCreationTime(OffsetDateTime creationTime) {
    this.creationTime = creationTime;
  }

  /**
   * User that created this entity
   **/
  
  @ApiModelProperty(value = "User that created this entity")
  @JsonProperty("createdByUser")
 @Size(max=80)  public String getCreatedByUser() {
    return createdByUser;
  }
  public void setCreatedByUser(String createdByUser) {
    this.createdByUser = createdByUser;
  }

  /**
   * Timestamp of last modification
   **/
  
  @ApiModelProperty(value = "Timestamp of last modification")
  @JsonProperty("modificationTime")
  public OffsetDateTime getModificationTime() {
    return modificationTime;
  }
  public void setModificationTime(OffsetDateTime modificationTime) {
    this.modificationTime = modificationTime;
  }

  /**
   * User that last modified this entity
   **/
  
  @ApiModelProperty(value = "User that last modified this entity")
  @JsonProperty("modifiedByUser")
 @Size(max=80)  public String getModifiedByUser() {
    return modifiedByUser;
  }
  public void setModifiedByUser(String modifiedByUser) {
    this.modifiedByUser = modifiedByUser;
  }

  /**
   * Finnish person number
   **/
  
  @ApiModelProperty(value = "Finnish person number")
  @JsonProperty("personNumber")
 @Pattern(regexp="^\\d{6}[A+-]\\d{3}[0-9A-FHJ-NPR-Y]$") @Size(min=11,max=11)  public String getPersonNumber() {
    return personNumber;
  }
  public void setPersonNumber(String personNumber) {
    this.personNumber = personNumber;
  }

  /**
   * First name of a person
   **/
  
  @ApiModelProperty(value = "First name of a person")
  @JsonProperty("firstName")
 @Size(max=80)  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Last name of a person
   **/
  
  @ApiModelProperty(value = "Last name of a person")
  @JsonProperty("lastName")
 @Size(max=80)  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   **/
  
  @ApiModelProperty(example = "Sun Oct 10 02:00:00 EET 1971", value = "")
  @JsonProperty("birthDate")
  public LocalDate getBirthDate() {
    return birthDate;
  }
  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("gender")
  public Gender getGender() {
    return gender;
  }
  public void setGender(Gender gender) {
    this.gender = gender;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("email")
 @Size(max=80)  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Person person = (Person) o;
    return Objects.equals(id, person.id) &&
        Objects.equals(creationTime, person.creationTime) &&
        Objects.equals(createdByUser, person.createdByUser) &&
        Objects.equals(modificationTime, person.modificationTime) &&
        Objects.equals(modifiedByUser, person.modifiedByUser) &&
        Objects.equals(personNumber, person.personNumber) &&
        Objects.equals(firstName, person.firstName) &&
        Objects.equals(lastName, person.lastName) &&
        Objects.equals(birthDate, person.birthDate) &&
        Objects.equals(gender, person.gender) &&
        Objects.equals(email, person.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, creationTime, createdByUser, modificationTime, modifiedByUser, personNumber, firstName, lastName, birthDate, gender, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Person {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    createdByUser: ").append(toIndentedString(createdByUser)).append("\n");
    sb.append("    modificationTime: ").append(toIndentedString(modificationTime)).append("\n");
    sb.append("    modifiedByUser: ").append(toIndentedString(modifiedByUser)).append("\n");
    sb.append("    personNumber: ").append(toIndentedString(personNumber)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

