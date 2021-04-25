package fi.rikusarlin.housingserver.topdown.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Payload;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import fi.rikusarlin.housingserver.validation.Severity;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
	@Override
	public Response toResponse(ConstraintViolationException e) 
	{
    	Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", Status.BAD_REQUEST.name());
        List<String> errors = new ArrayList<String>();
        for(ConstraintViolation<?> violation:e.getConstraintViolations()) {
        	String message = "UNKNOWN:" + violation.getPropertyPath() + ": " + violation.getMessage();
        	Set<Class<? extends Payload>> payloads =
                    violation.getConstraintDescriptor().getPayload();
        	for (Class<? extends Payload> payload : payloads) {
        		if (payload == Severity.Error.class) {
        			message = "ERROR: " + violation.getPropertyPath() + ": " +
                            violation.getMessage();
        		} else if (payload == Severity.Info.class) {
        			message = "INFO: " + violation.getPropertyPath() + ": " +
                            violation.getMessage();
        		}
        	}
        	errors.add(message);
        }
        body.put("error", "NOTVALID2");
        body.put("message", errors);
        
		return Response.status(Status.BAD_REQUEST).entity(body).build();  
	}
}
