package com.exercise.progetto_individuale.dtos.output_dtos;

import java.util.UUID;

import com.exercise.progetto_individuale.dtos.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonView(Views.Detail.class)
public class OutputParticipantDto {
    private UUID participantId;
    private String participantName;
    private double balance;
    private double participantTotalExpense;
}
