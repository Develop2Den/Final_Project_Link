package com.finalProject.linkedin.exception.handler;


import com.finalProject.linkedin.dto.error.ErrorResp;
import com.finalProject.linkedin.exception.ExceptionConstants;
import com.finalProject.linkedin.exception.InvalidRequestException;
import com.finalProject.linkedin.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResp> handleNotFoundException(NotFoundException ex) {
        log.debug("Route not found: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResp(ExceptionConstants.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResp> handleInvalidRequestException(InvalidRequestException ex) {
        log.debug("Invalid arguments in request: {}", ex.getMessage());
        return new ResponseEntity<>(new ErrorResp(ExceptionConstants.INVALID_REQUEST), HttpStatus.BAD_REQUEST);
    }
}
