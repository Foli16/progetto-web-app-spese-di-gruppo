package com.exercise.progetto_individuale.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.progetto_individuale.dtos.InputGroupDto;
import com.exercise.progetto_individuale.dtos.InputParticipantDto;
import com.exercise.progetto_individuale.entities.Participant;
import com.exercise.progetto_individuale.entities.SpendingGroup;
import com.exercise.progetto_individuale.repositories.ParticipantRepository;
import com.exercise.progetto_individuale.repositories.SpendingGroupRepository;

@Service
public class GroupService
{
    @Autowired
    private ParticipantRepository partRepo;
    @Autowired
    private SpendingGroupRepository sgRepo;

    public void createGroup(InputGroupDto dto)
    {
        SpendingGroup sg = convertToSpendingGroup(dto);
        sg = sgRepo.save(sg);
        System.out.println("ID gruppo " + sg.getName() + ": " + sg.getId());
    }

    public void addParticipant(UUID id, InputParticipantDto dto)
    {
        Optional<SpendingGroup> op = sgRepo.findById(id);
        if(op.isEmpty())
            throw new RuntimeException("Inexistent group");
        SpendingGroup sg = op.get();
        Participant p = convertToParticipant(dto);
        p = partRepo.save(p);
        sg.addParticipant(p);
        sgRepo.save(sg);
    }

    private SpendingGroup convertToSpendingGroup(InputGroupDto dto)
    {
        SpendingGroup sg = new SpendingGroup();
        sg.setName(dto.getName());
        return sg;
    }
    

    private Participant convertToParticipant(InputParticipantDto dto)
    {
        Participant p = new Participant();
        p.setName(dto.getName());
        return p;
    }
}
