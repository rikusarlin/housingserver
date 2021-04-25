package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fi.rikusarlin.housingserver.model.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.*;
import io.swagger.annotations.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class PersonAllOf   {
  
  private String personNumber;
  private String firstName;
  private String lastName;
  private LocalDate birthDate;
  private Gender gender;
  private String email;

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
    PersonAllOf personAllOf = (PersonAllOf) o;
    return Objects.equals(personNumber, personAllOf.personNumber) &&
        Objects.equals(firstName, personAllOf.firstName) &&
        Objects.equals(lastName, personAllOf.lastName) &&
        Objects.equals(birthDate, personAllOf.birthDate) &&
        Objects.equals(gender, personAllOf.gender) &&
        Objects.equals(email, personAllOf.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personNumber, firstName, lastName, birthDate, gender, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersonAllOf {\n");
    
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

