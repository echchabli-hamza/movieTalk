package com.MovieTalk.MT.exception.globalExeptionh;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {


        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, String>> handleValidation(
                        MethodArgumentNotValidException ex) {

                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                return ResponseEntity.badRequest().body(errors);
        }


        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, String>> handleRuntime(
                        RuntimeException ex) {

                return ResponseEntity
                                .badRequest()
                                .body(Map.of("error", ex.getMessage()));
        }


        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleAll(
                        Exception ex) {

                ex.printStackTrace();

                return ResponseEntity
                                .status(500)
                                .body(Map.of("error", "Internal server error"));
        }
}