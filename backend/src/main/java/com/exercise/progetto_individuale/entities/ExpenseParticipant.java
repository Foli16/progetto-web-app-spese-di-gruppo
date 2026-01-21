package com.exercise.progetto_individuale.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ExpenseParticipant extends BaseEntity
{
    private double paidAmount;
    private double share;

    @ManyToOne(fetch = FetchType.LAZY)
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    private Expense expense;
}
