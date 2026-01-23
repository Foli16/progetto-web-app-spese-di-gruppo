package com.exercise.progetto_individuale.dtos.output_dtos;

import com.exercise.progetto_individuale.dtos.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonView(Views.Detail.class)
public class OutputExpenseParticipantDto {
    private String expenseParticipantName;
    private double paidAmount;
    private double share;
}
