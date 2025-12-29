package com.exercise.progetto_individuale.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.progetto_individuale.entities.GroupUser;

public interface GroupUserRepository extends JpaRepository<GroupUser, UUID>
{
    
}
