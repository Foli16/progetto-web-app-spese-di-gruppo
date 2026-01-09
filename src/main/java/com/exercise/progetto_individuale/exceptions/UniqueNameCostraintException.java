package com.exercise.progetto_individuale.exceptions;

public class UniqueNameCostraintException extends RuntimeException
{
    public UniqueNameCostraintException()
    {
        super("Participants' name must be unique within the group");
    }
}
