package com.ssowithspring.springssodemo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String home() {
        return "index"; // This should display the login option
    }

    @GetMapping("/post-login-url")
    public String postLogin() {
        return "post-login";
    }

    @GetMapping("/ldap-login")
    public String ldapLoginPage() {
        return "ldap-login"; // returns the ldap-login.html Thymeleaf template
    }
}
