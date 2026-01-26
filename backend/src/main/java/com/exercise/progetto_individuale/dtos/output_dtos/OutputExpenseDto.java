package com.exercise.progetto_individuale.dtos.output_dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.exercise.progetto_individuale.dtos.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonView(Views.Detail.class)
public class OutputExpenseDto {
    private String expenseTitle;
    private double amount;
    private LocalDate date;
    private LocalDateTime creationTime;
    private Set<OutputExpenseParticipantDto> expenseParticipants = new HashSet<>();
}
