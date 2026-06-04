package com.example.Mini_Banking.repository;

import com.example.Mini_Banking.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findUserByEmail(String email);
}
