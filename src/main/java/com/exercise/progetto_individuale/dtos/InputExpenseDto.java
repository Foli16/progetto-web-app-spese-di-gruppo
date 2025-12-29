package com.exercise.progetto_individuale.dtos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputExpenseDto
{
    private String title;
    private double amount;
    private LocalDate date;
    private String payer;
    private Map<String, Double> shares = new HashMap<>();
}
