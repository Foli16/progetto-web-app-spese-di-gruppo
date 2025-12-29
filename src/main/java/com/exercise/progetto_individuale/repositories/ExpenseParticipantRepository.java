package com.exercise.progetto_individuale.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.progetto_individuale.entities.ExpenseParticipant;

public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipant, UUID>
{
    
}
