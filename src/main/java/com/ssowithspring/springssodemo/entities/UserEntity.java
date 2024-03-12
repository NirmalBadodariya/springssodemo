package com.ssowithspring.springssodemo.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sso_users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String provider;
}
