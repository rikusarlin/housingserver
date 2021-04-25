package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fi.rikusarlin.housingserver.model.ExpenseType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import io.swagger.annotations.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class ExpenseAllOf   {
  
  private ExpenseType expenseType;
  private Double amount;
  private String otherExpenseDescription;

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("expenseType")
  public ExpenseType getExpenseType() {
    return expenseType;
  }
  public void setExpenseType(ExpenseType expenseType) {
    this.expenseType = expenseType;
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
   * A textual description for expense if expense type is OTHER
   **/
  
  @ApiModelProperty(value = "A textual description for expense if expense type is OTHER")
  @JsonProperty("otherExpenseDescription")
  public String getOtherExpenseDescription() {
    return otherExpenseDescription;
  }
  public void setOtherExpenseDescription(String otherExpenseDescription) {
    this.otherExpenseDescription = otherExpenseDescription;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpenseAllOf expenseAllOf = (ExpenseAllOf) o;
    return Objects.equals(expenseType, expenseAllOf.expenseType) &&
        Objects.equals(amount, expenseAllOf.amount) &&
        Objects.equals(otherExpenseDescription, expenseAllOf.otherExpenseDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expenseType, amount, otherExpenseDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExpenseAllOf {\n");
    
    sb.append("    expenseType: ").append(toIndentedString(expenseType)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    otherExpenseDescription: ").append(toIndentedString(otherExpenseDescription)).append("\n");
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

