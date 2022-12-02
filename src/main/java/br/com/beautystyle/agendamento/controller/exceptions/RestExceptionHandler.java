package br.com.beautystyle.agendamento.controller.exceptions;

import br.com.beautystyle.agendamento.model.ResponseHandler;
import javax.persistence.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import javax.validation.ConstraintViolationException;


@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return ResponseHandler.generateResponse("Validation Error " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseHandler.generateResponse(ex.getCause().toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestNotAllowException.class)
    private ResponseEntity<Object> handleRequestNotAllow(RequestNotAllowException ex) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(StartTimeNotAvailableException.class)
    private ResponseEntity<String> handleStartTimeNotAvailable(StartTimeNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(DurationTimeNotAvailableException.class)
    private ResponseEntity<Object> handleDurationTimeNotAvailable(DurationTimeNotAvailableException ex) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(TenantNotEqualsException.class)
    private ResponseEntity<Object> handleTenantNotEquals(TenantNotEqualsException ex) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventDateNotAvailableException.class)
    private ResponseEntity<Object> handleEventDateNotAvailable(EventDateNotAvailableException ex) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(EventTimeNotAvailableException.class)
    private ResponseEntity<Object> handleDurationTimeNotAvailable(EventTimeNotAvailableException ex) {
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

}
