package com.exercise.progetto_individuale;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import com.exercise.progetto_individuale.entities.Participant;
import com.exercise.progetto_individuale.entities.SpendingGroup;
import com.exercise.progetto_individuale.repositories.ParticipantRepository;
import com.exercise.progetto_individuale.repositories.SpendingGroupRepository;

@SpringBootTest
@ActiveProfiles("database_fittizio")
public class ParticipantRepositoryTest {
    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private SpendingGroupRepository spendingGroupRepository;

    @Test
    void shouldFailWhenSavingDuplicateParticipantInSameGroup() {
        participantRepository.deleteAll();
        spendingGroupRepository.deleteAll();
        // given
        SpendingGroup group = new SpendingGroup();
        group.setName("Vacanze");
        spendingGroupRepository.saveAndFlush(group);

        Participant p1 = new Participant();
        p1.setName("Mario");
        group.addParticipant(p1);

        Participant p2 = new Participant();
        p2.setName("Mario");
        group.addParticipant(p2);

        // when
        participantRepository.saveAndFlush(p1);

        // then
        try{
            participantRepository.saveAndFlush(p2);
            fail("non doveva salvare");
        }
        catch(DataIntegrityViolationException e)
        {
            System.out.println("non salvato");
        }

        assertTrue(participantRepository.count() == 1, "non doveva salvare");
        // assertThrows(DataIntegrityViolationException.class, () -> {
        //     participantRepository.saveAndFlush(p2);
        // });
    }
}
