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
import fi.rikusarlin.housingserver.model.HousingBenefitApplication;
import io.swagger.annotations.ApiParam;

@Path("/applications/{caseId}")


@io.swagger.annotations.Api(description = "the applications API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class ApplicationsApi  {

    @Inject ApplicationsApiService service;

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "add application to case", notes = "Adds an application to a given case", response = HousingBenefitApplication.class, tags={ "Applications", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "application created", response = HousingBenefitApplication.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "application creation not ok, error message returned", response = ErrorMessage.class) })
    public Response addApplication( @PathParam("caseId") Integer caseId,@ApiParam(value = "Application to add" ) @Valid HousingBenefitApplication housingBenefitApplication,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.addApplication(caseId,housingBenefitApplication,securityContext);
    }
    @GET
    @Path("/check")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "cross-check application info in a housing benefit application", notes = "Cross validate by income id ", response = HousingBenefitApplication.class, tags={ "Applications", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "application validation ok", response = HousingBenefitApplication.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "application validation not ok, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "application not found", response = Void.class) })
    public Response checkApplication( @PathParam("caseId") Integer caseId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.checkApplication(caseId,securityContext);
    }
    @DELETE
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "delete application of a given case", notes = "Deletes application of a given case", response = Void.class, tags={ "Applications", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "application deleted successfully", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "delete not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "application not found", response = Void.class) })
    public Response deleteApplication( @PathParam("caseId") Integer caseId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.deleteApplication(caseId,securityContext);
    }
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch application of a given case", notes = "Fetch all incomes of a given case ", response = HousingBenefitApplication.class, tags={ "Applications", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results", response = HousingBenefitApplication.class) })
    public Response fetchApplication( @PathParam("caseId") Integer caseId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchApplication(caseId,securityContext);
    }
    @PUT
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "update application in a given case", notes = "Updates application info", response = HousingBenefitApplication.class, tags={ "Applications", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "updated application", response = HousingBenefitApplication.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "update not successful, error message returned", response = ErrorMessage.class) })
    public Response updateApplication( @PathParam("caseId") Integer caseId,@ApiParam(value = "Application to update" ) @Valid HousingBenefitApplication housingBenefitApplication,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.updateApplication(caseId,housingBenefitApplication,securityContext);
    }
}
