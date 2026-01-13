package com.exercise.progetto_individuale.controllers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.progetto_individuale.dtos.InputExpenseDto;
import com.exercise.progetto_individuale.dtos.InputGroupDto;
import com.exercise.progetto_individuale.dtos.OutputGroupDto;
import com.exercise.progetto_individuale.services.ExpenseService;
import com.exercise.progetto_individuale.services.GroupService;

@RestController
@RequestMapping("api/groups")
public class GroupController
{
    @Autowired
    private GroupService gServ;
    @Autowired
    private ExpenseService eServ;

    @PostMapping("create")
    public UUID createNewGroup(@RequestBody InputGroupDto dto, @CookieValue(required = false) String token)
    {
        if(token != null)
            return gServ.createUserLinkedGroup(dto, token);
        else
            return gServ.createGroup(dto).getId();
    }

    // @PostMapping("{groupId}/addparticipant/{participantName}")
    // public void addParticipant(@PathVariable UUID groupId, @PathVariable String participantName)
    // {
    //     gServ.addParticipant(groupId, participantName);
    // }

    @PostMapping("{groupId}/addexpense")
    public void addExpense(@PathVariable UUID groupId, @RequestBody InputExpenseDto dto)
    {
        eServ.addExpense(groupId, dto);
    }

    @PostMapping("list")
    public List<OutputGroupDto> getGroupList(@CookieValue(required = false) String token, @RequestBody(required = false) Set<UUID> participantIds)
    {
        if(token != null)
            return gServ.getUserGroupList(token);
        else
            return gServ.getLocalGroupList(participantIds);
    }

    // @GetMapping("list")
    // public List<OutputGroupDto> getGroupList(@CookieValue(required = false) String token, @RequestBody(required = false) String[] participantIds)
    // {
    //     return gServ.getGroupList(token, participantIds);
    // }
}
