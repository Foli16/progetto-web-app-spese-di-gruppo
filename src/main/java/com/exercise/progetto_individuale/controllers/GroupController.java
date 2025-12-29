package com.exercise.progetto_individuale.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.progetto_individuale.dtos.InputExpenseDto;
import com.exercise.progetto_individuale.dtos.InputGroupDto;
import com.exercise.progetto_individuale.dtos.InputParticipantDto;
import com.exercise.progetto_individuale.services.ExpenseService;
import com.exercise.progetto_individuale.services.GroupService;

@RestController
@RequestMapping("api/group")
public class GroupController
{
    @Autowired
    private GroupService gServ;
    @Autowired
    private ExpenseService eServ;

    @PostMapping("creategroup")
    public void createNewGroup(@RequestBody InputGroupDto dto)
    {
        gServ.createGroup(dto);
    }

    @PostMapping("addparticipant/{id}")
    public void addParticipant(@PathVariable UUID id, @RequestBody InputParticipantDto dto)
    {
        gServ.addParticipant(id, dto);
    }

    @PostMapping("addexpense/{id}")
    public void addExpense(@PathVariable UUID id, @RequestBody InputExpenseDto dto)
    {
        eServ.addExpense(id, dto);
    }
}
