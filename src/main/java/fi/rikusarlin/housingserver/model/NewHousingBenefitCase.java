package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fi.rikusarlin.housingserver.model.CaseState;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.model.Person;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import io.swagger.annotations.*;

@ApiModel(description="Housing benefit case")@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class NewHousingBenefitCase   {
  
  private CaseState caseState;
  private Person customer;
  private HousingBenefitApplication application;

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("caseState")
  public CaseState getCaseState() {
    return caseState;
  }
  public void setCaseState(CaseState caseState) {
    this.caseState = caseState;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("customer")
  public Person getCustomer() {
    return customer;
  }
  public void setCustomer(Person customer) {
    this.customer = customer;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("application")
  public HousingBenefitApplication getApplication() {
    return application;
  }
  public void setApplication(HousingBenefitApplication application) {
    this.application = application;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewHousingBenefitCase newHousingBenefitCase = (NewHousingBenefitCase) o;
    return Objects.equals(caseState, newHousingBenefitCase.caseState) &&
        Objects.equals(customer, newHousingBenefitCase.customer) &&
        Objects.equals(application, newHousingBenefitCase.application);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseState, customer, application);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewHousingBenefitCase {\n");
    
    sb.append("    caseState: ").append(toIndentedString(caseState)).append("\n");
    sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
    sb.append("    application: ").append(toIndentedString(application)).append("\n");
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

