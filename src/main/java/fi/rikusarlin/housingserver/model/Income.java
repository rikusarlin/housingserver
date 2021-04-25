package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fi.rikusarlin.housingserver.model.IdDateRangeAndAuditing;
import fi.rikusarlin.housingserver.model.IncomeAllOf;
import fi.rikusarlin.housingserver.model.IncomeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import javax.validation.constraints.*;
import io.swagger.annotations.*;

@ApiModel(description="Income info")@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class Income   {
  
  private Integer id;
  private OffsetDateTime creationTime;
  private String createdByUser;
  private OffsetDateTime modificationTime;
  private String modifiedByUser;
  private LocalDate startDate;
  private LocalDate endDate;
  private IncomeType incomeType;
  private Double amount;
  private String otherIncomeDescription;

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
   **/
  
  @ApiModelProperty(example = "Thu Oct 08 03:00:00 EEST 2020", value = "")
  @JsonProperty("startDate")
  public LocalDate getStartDate() {
    return startDate;
  }
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   **/
  
  @ApiModelProperty(example = "Thu Oct 08 03:00:00 EEST 2020", value = "")
  @JsonProperty("endDate")
  public LocalDate getEndDate() {
    return endDate;
  }
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

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
    Income income = (Income) o;
    return Objects.equals(id, income.id) &&
        Objects.equals(creationTime, income.creationTime) &&
        Objects.equals(createdByUser, income.createdByUser) &&
        Objects.equals(modificationTime, income.modificationTime) &&
        Objects.equals(modifiedByUser, income.modifiedByUser) &&
        Objects.equals(startDate, income.startDate) &&
        Objects.equals(endDate, income.endDate) &&
        Objects.equals(incomeType, income.incomeType) &&
        Objects.equals(amount, income.amount) &&
        Objects.equals(otherIncomeDescription, income.otherIncomeDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, creationTime, createdByUser, modificationTime, modifiedByUser, startDate, endDate, incomeType, amount, otherIncomeDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Income {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    createdByUser: ").append(toIndentedString(createdByUser)).append("\n");
    sb.append("    modificationTime: ").append(toIndentedString(modificationTime)).append("\n");
    sb.append("    modifiedByUser: ").append(toIndentedString(modifiedByUser)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

