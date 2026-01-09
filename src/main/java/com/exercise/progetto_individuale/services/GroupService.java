package com.exercise.progetto_individuale.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.progetto_individuale.dtos.InputGroupDto;
import com.exercise.progetto_individuale.dtos.OutputGroupDto;
import com.exercise.progetto_individuale.dtos.ParticipantDto;
import com.exercise.progetto_individuale.entities.GroupUser;
import com.exercise.progetto_individuale.entities.Participant;
import com.exercise.progetto_individuale.entities.SpendingGroup;
import com.exercise.progetto_individuale.entities.User;
import com.exercise.progetto_individuale.exceptions.UniqueNameCostraintException;
import com.exercise.progetto_individuale.repositories.GroupUserRepository;
import com.exercise.progetto_individuale.repositories.ParticipantRepository;
import com.exercise.progetto_individuale.repositories.SpendingGroupRepository;
import com.exercise.progetto_individuale.repositories.UserRepository;

@Service
public class GroupService
{
    @Autowired
    private ParticipantRepository partRepo;
    @Autowired
    private SpendingGroupRepository sgRepo;
    @Autowired
    private UserService uServ;
    @Autowired
    private GroupUserRepository gUserRepo;
    @Autowired
    private UserRepository uRepo;

    public Participant createGroup(InputGroupDto dto)
    {
        SpendingGroup sg = new SpendingGroup(dto.getName());

        List<Participant> participants = new ArrayList<>();

        String lastName = "";
        int counter = 0;
        for(ParticipantDto partDto : dto.getParticipants())
        {
            if(partDto.getName().isBlank() || partDto.getName() == null)
                throw new RuntimeException("Participants' name must not be blank");
            Participant p = new Participant(partDto.getName());
            if(lastName.equalsIgnoreCase(p.getName()))
                throw new UniqueNameCostraintException();
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
        participants = partRepo.saveAll(participants);

        Participant founder = null;
        for(Participant p : participants)
        {
            sg.addParticipant(p);
            if(p.isFounder())
                founder = p;
        }
        sgRepo.save(sg);
        return founder;
    }

    public UUID createUserLinkedGroup(InputGroupDto dto, String token)
    {
        Participant myParticipant = createGroup(dto);

        User u = uServ.findUserByToken(token);

        GroupUser gUser = new GroupUser(true);
        gUser = gUserRepo.save(gUser);

        gUser.setParticipant(myParticipant);
        u.addGroup(gUser);
        myParticipant.getSpendingGroup().addUser(gUser);
        uRepo.save(u);
        sgRepo.save(myParticipant.getSpendingGroup());
        gUserRepo.save(gUser);

        return null;
    }

    // public void addParticipant(SpendingGroup sg, String participantName)
    // {
    //     Participant p = new Participant(participantName);
    //     p = partRepo.save(p);
    //     sg.addParticipant(p);
    //     sgRepo.save(sg);
    // }

    // public void addParticipant(UUID id, String participantName)
    // {
    //     Optional<SpendingGroup> op = sgRepo.findById(id);
    //     if(op.isEmpty())
    //         throw new RuntimeException("Inexistent group");
    //     SpendingGroup sg = op.get();
    //     Participant p = new Participant(participantName);
    //     p = partRepo.save(p);
    //     sg.addParticipant(p);
    //     sgRepo.save(sg);
    // }

    public List<OutputGroupDto> getUserGroupList(String token)
    {
        if(token == null)
            return null;
        User u = uServ.findUserByToken(token);
        List<OutputGroupDto> groups = new ArrayList<>();
        for(GroupUser gUser : u.getGroups())
            groups.add(convertoToDto(gUser.getSpendingGroup()));
        return groups;
    }

    public List<OutputGroupDto> getLocalGroupList(UUID[] participantIds) //da aggiustare per non far uscire più volte lo stesso gruppo
    {
        List<OutputGroupDto> groups = new ArrayList<>();
        for(UUID id : participantIds)
        {
            Optional<Participant> op = partRepo.findById(id);
            if(op.isEmpty())
                throw new RuntimeException("Could not find participant");
            Participant p = op.get();
            groups.add(convertoToDto(p.getSpendingGroup()));
        }
        return groups;
    }

    private OutputGroupDto convertoToDto(SpendingGroup sg)
    {
        OutputGroupDto dto = new OutputGroupDto();
        dto.setName(sg.getName());
        return dto;
    }

//     private SpendingGroup convertDtoToEntity(InputGroupDto dto)
//     {
//         SpendingGroup sg = new SpendingGroup();
//         sg.setName(dto.getName());
//         sg.setParticipants(dto.getPartecipants());
//     }
}
