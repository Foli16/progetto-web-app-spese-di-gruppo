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

import com.exercise.progetto_individuale.dtos.Views;
import com.exercise.progetto_individuale.dtos.input_dtos.InputGroupDto;
import com.exercise.progetto_individuale.dtos.output_dtos.OutputGroupDto;
import com.exercise.progetto_individuale.services.GroupService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("api/groups")
public class GroupController
{
    @Autowired
    private GroupService gServ;

    @PostMapping("create")
    public UUID createNewGroup(@RequestBody InputGroupDto dto, @CookieValue(required = false) String token)
    {
        if(token != null)
            return gServ.createUserLinkedGroup(dto, token);
        else
            return gServ.createGroup(dto).getId();
    }

    @PostMapping("list")
    @JsonView(Views.Preview.class)
    public List<OutputGroupDto> getGroupList(@CookieValue(required = false) String token, @RequestBody(required = false) Set<UUID> participantIds)
    {
        if(token != null)
            return gServ.getUserGroupList(token);
        else
            return gServ.getLocalGroupList(participantIds);
    }

    @GetMapping("{groupId}/{myParticipantId}")
    @JsonView(Views.Detail.class)
    public OutputGroupDto getGroupDetail(@PathVariable UUID groupId, @PathVariable UUID myParticipantId, @CookieValue(required = false) String token)
    {
        return gServ.getGroupDetail(groupId, myParticipantId, token);
    }
}
