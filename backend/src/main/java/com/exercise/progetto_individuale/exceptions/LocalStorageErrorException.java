package com.exercise.progetto_individuale.exceptions;

public class LocalStorageErrorException extends RuntimeException {
    public LocalStorageErrorException()
    {
        super("Something went wrong, local storage might be compromised");
    }
}
