package com.projects.autoforexbackend.userapp.controller;

import com.projects.autoforexbackend.registration.dto.RegistrationRequest;
import com.projects.autoforexbackend.registration.service.RegistrationService;
import com.projects.autoforexbackend.userapp.model.UserApp;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/registrations")
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

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("user", new UserApp());
        return mav;
    }
}
