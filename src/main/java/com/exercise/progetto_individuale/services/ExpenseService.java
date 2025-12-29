package com.exercise.progetto_individuale.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.progetto_individuale.dtos.InputExpenseDto;
import com.exercise.progetto_individuale.entities.Expense;
import com.exercise.progetto_individuale.entities.ExpenseParticipant;
import com.exercise.progetto_individuale.entities.Participant;
import com.exercise.progetto_individuale.entities.SpendingGroup;
import com.exercise.progetto_individuale.repositories.ExpenseParticipantRepository;
import com.exercise.progetto_individuale.repositories.ExpenseRepository;
import com.exercise.progetto_individuale.repositories.ParticipantRepository;
import com.exercise.progetto_individuale.repositories.SpendingGroupRepository;

@Service
public class ExpenseService
{
    @Autowired
    private SpendingGroupRepository sgRepo;
    @Autowired
    private ExpenseParticipantRepository expParRepo;
    @Autowired
    private ParticipantRepository partRepo;
    @Autowired
    private ExpenseRepository expRepo;

    public void addExpense(UUID spendinGroupId, InputExpenseDto dto)
    {
        Optional<SpendingGroup> sgOp = sgRepo.findById(spendinGroupId);
        if(sgOp.isEmpty())
            throw new RuntimeException("Inexistent group");
        SpendingGroup sg = sgOp.get();

        convertToExpenseAndSave(dto, sg);
    }

    private void convertToExpenseAndSave(InputExpenseDto dto, SpendingGroup sg)
    {
        Expense exp = new Expense();
        exp.setTitle(dto.getTitle());
        exp.setDate(dto.getDate());
        exp.setAmount(dto.getAmount(), dto.getShares());
        for(String partName : dto.getShares().keySet())
        {
            Optional<Participant> pOp = partRepo.findParticipantByNameAndSpendingGroup(partName, sg);
            if(pOp.isEmpty())
                throw new RuntimeException("Inexistent participant");
            Participant p = pOp.get();
            createExpenseParticipant(p, sg, exp, dto);
        }
    }

    private void createExpenseParticipant(Participant p, SpendingGroup sg, Expense exp, InputExpenseDto dto)
    {
        ExpenseParticipant expPart = new ExpenseParticipant();
        expPart.setPayer(dto.getPayer().equalsIgnoreCase(p.getName()) ? true : false);
        expPart.setShare(dto.getShares().get(p.getName()));
        expPart = expParRepo.save(expPart);
        exp.addParticipant(expPart);
        p.addExpense(expPart);
        expRepo.save(exp);
        partRepo.save(p);
    }
}
