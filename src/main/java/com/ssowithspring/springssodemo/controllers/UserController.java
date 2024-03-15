package com.ssowithspring.springssodemo.controllers;


import org.springframework.stereotype.Controller;
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
        return "ldap-login";
    }

    @GetMapping("/developers")
    public String devLogin(){
        return "developers";
    }
    @GetMapping("/admins")
    public String adminLogin(){
        return "admin";
    }
}
