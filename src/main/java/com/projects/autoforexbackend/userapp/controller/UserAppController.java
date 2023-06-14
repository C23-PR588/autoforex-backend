package com.projects.autoforexbackend.userapp.controller;

import com.projects.autoforexbackend.registration.dto.RegistrationRequest;
import com.projects.autoforexbackend.registration.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registrations")
@AllArgsConstructor
public class UserAppController {

    private RegistrationService registrationService;

    @PostMapping
    public String regis(@RequestBody RegistrationRequest request) throws IllegalAccessException {
        return registrationService.regis(request);
    }
}
