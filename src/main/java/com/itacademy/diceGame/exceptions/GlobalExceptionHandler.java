package com.itacademy.diceGame.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.util.BindErrorUtils;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> PlayerNotFoundException(PlayerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> UserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NoGamesSavedException.class)
    public ResponseEntity<String> NoGamesSavedException(NoGamesSavedException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public ResponseEntity<String> PlayerAlreadyExistsException(PlayerAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> UserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> BadCredentialsException(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> MethodArgumentNotValidException(MethodArgumentNotValidException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BindErrorUtils.resolveAndJoin(ex.getFieldErrors()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> HttpMessageNotReadableException(HttpMessageNotReadableException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input data not valid.\n\t" + ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> MethodArgumentTypeMismatchException(NoResourceFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL not supported.\n\t" + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleError(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Uh oh. Something unexpected happened");
    }
}
