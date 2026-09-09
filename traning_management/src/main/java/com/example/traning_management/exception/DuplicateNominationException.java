package com.example.traning_management.exception;

public class DuplicateNominationException
        extends RuntimeException {

    public DuplicateNominationException(String message) {
        super(message);
    }
}