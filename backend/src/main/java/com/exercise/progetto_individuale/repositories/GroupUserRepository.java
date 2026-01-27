package com.exercise.progetto_individuale.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.progetto_individuale.entities.GroupUser;
import com.exercise.progetto_individuale.entities.User;

public interface GroupUserRepository extends JpaRepository<GroupUser, UUID>
{
    Optional<GroupUser> findByUserAndParticipantId(User u, UUID partId);
}
