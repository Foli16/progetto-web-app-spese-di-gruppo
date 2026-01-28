package com.exercise.progetto_individuale.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.progetto_individuale.entities.Participant;
import com.exercise.progetto_individuale.entities.SpendingGroup;

public interface ParticipantRepository extends JpaRepository<Participant, UUID>
{
    Optional<Participant> findParticipantByIdAndSpendingGroup(UUID id, SpendingGroup sg);

    List<Participant> findAllByIdInAndSpendingGroup(Iterable<UUID> ids, SpendingGroup sg);
}
