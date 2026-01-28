package com.exercise.progetto_individuale.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
    @PastOrPresent
    private LocalDateTime lastModified;

    @OneToMany(mappedBy = "spendingGroup", cascade = CascadeType.ALL)
    private Set<GroupUser> users = new HashSet<>();

    @OneToMany(mappedBy = "spendingGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Participant> participants = new HashSet<>();

    @OneToMany(mappedBy = "spendingGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Expense> expenses = new HashSet<>();
    
    public SpendingGroup(String name)
    {
        this.name = name;
        this.lastModified = LocalDateTime.now();
    }

    public void addParticipant(Participant p)
    {
        this.participants.add(p);
        p.setSpendingGroup(this);
    }

    public void addUser(GroupUser gUser)
    {
        this.users.add(gUser);
        gUser.setSpendingGroup(this);
    }

    public void addExpense(Expense expense)
    {
        this.expenses.add(expense);
        expense.setSpendingGroup(this);
    }

    public void addExpenseToTotal(double expense)
    {
        this.totalExpenses += expense;
    }

    public Participant getMyParticipant()
    {
        return this.participants.stream().filter(p -> p.isFounder()).findFirst().orElse(null);
    }
}
