package com.exercise.progetto_individuale.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Participant extends BaseEntity
{
    @NotNull @NotBlank
    private String name;
    private double balance;
    private boolean founder;

    @ManyToOne(fetch = FetchType.EAGER)
    private SpendingGroup spendingGroup;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private Set<ExpenseParticipant> expenses = new HashSet<>();

    @OneToOne(mappedBy = "participant", fetch = FetchType.LAZY)
    private GroupUser groupUser;

    public Participant(String name)
    {
        this.name = name;
    }

    public void addExpense(ExpenseParticipant expPart)
    {
        expenses.add(expPart);
        expPart.setParticipant(this);
    }
}
