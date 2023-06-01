package com.projects.autoforexbackend.registration.service;

import com.projects.autoforexbackend.registration.config.EmailValidator;
import com.projects.autoforexbackend.registration.dto.RegistrationRequest;
import com.projects.autoforexbackend.userapp.model.UserApp;
import com.projects.autoforexbackend.userapp.model.UserRole;
import com.projects.autoforexbackend.userapp.service.UserAppService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;
    private UserAppService userAppService;

    public String regis(RegistrationRequest request) throws IllegalAccessException {
        boolean validEmail = emailValidator.test(request.getEmail());
        if (!validEmail) {
            throw new IllegalArgumentException("tidak valid");
        }
        return userAppService.signUp(
                new UserApp(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.ADMIN
                )
        );
    }
}
