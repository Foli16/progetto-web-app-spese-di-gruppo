package com.exercise.progetto_individuale.services;

import java.util.ArrayList;
import java.util.List;
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

    public void createGroup(InputGroupDto dto, String token)
    {
        SpendingGroup sg = new SpendingGroup(dto.getName());
        GroupUser gUser = null;
        if(token != null) //se è loggato inizio a creare il collegamento
        {
            gUser = new GroupUser(true);
        }
        List<Participant> participants = new ArrayList<>();

        String lastName = "";
        int counter = 0;
        for(ParticipantDto partDto : dto.getParticipants())
        {
            Participant p = new Participant(partDto.getName()); //se il name è blank o null darà eccezione in questo punto
            participants.add(p);
            if(lastName.equalsIgnoreCase(p.getName()))
                throw new RuntimeException("Participant names must be unique within the group");
            if(partDto.isFounder()) //se è loggato ed è indicato come founder collego l'attuale Participant al GroupUser
            {
                counter++;
                if(token != null)
                    gUser.setParticipant(p);
            }
            lastName = p.getName();
        }
        if(counter != 1)
            throw new RuntimeException("There must be only one founder");
        if(token != null)
            gUser = gUserRepo.save(gUser);
        participants = partRepo.saveAll(participants);
        // sg = sgRepo.save(sg);

        for(Participant p : participants) //aggiungo Partecipants allo SpendingGroup
            sg.addParticipant(p);
        sg = sgRepo.save(sg);

        if(token != null) //se è loggato, ultimo il collegamento tra User e SpendingGroup
        {
            User u = uServ.findUserByToken(token);
            u.addGroup(gUser);
            sg.addUser(gUser);
            uRepo.save(u);
            sgRepo.save(sg);
        }
    }

    public UUID createLocalGroup(InputGroupDto dto)
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
        return founder.getId();
    }

    public UUID createUserLinkedGroup(InputGroupDto dto, String token)
    {
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

    public List<OutputGroupDto> getGroupList()
    {
        List<OutputGroupDto> groupNames = new ArrayList<>();
        for(SpendingGroup sg : sgRepo.findAll())
            groupNames.add(convertoToDto(sg));
        return groupNames;
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
