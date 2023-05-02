package com.example.restapi.security.repositories;

import com.example.restapi.security.domain.UserDetailsImpl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetailsImpl, String> {
}
