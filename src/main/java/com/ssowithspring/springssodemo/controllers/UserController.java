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
        return "index.html";
    }

    @GetMapping("/post-login-url")
    public String postLogin() {
        return "post-login.html";
    }

    @GetMapping("/ldap-login")
    public String ldapLoginPage() {
        return "ldap-login.html";
    }

    @GetMapping("/saml/demo")
    public String demoHome() {
        return "saml-login.html";
    }
}
