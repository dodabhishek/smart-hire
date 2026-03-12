package com.abhishek.student_api.exception;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// @RestControllerAdvice means this class watches over ALL controllers globally. Whenever any exception is thrown anywhere in your app, 
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    //  @ExceptionHandler says "when THIS specific exception occurs, run this method
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotFound(StudentNotFoundException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value() , ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenricException(Exception ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Something went wrong : "+  ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

    }

    //  MethodArgumentNotValidException is thrown automatically by Spring when @Valid catches bad data. 
    // We intercept it here, loop through all the field errors, and combine them 
    // into one clean message like "name: Name cannot be empty, email: Please provide a valid email address"
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
        MethodArgumentNotValidException ex ){
            String message = ex.getBindingResult()
                                        .getFieldErrors()
                                        .stream()
                                        .map(error -> error.getField() + " : " + error
                                        .getDefaultMessage())
                                        .collect(Collectors.joining(", "));
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        }
}
