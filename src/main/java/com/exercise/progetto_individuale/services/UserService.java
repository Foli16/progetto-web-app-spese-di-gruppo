package com.exercise.progetto_individuale.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.exercise.progetto_individuale.dtos.authentication.LoginDto;
import com.exercise.progetto_individuale.dtos.authentication.RegisterDto;
import com.exercise.progetto_individuale.dtos.authentication.UserDto;
import com.exercise.progetto_individuale.entities.User;
import com.exercise.progetto_individuale.exceptions.InvalidCredentials;
import com.exercise.progetto_individuale.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder encoder;

    public String register(RegisterDto registerDto)
    {
        if(!registerDto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"))
            throw new InvalidCredentials("Password not valid");

        User user = new User();
        String hash = encoder.encode(registerDto.getPassword());
        user.setPassword(hash);
        user.setUsername(registerDto.getUsername());


        System.out.println(hash);
        //genero un token in automatico
        user.setToken(UUID.randomUUID().toString());

        repo.save(user);

        return user.getToken();
    }

    public String login(LoginDto dto)
    {
        Optional<User> op = repo.findByUsername(dto.getUsername());
        if(op.isEmpty())
            throw new InvalidCredentials("Invalid username");

        if(!encoder.matches(dto.getPassword(), op.get().getPassword()))
            throw new InvalidCredentials("Invalid password");

        return op.get().getToken();
    }

    public User findUserByToken(String token)
    {
        Optional<User> op = repo.findByToken(token);

        if(op.isEmpty())
            throw new InvalidCredentials("Invalid token");

        return op.get();
    }

    public UserDto convertToUserDto(User u) {
        UserDto dto = new UserDto();
        dto.setUsername(u.getUsername());
        return dto;
    }

    public void changePassword(String token, RegisterDto dto) {
        User u = findUserByToken(token);

        if(!dto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"))
            throw new InvalidCredentials("Password not valid");
        String hash = encoder.encode(dto.getPassword());
        u.setPassword(hash);
        repo.save(u);
    }

    public void changeUsername(String token, RegisterDto dto) {
        User u = findUserByToken(token);
        u.setUsername(dto.getUsername());
        repo.save(u);

    }

    public void deleteUser (String token) {
        User u = findUserByToken(token);
        repo.delete(u);
    }
}
