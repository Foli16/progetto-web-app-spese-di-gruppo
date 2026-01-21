package com.exercise.progetto_individuale.dtos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputExpenseDto
{
    private String title;
    private LocalDate date;
    private Map<UUID, InputExpenseParticipantDto> expenseParticipants = new HashMap<>();
}
