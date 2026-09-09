package com.example.traning_management.advisor;

import com.example.traning_management.exception.DuplicateNominationException;
import com.example.traning_management.utill.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateNominationException.class)
    public ResponseEntity<StandardResponse> handleDuplicateNomination(
            DuplicateNominationException exception
    ) {

        StandardResponse response = new StandardResponse(
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}