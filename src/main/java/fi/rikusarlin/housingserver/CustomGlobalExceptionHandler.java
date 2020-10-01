package fi.rikusarlin.housingserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Payload;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import fi.rikusarlin.housingserver.validation.Severity;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public void springHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    // error handle for @Validated
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("error", "NOTVALID1");
        body.put("message", errors);
        body.put("path", request.getContextPath());
        return new ResponseEntity<>(body, headers, status);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> springHandleConstraintViolation(ConstraintViolationException cve, WebRequest request) {
    	Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        List<String> errors = new ArrayList<String>();
        for(ConstraintViolation<?> violation:cve.getConstraintViolations()) {
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
        body.put("path", request.getContextPath());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(TooLongRangeException.class)
    public final ResponseEntity<Object> springHandleConstraintViolation(TooLongRangeException tlre, WebRequest request) {
    	Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "TOOLONGRANGE");
        body.put("message", tlre.getMessage());
        body.put("path", request.getContextPath());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
