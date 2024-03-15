package com.ssowithspring.springssodemo.config;

import com.ssowithspring.springssodemo.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;

    // LDAP Config, Change as per your LDAP server
    // Here We've Used In memory LDAP DB
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setDefaultAuthority("USER"); // Set default role

        auth.ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org")
                .and()
                .passwordCompare()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .passwordAttribute("userPassword")
                .and()
                .authoritiesMapper(authorityMapper); // Directly use the configured mapper
    }




    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                String redirectUrl = "/default";
                if (authentication.getPrincipal() instanceof OidcUser) {
                    redirectUrl = "/post-login-url"; // OIDC-specific landing page
                }
                // Check if the authentication is OAuth2 (e.g., Facebook, GitHub)
                else if (authentication.getPrincipal() instanceof OAuth2User) {
                    redirectUrl = "/post-login-url"; // General OAuth2 landing page
                }
                // Fallback for other types, such as LDAP
                else {
                    // Check user roles and determine the redirect URL
                    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                    if (authorities.contains(new SimpleGrantedAuthority("ROLE_DEVELOPERS"))) {
                        redirectUrl = "/developers";
                    } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINS"))) {
                        redirectUrl = "/admins";
                    }
                    // Other roles can be handled here
                }
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            }
        };
    }


    @Bean
    public SecurityFilterChain commonFilterChain(HttpSecurity http) throws Exception {
        http
                // Logout configuration
                .logout(logout -> logout
                        .logoutUrl("/perform-logout") // Specifies the URL to trigger logout
                        .logoutSuccessUrl("/") // Specifies where to go after logout is successful
                        .deleteCookies("JSESSIONID") // Deletes the JSESSIONID cookie
                        .invalidateHttpSession(true) // Invalidates the session
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/ldap-login/**", "/oauth2/authorization/**", "/login", "/login?logout").permitAll() // Allow access to login and logout pages without authentication
                            .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/ldap-login")
                        .loginProcessingUrl("/perform-ldap-login")
                        .successHandler(successHandler())
                        .permitAll())
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/**")
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .oidcUserService(this.userService))
                .successHandler(successHandler())); // Handle OAuth2 login success

        return http.build();
    }

}
