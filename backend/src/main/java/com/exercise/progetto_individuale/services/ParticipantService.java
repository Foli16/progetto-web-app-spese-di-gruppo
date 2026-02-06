package com.exercise.progetto_individuale.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.progetto_individuale.dtos.input_dtos.InputParticipantDto;
import com.exercise.progetto_individuale.entities.Participant;
import com.exercise.progetto_individuale.entities.SpendingGroup;
import com.exercise.progetto_individuale.exceptions.UniqueNameCostraintException;
import com.exercise.progetto_individuale.repositories.ParticipantRepository;
import com.exercise.progetto_individuale.repositories.SpendingGroupRepository;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository partRepo;
    @Autowired
    private SpendingGroupRepository sgRepo;

    // METODI DI CREAZIONE PARTECIPANTI PER NUOVO GRUPPO

    public Participant addParticipantsToNewGroup(List<InputParticipantDto> dtos, SpendingGroup sg)
    {
        List<Participant> participants = saveParticipants(dtos);

        Participant myParticipant = null;
        for(Participant p : participants)
        {
            sg.addParticipant(p);
            if(p.isFounder())
                myParticipant = p;
        }
        sgRepo.save(sg);
        return myParticipant;
    }

    private List<Participant> saveParticipants(List<InputParticipantDto> dtos)
    {
        List<Participant> participants = new ArrayList<>();
        
        String lastName = "";
        int counter = 0;
        for(InputParticipantDto partDto : dtos)
        {
            Participant p = createParticipant(partDto.getName(), lastName);
            if(partDto.isFounder())
            {
                counter++;
                p.setFounder(partDto.isFounder());
            }
            lastName = p.getName();
            participants.add(p);
        }
        if(counter != 1)
            throw new RuntimeException("There must be one founder per group");
        return partRepo.saveAll(participants);
    }

    // METODI DI CREAZIONE PARTECIPANTI IN GRUPPO ESISTENTE

    public void addParticipantsToExistingGroup(UUID spendingGroupId, List<InputParticipantDto> dtos)
    {
        Optional<SpendingGroup> gOp = sgRepo.findById(spendingGroupId);
        if(gOp.isEmpty())
            throw new RuntimeException("Group not found");
        SpendingGroup sg = gOp.get();
        List<Participant> participants = saveParticipants(dtos, sg);

        for(Participant p : participants)
            sg.addParticipant(p);
        sgRepo.save(sg);
    }

    private List<Participant> saveParticipants(List<InputParticipantDto> dtos, SpendingGroup sg)
    {
        List<Participant> participants = new ArrayList<>();
        
        String lastName = "";
        for(InputParticipantDto partDto : dtos)
        {
            Participant p = createParticipant(partDto.getName(), lastName);
            if(partRepo.existsByNameAndSpendingGroup(p.getName(), sg))
                throw new UniqueNameCostraintException();
            lastName = p.getName();
            participants.add(p);
        }
        return partRepo.saveAll(participants);
    }

    // METODO CREAZIONE SINGOLO PARTECIPANTE

    private Participant createParticipant(String dtoName, String lastName)
    {
        if(dtoName == null || dtoName.isBlank())
            throw new RuntimeException("Participants' name must not be blank or null");
        if(lastName.equalsIgnoreCase(dtoName))
            throw new UniqueNameCostraintException();
        return new Participant(dtoName);
    }
}
