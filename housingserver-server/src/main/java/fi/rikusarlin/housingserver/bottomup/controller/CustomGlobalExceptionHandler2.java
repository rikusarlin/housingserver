package fi.rikusarlin.housingserver.bottomup.controller;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

import fi.rikusarlin.housingserver.exception.ApiErrorMessage;
import fi.rikusarlin.housingserver.exception.DuplicateNotAllowedException;
import fi.rikusarlin.housingserver.exception.ErrorMessageMessage;
import fi.rikusarlin.housingserver.exception.NotFoundException;
import fi.rikusarlin.housingserver.exception.TooLongRangeException;
import fi.rikusarlin.housingserver.validation.Severity;

@ControllerAdvice
public class CustomGlobalExceptionHandler2 extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
    	ApiErrorMessage error = new ApiErrorMessage();
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus(status.value());
        error.setError("NOTVALID1");
        List<ErrorMessageMessage> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> {
                	ErrorMessageMessage emm = new ErrorMessageMessage();
                	emm.setFieldName(x.getField());
                	emm.setErrorMessage(x.getDefaultMessage());
                	emm.setErrorCode("ERROR");
                    return emm;
                })
                .collect(Collectors.toList());       
        error.setMessage(errors);
        error.setPath(request.getContextPath());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> springHandleConstraintViolation(ConstraintViolationException cve, WebRequest request) {
    	ApiErrorMessage error = new ApiErrorMessage();
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        List<ErrorMessageMessage> messages = new ArrayList<ErrorMessageMessage>();
        for(ConstraintViolation<?> violation:cve.getConstraintViolations()) {
        	ErrorMessageMessage emm = new ErrorMessageMessage();
        	emm.setErrorCode("UNKNOWN");
        	emm.setFieldName(violation.getPropertyPath().toString());
        	emm.setErrorMessage(violation.getMessage());
        	Set<Class<? extends Payload>> payloads =
                    violation.getConstraintDescriptor().getPayload();
        	for (Class<? extends Payload> payload : payloads) {
        		if (payload == Severity.Error.class) {
        			emm.setErrorCode("ERROR");
        		} else if (payload == Severity.Info.class) {
        			emm.setErrorCode("INFO");
        		}
        	}
        	messages.add(emm);
        }
        error.setError("NOTVALID2");
        error.setMessage(messages);
        error.setPath(request.getContextPath());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(TooLongRangeException.class)
    public final ResponseEntity<Object> springHandleTooLong(TooLongRangeException tlre, WebRequest request) {
    	ApiErrorMessage error = new ApiErrorMessage();
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("TOO_LONG_DATERANGE");
        List<ErrorMessageMessage> messages = new ArrayList<ErrorMessageMessage>();
        ErrorMessageMessage emm = new ErrorMessageMessage();
        emm.setErrorMessage(tlre.getMessage());
        emm.setErrorCode("ERROR");
        messages.add(emm);
        error.setMessage(messages);
        error.setPath(request.getContextPath());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> springHandleTooLong(NotFoundException nfe, WebRequest request) {
    	ApiErrorMessage error = new ApiErrorMessage();
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("NOT_FOUND");
        List<ErrorMessageMessage> messages = new ArrayList<ErrorMessageMessage>();
        ErrorMessageMessage emm = new ErrorMessageMessage();
        emm.setErrorMessage(nfe.getMessage());
        emm.setErrorCode("ERROR");
        messages.add(emm);
        error.setMessage(messages);
        error.setPath(request.getContextPath());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateNotAllowedException.class)
    public final ResponseEntity<Object> springHandleDuplicate(DuplicateNotAllowedException dnae, WebRequest request) {
    	ApiErrorMessage error = new ApiErrorMessage();
        error.setTimestamp(OffsetDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError("DUPLICATE_NOT_ALLOWED");
        List<ErrorMessageMessage> messages = new ArrayList<ErrorMessageMessage>();
        ErrorMessageMessage emm = new ErrorMessageMessage();
        emm.setErrorMessage(dnae.getMessage());
        emm.setErrorCode("ERROR");
        messages.add(emm);
        error.setMessage(messages);
        error.setPath(request.getContextPath());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
