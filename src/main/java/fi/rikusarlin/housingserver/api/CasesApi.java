package fi.rikusarlin.housingserver.api;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
import fi.rikusarlin.housingserver.model.HousingBenefitCase;
import fi.rikusarlin.housingserver.model.NewHousingBenefitCase;
import io.swagger.annotations.ApiParam;

@Path("/cases")


@io.swagger.annotations.Api(description = "the cases API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class CasesApi  {

    @Inject CasesApiService service;

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "add housing benefit case based on application", notes = "Adds housing benefit case", response = HousingBenefitCase.class, tags={ "Cases", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "housing benefit case and application created", response = HousingBenefitCase.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "housing benefit case and application creation not ok, error message returned", response = ErrorMessage.class) })
    public Response addHousingBenefitCase(@ApiParam(value = "housing benefit case and application to add" ) @Valid NewHousingBenefitCase newHousingBenefitCase,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.addHousingBenefitCase(newHousingBenefitCase,securityContext);
    }
    @GET
    @Path("/{caseId}/check")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "cross-check housing benefit case", notes = "Cross validate housing benefit case ", response = HousingBenefitCase.class, tags={ "Cases", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "housing benefit case validation ok", response = HousingBenefitCase.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "housing benefit case validation not ok, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "housing benefit case not found", response = Void.class) })
    public Response checkHousingBenefitCase( @PathParam("caseId") Integer caseId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.checkHousingBenefitCase(caseId,securityContext);
    }
    @DELETE
    @Path("/{caseId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "delete housing benefit case", notes = "Deletes housing benefit case", response = Void.class, tags={ "Cases", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "housing benefit case deleted successfully", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "delete not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "housing benefit case not found", response = Void.class) })
    public Response deleteHousingBenefitCase( @PathParam("caseId") Integer caseId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.deleteHousingBenefitCase(caseId,securityContext);
    }
    @GET
    @Path("/{caseId}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch housing benefit case", notes = "Fetch housing benefit case by id ", response = HousingBenefitCase.class, tags={ "Cases", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "housing benefit application found", response = HousingBenefitCase.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "fetch not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "housing benefit case not found", response = Void.class) })
    public Response fetchHousingBenefitCaseById( @PathParam("caseId") Integer caseId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchHousingBenefitCaseById(caseId,securityContext);
    }
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch housing benefit cases", notes = "Fetch all housing benefit cases ", response = HousingBenefitCase.class, responseContainer = "List", tags={ "Cases", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results", response = HousingBenefitCase.class, responseContainer = "List") })
    public Response fetchHousingBenefitCases(@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchHousingBenefitCases(securityContext);
    }
    @GET
    @Path("/person/{personNumber}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch housing benefit cases for a given person", notes = "Fetch all housing benefit cases of a given person A housing benefit case is returned if the given person is a household member in the application ", response = HousingBenefitCase.class, responseContainer = "List", tags={ "Cases", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results", response = HousingBenefitCase.class, responseContainer = "List") })
    public Response fetchHousingBenefitCasesByPersonNumber( @Pattern(regexp="^\\d{6}[A+-]\\d{3}[0-9A-FHJ-NPR-Y]$") @Size(min=11,max=11) @PathParam("personNumber") String personNumber,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchHousingBenefitCasesByPersonNumber(personNumber,securityContext);
    }
    @PUT
    @Path("/{caseId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "update housing benefit case", notes = "Updates housing benefit case", response = HousingBenefitCase.class, tags={ "Cases", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "updated housing benefit case", response = HousingBenefitCase.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "update not successful, error message returned", response = ErrorMessage.class) })
    public Response updateHousingBenefitCase( @PathParam("caseId") Integer caseId,@ApiParam(value = "Housing benefit case to update" ) @Valid HousingBenefitCase housingBenefitCase,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.updateHousingBenefitCase(caseId,housingBenefitCase,securityContext);
    }
}
