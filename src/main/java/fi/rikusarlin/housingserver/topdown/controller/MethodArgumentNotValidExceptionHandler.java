package fi.rikusarlin.housingserver.topdown.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.web.bind.MethodArgumentNotValidException;

@Provider
public class MethodArgumentNotValidExceptionHandler implements ExceptionMapper<MethodArgumentNotValidException> {
	@Override
	public Response toResponse(MethodArgumentNotValidException e) 
	{
    	Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", Status.BAD_REQUEST.name());
        body.put("error", "NOT_VALID_ARGUMENT");
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField()+ ":" + x.getDefaultMessage()) // + ", value: " + x.getRejectedValue())
                .collect(Collectors.toList());
        body.put("message", errors);

		return Response.status(Status.BAD_REQUEST).entity(body).build();  
	}
}
