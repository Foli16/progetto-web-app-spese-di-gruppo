package com.exercise.progetto_individuale.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.progetto_individuale.dtos.input_dtos.InputExpenseDto;
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
        List<Participant> validatedParticipants = partRepo.findAllByIdInAndSpendingGroup(dto.getExpenseParticipants().keySet(), sg); //non va bene, deve dare eccezione se non ne trova uno
        Expense exp = new Expense();
        exp.setTitle(dto.getTitle());
        exp.setDate(dto.getDate());
        exp.setAmount(dto.getExpenseParticipants());
        for(Participant p : validatedParticipants)
            exp = createExpenseParticipant(p, exp, dto);
        sg.addExpense(exp);
        sg.addExpenseToTotal(exp.getAmount());
        sg.setLastModified(LocalDateTime.now());
        sgRepo.save(sg);
    }

    private Expense createExpenseParticipant(Participant p, Expense exp, InputExpenseDto dto)
    {
        ExpenseParticipant expPart = new ExpenseParticipant();
        expPart.setPaidAmount(dto.getExpenseParticipants().get(p.getId()).getPaidAmount());
        expPart.setShare(dto.getExpenseParticipants().get(p.getId()).getShare());
        expPart = expParRepo.save(expPart);
        exp.addParticipant(expPart);
        exp = expRepo.save(exp);
        p.addExpense(expPart);
        p.setBalance(expPart.getPaidAmount() - expPart.getShare());
        partRepo.save(p);
        return exp;
    }
}
