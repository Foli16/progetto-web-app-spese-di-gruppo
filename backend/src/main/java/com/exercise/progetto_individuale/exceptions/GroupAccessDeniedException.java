package com.exercise.progetto_individuale.exceptions;

public class GroupAccessDeniedException extends RuntimeException{
    public GroupAccessDeniedException()
    {
        super("Cannot access the group with this participant ID");
    }
}
