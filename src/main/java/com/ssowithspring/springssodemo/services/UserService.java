package com.ssowithspring.springssodemo.services;

import com.ssowithspring.springssodemo.entities.UserEntity;
import com.ssowithspring.springssodemo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.stereotype.Service;


@Service
public class UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    private UserRepository userRepository;


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        // Determine the provider
        String provider = userRequest.getClientRegistration().getRegistrationId();


        // Delegate to the default implementation for loading the user
        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);

        // Extract user details from OAuth2User
        String email = oidcUser.getAttribute("email");
        String name = oidcUser.getAttribute("name");
        // Additional attributes can be extracted here


        // Check if user exists, update or create new one
        UserEntity applicationUser = userRepository.findByEmail(email);
        if (applicationUser == null) {
            applicationUser = new UserEntity();
            applicationUser.setEmail(email);
        }
        applicationUser.setName(name);
        if ("google".equals(provider)) {
            applicationUser.setProvider("Google");
        } else if ("azure".equals(provider)) {
            applicationUser.setProvider("Azure");
            // You may need to adjust the attributes you retrieve for Azure AD users, for example:
            // name = oidcUser.getAttribute("preferred_username");
        }

        userRepository.save(applicationUser);


        return oidcUser;
    }

}
