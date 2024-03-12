//package com.ssowithspring.springssodemo.controllers;
//
//import com.ssowithspring.springssodemo.services.UserService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import java.io.IOException;
//
//public class CustomLdapAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    private UserService userService; // Your user service to interact with the database
//
//    public CustomLdapAuthenticationSuccessHandler(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        // Extract user details from authentication object
//        User ldapUser = (User) authentication.getPrincipal();
//
//        // Extract more details if required from the authentication object
//        // ...
//
//        // Use your service to save the user details to your database
//        userService.saveUserDetails(ldapUser);
//
//        // Redirect to the default success URL
//        response.sendRedirect("/post-login-url");
//    }
//}
