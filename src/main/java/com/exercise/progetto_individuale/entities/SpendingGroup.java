package com.exercise.progetto_individuale.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SpendingGroup extends BaseEntity
{
    @NotBlank @NotNull
    private String name;
    private double totalExpenses;

    @OneToMany(mappedBy = "spendingGroup", cascade = CascadeType.ALL)
    private Set<GroupUser> users = new HashSet<>();

    @OneToMany(mappedBy = "spendingGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Participant> participants = new HashSet<>();
    
    public SpendingGroup(String name)
    {
        this.name = name;
    }

    public void addParticipant(Participant p)
    {
        this.participants.add(p);
        p.setSpendingGroup(this);
    }

    /* public void setTotalExpenses()
    {
        double expenses = 0;
        for(Participant p : participants)
            expenses += p.getBalance();
        this.totalExpenses = expenses;
    } */
}
