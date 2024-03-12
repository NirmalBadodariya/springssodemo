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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configure LDAP authentication
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
//                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org")
                .and()
                .passwordCompare()
                // Specify that no password encoding is currently being used.
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .passwordAttribute("userPassword");
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,AuthenticationManagerBuilder auth) throws Exception {
        AuthenticationManager authManager = auth.getObject();
        http    .csrf(csrf->csrf.disable())
                .authorizeHttpRequests((authz) -> authz

                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/ldap-login") // custom LDAP login page
                        .loginProcessingUrl("/perform-login") // custom URL to submit the username and password to
                        .defaultSuccessUrl("/post-login-url", true)
                        .permitAll()
                )
                .authenticationManager(authManager)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .oidcUserService(this.userService))
                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("/post-login-url")));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new SimpleUrlAuthenticationSuccessHandler("/post-login-url");
    }
}
