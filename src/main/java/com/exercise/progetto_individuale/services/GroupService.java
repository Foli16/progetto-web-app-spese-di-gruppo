package com.exercise.progetto_individuale.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void createGroup(String name)
    {
        SpendingGroup sg = new SpendingGroup(name);
        sgRepo.save(sg);
    }

    public void addParticipant(UUID id, String participantName)
    {
        Optional<SpendingGroup> op = sgRepo.findById(id);
        if(op.isEmpty())
            throw new RuntimeException("Inexistent group");
        SpendingGroup sg = op.get();
        Participant p = new Participant(participantName);
        p = partRepo.save(p);
        sg.addParticipant(p);
        sgRepo.save(sg);
    }
}
