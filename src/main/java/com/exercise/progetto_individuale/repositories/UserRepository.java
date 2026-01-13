package com.exercise.progetto_individuale.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exercise.progetto_individuale.entities.User;

public interface UserRepository extends JpaRepository<User, UUID>
{

	Optional<User> findByUsername(String username);

	Optional<User> findByToken(String token);
    
}
