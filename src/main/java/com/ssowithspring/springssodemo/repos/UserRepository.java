package com.ssowithspring.springssodemo.repos;

import com.ssowithspring.springssodemo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}