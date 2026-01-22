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
        exp.setAmount(dto.getExpenseParticipants());
        for(UUID partId : dto.getExpenseParticipants().keySet())
        {
            Optional<Participant> pOp = partRepo.findParticipantByIdAndSpendingGroup(partId, sg);
            if(pOp.isEmpty())
                throw new RuntimeException("Participant with id '" + partId + "' not existent or not present in group");
            Participant p = pOp.get();
            createExpenseParticipant(p, exp, dto);
        }
        sg.addExpenseToTotal(exp.getAmount());
        sgRepo.save(sg);
    }

    private void createExpenseParticipant(Participant p, Expense exp, InputExpenseDto dto)
    {
        ExpenseParticipant expPart = new ExpenseParticipant();
        expPart.setPaidAmount(dto.getExpenseParticipants().get(p.getId()).getPaidAmount());
        expPart.setShare(dto.getExpenseParticipants().get(p.getId()).getShare());
        expPart = expParRepo.save(expPart);
        exp.addParticipant(expPart);
        p.addExpense(expPart);
        p.setBalance(expPart.getPaidAmount() - expPart.getShare());
        expRepo.save(exp);
        partRepo.save(p);
    }
}
