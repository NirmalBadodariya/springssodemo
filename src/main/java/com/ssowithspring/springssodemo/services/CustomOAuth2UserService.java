package com.ssowithspring.springssodemo.services;

import com.ssowithspring.springssodemo.entities.UserEntity;
import com.ssowithspring.springssodemo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        processOAuth2User(registrationId, oauth2User);

        // You can adjust the authorities as necessary
        return oauth2User;
    }


    private void processOAuth2User(String registrationId, OAuth2User oauth2User) {
        if ("github".equals(registrationId)) {
            String githubUsername = oauth2User.getAttribute("login");
            String email = oauth2User.getAttribute("email");
            // Save or update the GitHub user in your database
            saveOrUpdateUser(githubUsername, email, registrationId);
        } else if ("facebook".equals(registrationId)) {
            String facebookUsername = oauth2User.getAttribute("name");
            String email = oauth2User.getAttribute("email"); // Note: email might be null depending on user's privacy settings
            // Save or update the Facebook user in your database
            saveOrUpdateUser(facebookUsername, email, registrationId);
        } else if ("google".equals(registrationId)) {
            // Similarly handle for Google, if different from others
            String googleUsername = oauth2User.getAttribute("name");
            String email = oauth2User.getAttribute("email");
            saveOrUpdateUser(googleUsername, email, registrationId);
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
    }

    private void saveOrUpdateUser(String username, String email, String provider) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            user = new UserEntity();
            user.setEmail(email);
        }
        user.setName(username);
        user.setProvider(provider);
        userRepository.save(user);
    }
}