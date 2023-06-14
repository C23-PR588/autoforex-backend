package com.projects.autoforexbackend.userapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping()
public class LoginController {

    @GetMapping("/logout")
    public String logoutDo(HttpServletRequest request) throws ServletException {
        request.logout();
        return "Logout Sucsessfull";
    }

    @GetMapping("/home")
    public String login() {
        return "Login Berhasil";
    }
}
