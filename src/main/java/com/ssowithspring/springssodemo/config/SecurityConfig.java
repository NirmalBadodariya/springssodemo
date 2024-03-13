package com.ssowithspring.springssodemo.config;

import com.ssowithspring.springssodemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;

    // LDAP Config, Change as per your LDAP server
    // Here We've Used In memory LDAP DB
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org")
                .and()
                .passwordCompare()
                //No Encoder,update as you fancy
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .passwordAttribute("userPassword");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/","/ldap-login", "/perform-login", "/oauth2/authorization/**").permitAll() // Permit these without authentication
                        .anyRequest().authenticated()) // Other requests require authentication
                .formLogin(form -> form
                        .loginPage("/ldap-login") // Use the custom login page
                        .loginProcessingUrl("/perform-login") // Custom processing URL
                        .defaultSuccessUrl("/post-login-url",true) // Redirect after successful login
                        .permitAll()) // Allow access to everyone for the login page
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .oidcUserService(this.userService))
                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("/post-login-url"))); // Handle OAuth2 login success
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/post-login-url");
        return successHandler;
    }
}
