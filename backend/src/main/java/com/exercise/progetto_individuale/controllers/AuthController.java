package com.exercise.progetto_individuale.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.progetto_individuale.dtos.authentication.LoginDto;
import com.exercise.progetto_individuale.dtos.authentication.RegisterDto;
import com.exercise.progetto_individuale.dtos.authentication.UserDto;
import com.exercise.progetto_individuale.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/auth")
public class AuthController
{
    @Autowired
    private UserService userService;

    @PostMapping("register")
    public void register(@RequestBody RegisterDto dto, HttpServletResponse response)
    {
        String tokenUtente = userService.register(dto);

        Cookie cookie = new Cookie("token", tokenUtente);
        cookie.setMaxAge(3600);
        cookie.setPath("/api");
        response.addCookie(cookie);
    }

    @PostMapping("login")
    public void login(@RequestBody LoginDto dto, HttpServletResponse response)
    {
        String tokenUtente = userService.login(dto);

        Cookie cookie = new Cookie("token", tokenUtente);
        cookie.setMaxAge(3600);
        cookie.setPath("/api");
        response.addCookie(cookie);
    }

    @GetMapping("/userinformation")
    public UserDto getUserInfo(@CookieValue(required = false) String token)
    {
        return userService.getUserInfo(token);
    }
}
