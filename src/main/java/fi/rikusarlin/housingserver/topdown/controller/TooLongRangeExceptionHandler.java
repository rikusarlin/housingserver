package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import fi.rikusarlin.housingserver.exception.TooLongRangeException;

@Provider
public class TooLongRangeExceptionHandler implements ExceptionMapper<TooLongRangeException> {
	@Override
	public Response toResponse(TooLongRangeException e) 
	{
    	Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", Status.BAD_REQUEST.name());
        body.put("error", "TOO_LONG_DATERANGE");
        body.put("message", e.getMessage());

		return Response.status(Status.BAD_REQUEST).entity(body).build();  
	}
}
