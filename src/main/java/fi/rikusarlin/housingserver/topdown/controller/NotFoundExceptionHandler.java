package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import fi.rikusarlin.housingserver.exception.NotFoundException;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {
	@Override
	public Response toResponse(NotFoundException e) 
	{
    	Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", Status.NOT_FOUND.name());
        body.put("error", "NOT_FOUND");
        body.put("message", e.getMessage());

		return Response.status(Status.NOT_FOUND).entity(body).build();  
	}
}
