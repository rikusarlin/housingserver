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
 * GET /householdmembers
 * GET /householdmember/{id}
 * PUT /householdmember
   {
     "personNumber": "020103A678R",
     "startDate": "2020-08-01",
     "endDate": "2021-12-31"
   }
 * POST /householdmember/{id} - payload with or without ID, either updates or inserts
 
 In addition to the actual Rest service, there are experiments with JSON schema based validation in class JsonSchemaValidition application. JSON schemas and example JSON input files can be found in src/main/resources. JsonSchemaValidation app also shows how to combine JSON schema validation to Spring validation. This could also be applied to Rest service, see [this blog post](https://www.mscharhag.com/spring/json-schema-validation-handlermethodargumentresolver). All in all, though, only quite basic validation can be done through JSON schema validation. 
 