package com.exercise.progetto_individuale.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

        return createAndAddParticipants(dto.getParticipants(), sg);
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
            groups.add(convertToDto(gUser.getSpendingGroup()));
        return groups;
    }

    public List<OutputGroupDto> getLocalGroupList(Set<UUID> participantIds)
    {
        List<SpendingGroup> groupsToBeValidated = new ArrayList<>();
        for(UUID id : participantIds)
        {
            Optional<Participant> op = partRepo.findById(id);
            if(op.isEmpty())
                throw new RuntimeException("Could not find group related to this participant");
            SpendingGroup sg = op.get().getSpendingGroup();
            groupsToBeValidated.add(sg);
        }
        Set<SpendingGroup> groupSet = new HashSet<>(groupsToBeValidated);
        if(groupSet.size() != groupsToBeValidated.size())
            throw new RuntimeException("Something went wrong, local storage might be compromised");
        List<OutputGroupDto> validatedGroups = new ArrayList<>();
        for(SpendingGroup sg : groupSet)
            validatedGroups.add(convertToDto(sg));
        return validatedGroups;
    }

    private Participant createAndAddParticipants(List<ParticipantDto> dtos, SpendingGroup sg)
    {
        List<Participant> participants = new ArrayList<>();

        String lastName = "";
        int counter = 0;
        for(ParticipantDto partDto : dtos)
        {
            if(partDto.getName() == null || partDto.getName().isBlank())
                throw new RuntimeException("Participants' name must not be blank or null");
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

    private OutputGroupDto convertToDto(SpendingGroup sg)
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
