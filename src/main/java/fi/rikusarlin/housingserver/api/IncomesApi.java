package fi.rikusarlin.housingserver.api;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import fi.rikusarlin.housingserver.model.ErrorMessage;
import fi.rikusarlin.housingserver.model.Income;
import io.swagger.annotations.ApiParam;

@Path("/incomes/{caseId}")


@io.swagger.annotations.Api(description = "the incomes API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class IncomesApi  {

    @Inject IncomesApiService service;

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "add income", notes = "Adds an income to a given case", response = Income.class, tags={ "Incomes", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "income created", response = Income.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "income creation not ok, error message returned", response = ErrorMessage.class) })
    public Response addIncome( @PathParam("caseId") Integer caseId,@ApiParam(value = "Income to add" ) @Valid Income income,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.addIncome(caseId,income,securityContext);
    }
    @GET
    @Path("/{id}/check")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "cross-check income info in a housing benefit application", notes = "Cross validate by income id ", response = Income.class, tags={ "Incomes", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "income validation ok", response = Income.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "income validation not ok, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "income not found", response = Void.class) })
    public Response checkIncomeById( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.checkIncomeById(caseId,id,securityContext);
    }
    @DELETE
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "delete income in a given case", notes = "Deletes income in a given case", response = Void.class, tags={ "Incomes", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "income deleted successfully", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "delete not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "income not found", response = Void.class) })
    public Response deleteIncome( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.deleteIncome(caseId,id,securityContext);
    }
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch income", notes = "Fetch income by id in a given case ", response = Income.class, tags={ "Incomes", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "income found", response = Income.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "fetch not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "income not found", response = Void.class) })
    public Response fetchIncomeById( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchIncomeById(caseId,id,securityContext);
    }
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch incomes of a given case", notes = "Fetch all incomes of a given case ", response = Income.class, responseContainer = "List", tags={ "Incomes", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results", response = Income.class, responseContainer = "List") })
    public Response fetchIncomes( @PathParam("caseId") Integer caseId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchIncomes(caseId,securityContext);
    }
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "update income in a given case", notes = "Updates income info", response = Income.class, tags={ "Incomes", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "updated income", response = Income.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "update not successful, error message returned", response = ErrorMessage.class) })
    public Response updateIncome( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@ApiParam(value = "Income to update" ) @Valid Income income,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.updateIncome(caseId,id,income,securityContext);
    }
}
