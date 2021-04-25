package fi.rikusarlin.housingserver.model;

import java.util.Objects;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import fi.rikusarlin.housingserver.model.CaseState;
import fi.rikusarlin.housingserver.model.Expense;
import fi.rikusarlin.housingserver.model.HouseholdMember;
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import fi.rikusarlin.housingserver.model.Income;
import fi.rikusarlin.housingserver.model.Person;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import io.swagger.annotations.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class HousingBenefitCaseAllOf   {
  
  private CaseState caseState;
  private Person customer;
  private HousingBenefitApplication application;
  private List<HouseholdMember> householdMembers = new ArrayList<>();
  private List<Expense> housingExpenses = new ArrayList<>();
  private List<Income> incomes = new ArrayList<>();

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
    HousingBenefitCaseAllOf housingBenefitCaseAllOf = (HousingBenefitCaseAllOf) o;
    return Objects.equals(caseState, housingBenefitCaseAllOf.caseState) &&
        Objects.equals(customer, housingBenefitCaseAllOf.customer) &&
        Objects.equals(application, housingBenefitCaseAllOf.application) &&
        Objects.equals(householdMembers, housingBenefitCaseAllOf.householdMembers) &&
        Objects.equals(housingExpenses, housingBenefitCaseAllOf.housingExpenses) &&
        Objects.equals(incomes, housingBenefitCaseAllOf.incomes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(caseState, customer, application, householdMembers, housingExpenses, incomes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HousingBenefitCaseAllOf {\n");
    
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

