package com.ordersmicroservice.orders_microservice.exception;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Date;

@ControllerAdvice
@Generated
public class GlobalExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> localNotFoundException(NotFoundException exception){
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> localBadRequest(BadRequest exception){
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> localServerError (InternalServerErrorException exception){
        ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(GlobalExceptionHandler.ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(GlobalExceptionHandler.ResourceNotFoundException ex) {
        GlobalExceptionHandler.ErrorMessage errorResponse = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage(), new Date());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Invalid parameter: " + ex.getName() + ". Value '" + ex.getValue() +
                "' cannot be converted to type Long ";
        GlobalExceptionHandler.ErrorMessage errorResponse = new ErrorMessage(HttpStatus.BAD_REQUEST, errorMessage, new Date());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Generated
    public static class BadRequest extends RuntimeException{
        public BadRequest(String message) {
            super(message);
        }
    }
    @Generated
    public static class InternalServerErrorException extends RuntimeException {
        public InternalServerErrorException(String message) {
            super(message);
        }

    }
    @Generated
    public static class NotFoundException extends RuntimeException{
        public NotFoundException(String message) {
            super(message);
        }

    }
    @Generated
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @Data
    @AllArgsConstructor
    @Generated
    public static class ErrorMessage {
        private HttpStatus status;
        private String message;
        private Date timestamp;
    }

}
