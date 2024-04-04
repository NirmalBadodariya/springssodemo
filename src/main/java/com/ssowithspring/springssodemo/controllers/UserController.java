package com.ssowithspring.springssodemo.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UserController {

    @GetMapping("/")
    public String home() {
        return "index"; // This should display the login option
    }

    @GetMapping("/post-login-url")
    public String postLogin(HttpServletRequest request,Model model) {
        Principal principal = request.getUserPrincipal();
        if(((OAuth2AuthenticationToken) principal).getAuthorizedClientRegistrationId().equals("google")){
            model.addAttribute("name", ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("name"));
        }else if(((OAuth2AuthenticationToken) principal).getAuthorizedClientRegistrationId().equals("github")){
            model.addAttribute("name", ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("login"));
        }else  if (((OAuth2AuthenticationToken) principal).getAuthorizedClientRegistrationId().equals("facebook")){
            model.addAttribute("name", ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("name"));
        }
        return "post-login";
    }

    @GetMapping("/ldap-login")
    public String ldapLoginPage() {
        return "ldap-login";
    }

    @GetMapping("/developers")
    public String devLogin() {
        return "developers";
    }
    @GetMapping("/admins")
    public String adminLogin() {
        return "admin";
    }

    @GetMapping("/saml/demo")
    public String demoHome() {
        return "saml-login.html";
    }
}
