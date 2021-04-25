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
import fi.rikusarlin.housingserver.model.Expense;
import io.swagger.annotations.ApiParam;

@Path("/expenses/{caseId}")


@io.swagger.annotations.Api(description = "the expenses API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class ExpensesApi  {

    @Inject ExpensesApiService service;

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "add expense", notes = "Adds an expense to a given case", response = Expense.class, tags={ "Expenses", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "expense created", response = Expense.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "expense creation not ok, error message returned", response = ErrorMessage.class) })
    public Response addExpense( @PathParam("caseId") Integer caseId,@ApiParam(value = "Expense to add" ) @Valid Expense expense,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.addExpense(caseId,expense,securityContext);
    }
    @GET
    @Path("/{id}/check")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "cross-check expense info in a housing benefit application", notes = "Cross validate by expense id ", response = Expense.class, tags={ "Expenses", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "expense validation ok", response = Expense.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "expense validation not ok, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "expense not found", response = Void.class) })
    public Response checkExpenseById( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.checkExpenseById(caseId,id,securityContext);
    }
    @DELETE
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "delete expense in a given case", notes = "Deletes expense in a given case", response = Void.class, tags={ "Expenses", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "expense deleted successfully", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "delete not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "expense not found", response = Void.class) })
    public Response deleteExpense( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.deleteExpense(caseId,id,securityContext);
    }
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch expense", notes = "Fetch expense by id in a given case ", response = Expense.class, tags={ "Expenses", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "expense found", response = Expense.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "fetch not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "expense not found", response = Void.class) })
    public Response fetchExpenseById( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchExpenseById(caseId,id,securityContext);
    }
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch expenses of a given case", notes = "Fetch all expenses of a given case ", response = Expense.class, responseContainer = "List", tags={ "Expenses", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results", response = Expense.class, responseContainer = "List") })
    public Response fetchExpenses( @PathParam("caseId") Integer caseId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchExpenses(caseId,securityContext);
    }
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "update expense in a given case", notes = "Updates expense info", response = Expense.class, tags={ "Expenses", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "updated expense", response = Expense.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "update not successful, error message returned", response = ErrorMessage.class) })
    public Response updateExpense( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@ApiParam(value = "Expense to update" ) @Valid Expense expense,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.updateExpense(caseId,id,expense,securityContext);
    }
}
