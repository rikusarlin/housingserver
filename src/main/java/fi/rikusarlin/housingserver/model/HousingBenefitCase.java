package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fi.rikusarlin.housingserver.model.CaseState;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.model.HousingBenefitCaseAllOf;
import fi.rikusarlin.housingserver.model.IdAndAuditing;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.model.Person;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import io.swagger.annotations.*;

@ApiModel(description="Housing benefit case")@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class HousingBenefitCase   {
  
  private Integer id;
  private OffsetDateTime creationTime;
  private String createdByUser;
  private OffsetDateTime modificationTime;
  private String modifiedByUser;
  private CaseState caseState;
  private Person customer;
  private HousingBenefitApplication application;
  private List<HouseholdMember> householdMembers = new ArrayList<>();
  private List<Expense> housingExpenses = new ArrayList<>();
  private List<Income> incomes = new ArrayList<>();

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

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("householdMembers")
  public List<HouseholdMember> getHouseholdMembers() {
    return householdMembers;
  }
  public void setHouseholdMembers(List<HouseholdMember> householdMembers) {
    this.householdMembers = householdMembers;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("housingExpenses")
  public List<Expense> getHousingExpenses() {
    return housingExpenses;
  }
  public void setHousingExpenses(List<Expense> housingExpenses) {
    this.housingExpenses = housingExpenses;
  }

  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("incomes")
  public List<Income> getIncomes() {
    return incomes;
  }
  public void setIncomes(List<Income> incomes) {
    this.incomes = incomes;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HousingBenefitCase housingBenefitCase = (HousingBenefitCase) o;
    return Objects.equals(id, housingBenefitCase.id) &&
        Objects.equals(creationTime, housingBenefitCase.creationTime) &&
        Objects.equals(createdByUser, housingBenefitCase.createdByUser) &&
        Objects.equals(modificationTime, housingBenefitCase.modificationTime) &&
        Objects.equals(modifiedByUser, housingBenefitCase.modifiedByUser) &&
        Objects.equals(caseState, housingBenefitCase.caseState) &&
        Objects.equals(customer, housingBenefitCase.customer) &&
        Objects.equals(application, housingBenefitCase.application) &&
        Objects.equals(householdMembers, housingBenefitCase.householdMembers) &&
        Objects.equals(housingExpenses, housingBenefitCase.housingExpenses) &&
        Objects.equals(incomes, housingBenefitCase.incomes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, creationTime, createdByUser, modificationTime, modifiedByUser, caseState, customer, application, householdMembers, housingExpenses, incomes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HousingBenefitCase {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    createdByUser: ").append(toIndentedString(createdByUser)).append("\n");
    sb.append("    modificationTime: ").append(toIndentedString(modificationTime)).append("\n");
    sb.append("    modifiedByUser: ").append(toIndentedString(modifiedByUser)).append("\n");
    sb.append("    caseState: ").append(toIndentedString(caseState)).append("\n");
    sb.append("    customer: ").append(toIndentedString(customer)).append("\n");
    sb.append("    application: ").append(toIndentedString(application)).append("\n");
    sb.append("    householdMembers: ").append(toIndentedString(householdMembers)).append("\n");
    sb.append("    housingExpenses: ").append(toIndentedString(housingExpenses)).append("\n");
    sb.append("    incomes: ").append(toIndentedString(incomes)).append("\n");
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

