# housingserver
Spring Boot Rest service, with validations done using Spring Validation and ORM stuff done with Spring Data repository backed by H2 database.

## Domain model
The application domain is an imaginary "housing benefit application" containing various pieces of information. Date periods and of money of many are prominent features in this domain model. The business objects are as follows
* Housing benefit application
    * date range
    * id
    * List of household members 
        * id
        * person number
        * date range
    * List of incomes
        * id
        * income type
        * description, if type of income is "other income"
        * amount
        * date range
    * List of housing expenses
        * id
        * expense type
        * description, if type of expense is "other expense"
        * amount
        * date range
        
## Usage
The service is accessed via Rest api, with the following methods
 
### Household member
* GET /api/v1/housing/{caseId}/householdmembers - fetch all household members of a case
* GET /api/v1/housing/{caseId}/householdmember/{id} - fetch one household member of a case
* PUT /api/v1/housing/{caseId}/householdmember - add household member to a case
* POST /api/v1/housing/{caseId}/householdmember/{id} - update household member in a case
* GET /api/v1/housing/{caseId}/householdmember/{id}/check - cross validate household member

### Expense
* GET /api/v1/housing/{caseId}/expenses - fetch all expenses of a case
* GET /api/v1/housing/{caseId}/expense/{id} - fetch one expense
* PUT /api/v1/housing/{caseId}/expense - add expense to a case
* POST /api/v1/housing/{caseId}/expense/{id} - update expense in a case
* GET /api/v1/housing/{caseId}/expense/{id}/check - cross validate expense

### Income
* GET /api/v1/housing/{caseId}/incomes - fetch all incomes of a case
* GET /api/v1/housing/{caseId}/income/{id} - fetch one income
* PUT /api/v1/housing/{caseId}/income - add income to a case
* POST /api/v1/housing/{caseId}/income/{id} - update income in a case
* GET /api/v1/housing/{caseId}/income/{id}/check - cross validate income

### Housing benefit application
* GET /api/v1/housing - fetch all housing benefit applications
* GET /api/v1/housing/{caseId} - fetch one housing benefit application
* PUT /api/v1/housing - add housing benefit application
* POST /api/v1/housing - update housing benefit application in a case
* GET /api/v1/housing/{caseId}/check - cross validate housing benefit application
 
 In addition to the actual Rest service, there are experiments with JSON schema based validation in class JsonSchemaValidition application (in src/test/main). JSON schema files can be found in src/main/resources and some example JSON input in src/test/resources. JsonSchemaValidation app also shows how to combine JSON schema validation to Spring validation (in a rather unsatisfactory way, though). This could also be applied to Rest service, see [this blog post](https://www.mscharhag.com/spring/json-schema-validation-handlermethodargumentresolver). All in all, only quite basic validation can be done through JSON schema validation - no cross-validation can be done, and no validation containing complex logic (such as validating control characters). 
 