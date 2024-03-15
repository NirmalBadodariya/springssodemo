# Spring Boot SSO Integration Sample

This project demonstrates the integration of multiple Single Sign-On (SSO) authentication mechanisms using Spring Boot(3.2.3) and Spring Security(6.2.2). It includes examples for Google, GitHub, Facebook, Azure Active Directory (AD), LDAP, and SAML.

## Features

- OAuth2 login with Google, GitHub, and Facebook.
- OpenID Connect (OIDC) login with Azure AD.
- LDAP authentication with an in-memory LDAP server.
- SAML 2.0 authentication.

## Prerequisites

- JDK 11 or later.
- Maven 3.2+.
- An IDE of your choice (IntelliJ IDEA, Eclipse, etc.).

## Setup and Configuration

### OAuth2 and OIDC Providers

To configure OAuth2 and OIDC providers (Google, GitHub, Facebook, Azure AD), follow these steps:

1. Register your application with each provider to obtain client IDs and client secrets.
2. Update `src/main/resources/application.yml` (or `application.properties`) with your OAuth2 and OIDC configuration details, including client IDs, client secrets, and redirect URIs.

### LDAP Configuration

The LDAP configuration uses an embedded LDAP server populated from an LDIF file located at `src/main/resources/users.ldif`.

### SAML Configuration

SAML configuration requires setting up an identity provider (IdP). For testing purposes, you can use a service like [SSO Circle](http://www.ssocircle.com/) or set up your own IdP with tools like [Keycloak](https://www.keycloak.org/).

Update the SAML configuration in `src/main/resources/application.yml` with details from your IdP, including entity IDs and key store information.

## Running the Application

To run the application, execute:

