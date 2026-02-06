package com.exercise.progetto_individuale.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.progetto_individuale.dtos.input_dtos.InputGroupDto;
import com.exercise.progetto_individuale.dtos.output_dtos.OutputExpenseDto;
import com.exercise.progetto_individuale.dtos.output_dtos.OutputExpenseParticipantDto;
import com.exercise.progetto_individuale.dtos.output_dtos.OutputGroupDto;
import com.exercise.progetto_individuale.dtos.output_dtos.OutputParticipantDto;
import com.exercise.progetto_individuale.entities.Expense;
import com.exercise.progetto_individuale.entities.ExpenseParticipant;
import com.exercise.progetto_individuale.entities.GroupUser;
import com.exercise.progetto_individuale.entities.Participant;
import com.exercise.progetto_individuale.entities.SpendingGroup;
import com.exercise.progetto_individuale.entities.User;
import com.exercise.progetto_individuale.exceptions.GroupAccessDeniedException;
import com.exercise.progetto_individuale.exceptions.LocalStorageErrorException;
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
    @Autowired
    private ParticipantService pServ;

    // METODI DI CREAZIONE GRUPPO

    public Participant createGroup(InputGroupDto dto)
    {
        SpendingGroup sg = new SpendingGroup(dto.getName());

        return pServ.addParticipantsToNewGroup(dto.getParticipants(), sg);
    }

    public UUID createUserLinkedGroup(InputGroupDto dto, String token)
    {
        User u = uServ.findUserByToken(token);
        
        Participant myParticipant = createGroup(dto);

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

    // METODI GET PER I GRUPPI

    /**
     * Questo metodo restituisce la lista di DTO dei gruppi collegati all'utente autenticato.
     * @param token Il token per autenticare l'utente
     * @return La lista di DTO dei gruppi collegati all'utente
     */
    public List<OutputGroupDto> getUserGroupList(String token)
    {
        if(token == null || token.isBlank())
            return new ArrayList<>();
        User u = uServ.findUserByToken(token);
        if(u.getGroups().isEmpty())
            return new ArrayList<>();
        List<OutputGroupDto> groups = new ArrayList<>();
        for(GroupUser gUser : u.getGroups())
            groups.add(convertToGroupPreviewDto(gUser.getSpendingGroup(), gUser.getParticipant()));
        return sortByLastModified(groups);
    }

    /**
     * Questo metodo prende i gruppi "salvati" in locale. Nello specifico si occupa di ricevere un Set di
     * ID di Participant, utilizzato poiché non possono esistere due ID uguali nella lista passata, e ottenerne i relativi
     * SpendingGroup collegati. Siccome non possono esserci due o più ID di Participant legati allo stesso gruppo
     * salvati in locale, perché causerebbe problemi nella corretta visualizzazione dei gruppi, viene fatto un
     * controllo per evitare questa problematica che restituisce un eccezione nel caso il Local Storage sia stato manomesso manualmente.
     * La stessa cosa succede se risulta anche solo un singolo ID di un partecipante che possiede una relazione con un GroupUser, poiché
     * questo potrebbe causare problemi di sicurezza: i gruppi condivisi non possono essere visualizzati localmente.
     * @param participantIds Il Set di ID di partecipanti salvati nel Local Storage del browser
     * @return La lista di DTO dei gruppi collegati ai Participant il cui ID è salvato nel Local Storage del browser dell'utente
     * @exception RuntimeException Lancia eccezione se risulta anche solo un participantId non esistente.
     * @exception LocalStorageErrorException Lancia questa eccezione se il Local Storage è stato manipolato inserendo dati incompatibili.
     */
    public List<OutputGroupDto> getLocalGroupList(Set<UUID> participantIds)
    {
        if(participantIds == null || participantIds.isEmpty())
            return new ArrayList<>();
        List<SpendingGroup> groupsToBeValidated = new ArrayList<>();
        for(UUID id : participantIds)
        {
            Optional<Participant> op = partRepo.findById(id);
            if(op.isEmpty())
                throw new RuntimeException("Could not find group related to this participant");
            if(op.get().getGroupUser() != null)
                throw new LocalStorageErrorException();
            SpendingGroup sg = op.get().getSpendingGroup();
            groupsToBeValidated.add(sg);
        }
        Set<SpendingGroup> groupSet = new HashSet<>(groupsToBeValidated);
        if(groupSet.size() != groupsToBeValidated.size())
            throw new LocalStorageErrorException();
        List<OutputGroupDto> validatedGroups = new ArrayList<>();
        for(SpendingGroup sg : groupSet)
            validatedGroups.add(convertToGroupPreviewDto(sg, sg.getMyParticipant()));
        return sortByLastModified(validatedGroups);
    }

    private List<OutputGroupDto> sortByLastModified(List<OutputGroupDto> dtos)
    {
        return dtos.stream().sorted(
            Comparator.comparing(OutputGroupDto::getLastModified, Comparator.reverseOrder()))
            .toList();
    }

    public OutputGroupDto getGroupDetail(UUID groupId, UUID myParticipantId, String token)
    {
        SpendingGroup sg = findSpendingGroupById(groupId);
        Optional<Participant> pOp = partRepo.findParticipantByIdAndSpendingGroup(myParticipantId, sg);
        if(pOp.isEmpty())
            throw new RuntimeException("Participant not existent or not present in group");
        if(token != null)
        {
            User u = uServ.findUserByToken(token);
            Optional<GroupUser> op = gUserRepo.findByUserAndParticipantId(u, myParticipantId);
            if(op.isEmpty())
                throw new GroupAccessDeniedException();
        }
        else
            if(!sg.getMyParticipant().equals(pOp.get()) || pOp.get().getGroupUser() != null)
                throw new GroupAccessDeniedException();
        return convertToGroupDetailDto(sg, pOp.get());
    }

    // METODI DI CONVERSIONE IN DTO PER LA PREVIEW DEI GRUPPI

    private OutputGroupDto convertToGroupPreviewDto(SpendingGroup sg, Participant p)
    {
        OutputGroupDto dto = new OutputGroupDto();
        dto.setGroupId(sg.getId());
        dto.setGroupName(sg.getName());
        dto.setGroupTotalExpenses(sg.getTotalExpenses());
        dto.setLastModified(sg.getLastModified());
        dto.setMyParticipantId(p.getId());
        dto.setMyParticipantBalance(p.getBalance());
        dto.setMyParticipantTotalExpenses(p.getParticipantTotalExpense());
        return dto;
    }

    // METODI DI CONVERSIONE IN DTO PER IL DETTAGLIO DEL SINGOLO GRUPPO

    private OutputGroupDto convertToGroupDetailDto(SpendingGroup sg, Participant p)
    {
        OutputGroupDto dto = convertToGroupPreviewDto(sg, p);
        dto.setParticipants(convertParticipantsToDto(sg));
        dto.setExpenses(convertExpensesToDto(sg));
        return dto;
    }

    private Set<OutputParticipantDto> convertParticipantsToDto(SpendingGroup sg)
    {
        Set<OutputParticipantDto> dtos = new HashSet<>();
        for(Participant p : sg.getParticipants())
        {
            OutputParticipantDto dto = new OutputParticipantDto();
            dto.setParticipantId(p.getId());
            dto.setParticipantName(p.getName());
            dto.setBalance(p.getBalance());
            dto.setParticipantTotalExpense(p.getParticipantTotalExpense());
            dtos.add(dto);
        }
        return dtos;
    }

    private Set<OutputExpenseDto> convertExpensesToDto(SpendingGroup sg)
    {
        Set<OutputExpenseDto> dtos = new HashSet<>();
        for(Expense e : sg.getExpenses())
        {
            OutputExpenseDto dto = new OutputExpenseDto();
            dto.setExpenseTitle(e.getTitle());
            dto.setAmount(e.getAmount());
            dto.setDate(e.getDate());
            dto.setCreationTime(e.getCreationTime());
            dto.setExpenseParticipants(convertExpenseParticipantsToDto(e));
            dtos.add(dto);
        }
        return dtos.stream().sorted(
            Comparator.comparing(OutputExpenseDto::getDate, Comparator.reverseOrder())
            .thenComparing(OutputExpenseDto::getCreationTime, Comparator.reverseOrder()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<OutputExpenseParticipantDto> convertExpenseParticipantsToDto(Expense e)
    {
        Set<OutputExpenseParticipantDto> dtos = new HashSet<>();
        for(ExpenseParticipant expPart : e.getParticipants())
        {
            OutputExpenseParticipantDto dto = new OutputExpenseParticipantDto();
            dto.setExpenseParticipantName(expPart.getParticipant().getName());
            dto.setPaidAmount(expPart.getPaidAmount());
            dto.setShare(expPart.getShare());
            dtos.add(dto);
        }
        return dtos;
    }

    public SpendingGroup findSpendingGroupById(UUID groupId)
    {
        Optional<SpendingGroup> gOp = sgRepo.findById(groupId);
        if(gOp.isEmpty())
            throw new RuntimeException("Group not found");
        return gOp.get();
    }
}
