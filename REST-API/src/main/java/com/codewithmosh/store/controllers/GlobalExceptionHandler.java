package com.codewithmosh.store.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice//advisor that contains exception handling logic (any time exception is thrown in any class, this will catch it)
public class GlobalExceptionHandler {
    //method for handling validation errors thrown by the @Valid annotation to provide meaningful error messages to client
    @ExceptionHandler(MethodArgumentNotValidException.class) //argument is the type of exception we want to handle
    public ResponseEntity<Map<String,String>> handleValidationErrors(MethodArgumentNotValidException exception){
        //we want to return in the format "name": "Name is required" Map<String, String>
        var errors = new HashMap<String, String>();
        //loop through each error and add it to the map
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);//return the map of errors
    }

}
