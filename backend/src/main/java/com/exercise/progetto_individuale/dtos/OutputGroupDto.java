package com.exercise.progetto_individuale.dtos;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutputGroupDto {
    private String name;
    private double totalExpenses;
    private double myParticipantBalance;
    private double myParticipantTotalExpenses;
}
