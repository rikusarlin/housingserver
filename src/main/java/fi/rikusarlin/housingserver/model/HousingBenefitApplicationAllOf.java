package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fi.rikusarlin.housingserver.model.Person;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import javax.validation.constraints.*;
import io.swagger.annotations.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class HousingBenefitApplicationAllOf   {
  
  private Person applicant;
  private OffsetDateTime received;

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("applicant")
  public Person getApplicant() {
    return applicant;
  }
  public void setApplicant(Person applicant) {
    this.applicant = applicant;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("received")
  public OffsetDateTime getReceived() {
    return received;
  }
  public void setReceived(OffsetDateTime received) {
    this.received = received;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HousingBenefitApplicationAllOf housingBenefitApplicationAllOf = (HousingBenefitApplicationAllOf) o;
    return Objects.equals(applicant, housingBenefitApplicationAllOf.applicant) &&
        Objects.equals(received, housingBenefitApplicationAllOf.received);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicant, received);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HousingBenefitApplicationAllOf {\n");
    
    sb.append("    applicant: ").append(toIndentedString(applicant)).append("\n");
    sb.append("    received: ").append(toIndentedString(received)).append("\n");
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

