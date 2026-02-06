package com.exercise.progetto_individuale.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.progetto_individuale.dtos.input_dtos.InputParticipantDto;
import com.exercise.progetto_individuale.services.ParticipantService;

@RestController
@RequestMapping("api/groups/{groupId}/participants")
public class ParticipantController {
    @Autowired
    private ParticipantService pServ;

    @PostMapping("add")
    public void addParticipantsToExistingGroup(@PathVariable UUID groupId, @RequestBody List<InputParticipantDto> dtos)
    {
        pServ.addParticipantsToExistingGroup(groupId, dtos);
    }
}
