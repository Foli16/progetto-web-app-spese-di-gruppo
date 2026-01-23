package com.exercise.progetto_individuale.dtos.output_dtos;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.exercise.progetto_individuale.dtos.Views;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutputGroupDto {
    @JsonView(Views.Preview.class)
    private UUID groupId;
    @JsonView(Views.Preview.class)
    private String groupName;
    @JsonView(Views.Preview.class)
    private double groupTotalExpenses;
    @JsonView(Views.Preview.class)
    private UUID myParticipantId;
    @JsonView(Views.Preview.class)
    private double myParticipantBalance;
    @JsonView(Views.Preview.class)
    private double myParticipantTotalExpenses;

    @JsonView(Views.Detail.class)
    private Set<OutputParticipantDto> participants = new HashSet<>();
    @JsonView(Views.Detail.class)
    private Set<OutputExpenseDto> expenses = new HashSet<>();
}
