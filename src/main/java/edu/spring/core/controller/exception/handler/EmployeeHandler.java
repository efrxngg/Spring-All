package edu.spring.core.controller.exception.handler;

import edu.spring.core.exception.EmployeeException;
import edu.spring.core.util.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class EmployeeHandler {

    @ExceptionHandler(value = {EmployeeException.class})
    public ResponseEntity<ErrorMessage> badRequestException(EmployeeException e, WebRequest request) {
        return status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage(), request.getDescription(false)));
    }

}
