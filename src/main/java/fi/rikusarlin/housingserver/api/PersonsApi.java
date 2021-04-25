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
import fi.rikusarlin.housingserver.model.Person;
import io.swagger.annotations.ApiParam;

@Path("/persons")


@io.swagger.annotations.Api(description = "the persons API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaResteasyServerCodegen", date = "2021-04-24T22:40:01.295170+03:00[Europe/Helsinki]")
public class PersonsApi  {

    @Inject PersonsApiService service;

    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "add person", notes = "Adds a person to the system", response = Person.class, tags={ "Persons", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "person created", response = Person.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "person creation not ok, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "an existing person already exists", response = Void.class) })
    public Response addPerson(@ApiParam(value = "Person to add" ) @Valid Person person,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.addPerson(person,securityContext);
    }
    @GET
    @Path("/{id}/check")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "cross-check person info", notes = "Cross validate by person id ", response = Person.class, tags={ "Persons", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "person validation ok", response = Person.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "person validation not ok, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "person not found", response = Void.class) })
    public Response checkPersonById( @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.checkPersonById(id,securityContext);
    }
    @DELETE
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "delete person", notes = "Deletes person", response = Void.class, tags={ "Persons", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "person deleted successfully", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "delete not successful, error message returned", response = ErrorMessage.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "person not found", response = Void.class) })
    public Response deletePerson( @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.deletePerson(id,securityContext);
    }
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch person", notes = "Fetch person by person id ", response = Person.class, tags={ "Persons", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "person found", response = Person.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "bad input parameter", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "person not found", response = Void.class) })
    public Response fetchPersonById( @PathParam("id") Integer id,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchPersonById(id,securityContext);
    }
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "fetch persons", notes = "Fetch all persons ", response = Person.class, responseContainer = "List", tags={ "Persons", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "search results matching criteria", response = Person.class, responseContainer = "List") })
    public Response fetchPersons(@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.fetchPersons(securityContext);
    }
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "update person", notes = "Updates person info", response = Person.class, tags={ "Persons", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "person updated", response = Person.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "update not successful, error message returned", response = ErrorMessage.class) })
    public Response updatePerson( @PathParam("id") Integer id,@ApiParam(value = "Person to update" ) @Valid Person person,@Context SecurityContext securityContext)
    throws NotFoundException {
        return service.updatePerson(id,person,securityContext);
    }
}
