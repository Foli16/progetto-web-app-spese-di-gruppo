package com.exercise.progetto_individuale.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.exercise.progetto_individuale.dtos.input_dtos.InputExpenseParticipantDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
    @NotNull @PastOrPresent
    private LocalDate date;
    @NotNull @PastOrPresent
    private LocalDateTime creationTime;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExpenseParticipant> participants = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private SpendingGroup spendingGroup;

    public void addParticipant(ExpenseParticipant expPart)
    {
        participants.add(expPart);
        expPart.setExpense(this);
    }

    public void setAmount(Map<UUID, InputExpenseParticipantDto> expPart)
    {
        double totalShare = 0;
        double totalPaid = 0;
        for(UUID partId : expPart.keySet())
        {
            totalShare += expPart.get(partId).getShare();
            totalPaid += expPart.get(partId).getPaidAmount();
        }
        if(totalShare != totalPaid)
            throw new RuntimeException("Total share amount must be equal to total paid amount");
        this.amount = totalShare;
    }

    @PrePersist
    public void onCreation()
    {
        this.creationTime = LocalDateTime.now();
    }
}
