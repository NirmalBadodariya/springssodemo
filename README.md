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

### 1. Project Overview

This project aims to demonstrate the implementation of Single Sign-On (SSO) functionality using Spring Boot. It showcases integration with various authentication providers such as OAuth2 (Google, GitHub, Facebook, Azure AD) and LDAP for user authentication.

### 2. Configuration

The project's configuration is managed primarily through the `application.properties` (or `application.yaml`) file. Here, you can set up the datasource configuration for connecting to the MySQL database, configure Hibernate properties for entity management, define the server port, and specify OAuth2 client registration details for each provider.

### 3. Controllers

### UserController:
This controller defines mappings for different authentication endpoints and login pages. It includes endpoints for displaying the home page, post-login page, LDAP login page, and SAML login page.

### 4. Entities

### UserEntity:
This entity represents a user in the system, containing basic attributes such as id, name, email, and provider. It serves as the model for user data storage in the database.

### 5. Services

#### CustomOAuth2UserService:
This service class extends `DefaultOAuth2UserService` to customize user details retrieval from OAuth2 providers. It processes OAuth2 user information and saves or updates user details in the database based on the OAuth2 provider.

#### UserService:
This service implements `OAuth2UserService` to handle loading user details from the OAuth2 provider, specifically for Google authentication. It retrieves user attributes such as email and name and saves or updates user information in the database.

### 6. Security Configuration

#### SecurityConfig:
This configuration class manages security settings for the application. It includes LDAP authentication configuration, OAuth2 login configuration for Google, GitHub, and Facebook, form-based login configuration, CSRF protection, and SAML configuration for Okta integration.

### 7. Database Configuration <a name="database-configuration"></a>

The project is configured to use MySQL as the database. The `application.properties` file includes properties for setting up the MySQL datasource, including the URL, username, password, and Hibernate properties for schema generation and management.

### 8. OAuth2 Provider Configuration

The `application.properties` file includes configurations for OAuth2 client registration with Google, GitHub, and Facebook. These configurations include client IDs, client secrets, and scopes required for authentication and user authorization.

### 9. LDAP Configuration

LDAP authentication is configured for the application using an embedded LDAP server for testing purposes. The configuration includes base DN, LDIF file location for defining user entries, port number, and credentials for the embedded LDAP server.

### 10. SAML Configuration

SAML configuration is set up for integration with Okta as the relying party. The configuration includes metadata URI for Okta, signing credentials for SAML assertions, and single logout settings for handling logout requests.

---
## CURLs For the SSO Applicaion

#### 1. **Home Page (GET "/")**:
```bash
curl -X GET http://localhost:8080/
```

#### 2. **Post-Login Page (GET "/post-login-url")**:
```bash
curl -X GET http://localhost:8080/post-login-url
```

#### 3. **LDAP Login Page (GET "/ldap-login")**:
```bash
curl -X GET http://localhost:8080/ldap-login
```

#### 4. **SAML Login Page (GET "/saml/demo")**:
```bash
curl -X GET http://localhost:8080/saml/demo
```

You can run these `curl` commands in your terminal to make requests to the corresponding endpoints of your Spring Boot application. Make sure to replace `http://localhost:8080/` with the appropriate base URL of your application if it's different.
