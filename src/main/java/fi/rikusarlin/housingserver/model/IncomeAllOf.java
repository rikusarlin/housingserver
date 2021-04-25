package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fi.rikusarlin.housingserver.model.IncomeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import io.swagger.annotations.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class IncomeAllOf   {
  
  private IncomeType incomeType;
  private Double amount;
  private String otherIncomeDescription;

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("incomeType")
  public IncomeType getIncomeType() {
    return incomeType;
  }
  public void setIncomeType(IncomeType incomeType) {
    this.incomeType = incomeType;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("amount")
  public Double getAmount() {
    return amount;
  }
  public void setAmount(Double amount) {
    this.amount = amount;
  }

  /**
   * A textual description for income if income type is OTHER
   **/
  
  @ApiModelProperty(value = "A textual description for income if income type is OTHER")
  @JsonProperty("otherIncomeDescription")
  public String getOtherIncomeDescription() {
    return otherIncomeDescription;
  }
  public void setOtherIncomeDescription(String otherIncomeDescription) {
    this.otherIncomeDescription = otherIncomeDescription;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncomeAllOf incomeAllOf = (IncomeAllOf) o;
    return Objects.equals(incomeType, incomeAllOf.incomeType) &&
        Objects.equals(amount, incomeAllOf.amount) &&
        Objects.equals(otherIncomeDescription, incomeAllOf.otherIncomeDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incomeType, amount, otherIncomeDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncomeAllOf {\n");
    
    sb.append("    incomeType: ").append(toIndentedString(incomeType)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    otherIncomeDescription: ").append(toIndentedString(otherIncomeDescription)).append("\n");
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

