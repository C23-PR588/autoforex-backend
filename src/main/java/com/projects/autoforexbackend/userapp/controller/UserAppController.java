package com.projects.autoforexbackend.userapp.controller;

import com.projects.autoforexbackend.registration.dto.RegistrationRequest;
import com.projects.autoforexbackend.registration.service.RegistrationService;
import com.projects.autoforexbackend.userapp.service.UserAppService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping()
@AllArgsConstructor
public class UserAppController {

    private RegistrationService registrationService;

    @PostMapping
    public String regis(@RequestBody RegistrationRequest request) throws IllegalAccessException {
        return registrationService.regis(request);
    }

    @GetMapping("/logout")
    public String logoutDo(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.logout();
        return "Logout Sucsessfull";
    }

    @GetMapping("/home")
    public String login() {
        return "Login Berhasil";
    }
}
