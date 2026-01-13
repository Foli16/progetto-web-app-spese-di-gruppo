package com.exercise.progetto_individuale.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Expense extends BaseEntity
{
    @NotNull @NotBlank
    private String title;
    @Positive
    private double amount;
    @NotNull
    private LocalDate date;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExpenseParticipant> participants = new HashSet<>();

    public void addParticipant(ExpenseParticipant expPart)
    {
        participants.add(expPart);
        expPart.setExpense(this);
    }

    public void setAmount(double expenseAmount, Map<UUID, Double> shares)
    {
        double shareAmount = 0;
        for(UUID partId : shares.keySet())
            shareAmount += shares.get(partId);
        if(shareAmount != expenseAmount)
            throw new RuntimeException("Expense amount must be equal to shares amount");
        this.amount = expenseAmount;
    }
}
