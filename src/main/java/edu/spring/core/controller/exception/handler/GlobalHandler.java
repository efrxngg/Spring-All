package edu.spring.core.controller.exception.handler;

import edu.spring.core.exception.NotFoundException;
import edu.spring.core.util.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ErrorMessage> notFoundException(NotFoundException e, WebRequest request) {
        return status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(e.getMessage(), request.getDescription(false)));
    }

}
