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
import fi.rikusarlin.housingserver.model.HouseholdMember;
import io.swagger.annotations.ApiParam;

@Path("/householdmembers/{caseId}")


@io.swagger.annotations.Api(description = "the householdmembers API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class HouseholdmembersApi  {

    @Inject HouseholdmembersApiService service;

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "add household member", notes = "Adds a household member to a given case", response = HouseholdMember.class, tags={ "Household members", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "household member created", response = HouseholdMember.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "household member creation not ok, error message returned", response = ErrorMessage.class) })
    public Response addHouseholdMember( @PathParam("caseId") Integer caseId,@ApiParam(value = "Household member to add" ) @Valid HouseholdMember householdMember,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.addHouseholdMember(caseId,householdMember,securityContext);
    }
    @GET
    @Path("/{id}/check")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "cross-check household member info in a housing benefit application", notes = "Cross validate by person id ", response = HouseholdMember.class, tags={ "Household members", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "household member validation ok", response = HouseholdMember.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "household member validation not ok, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "household member not found", response = Void.class) })
    public Response checkHouseholdMemberById( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.checkHouseholdMemberById(caseId,id,securityContext);
    }
    @DELETE
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "delete household member in a given case", notes = "Deletes household member in a given case", response = Void.class, tags={ "Household members", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "household member deleted successfully", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "delete not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "household member not found", response = Void.class) })
    public Response deleteHouseholdMember( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.deleteHouseholdMember(caseId,id,securityContext);
    }
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch household member", notes = "Fetch household member by id in a given case ", response = HouseholdMember.class, tags={ "Household members", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "household member found", response = HouseholdMember.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "fetch not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "person not found", response = Void.class) })
    public Response fetchHouseholdMemberById( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchHouseholdMemberById(caseId,id,securityContext);
    }
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch household members of a given case", notes = "Fetch all household members of a given case ", response = HouseholdMember.class, responseContainer = "List", tags={ "Household members", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results", response = HouseholdMember.class, responseContainer = "List") })
    public Response fetchHouseholdMembers( @PathParam("caseId") Integer caseId,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchHouseholdMembers(caseId,securityContext);
    }
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "update household member in a given case", notes = "Updates household member info", response = HouseholdMember.class, tags={ "Household members", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "updated household member", response = HouseholdMember.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "update not successful, error message returned", response = ErrorMessage.class) })
    public Response updateHouseholdMember( @PathParam("caseId") Integer caseId, @PathParam("id") Integer id,@ApiParam(value = "Household member to update" ) @Valid HouseholdMember householdMember,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.updateHouseholdMember(caseId,id,householdMember,securityContext);
    }
}
