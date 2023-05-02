package com.example.restapi.repositories;

import com.example.restapi.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
}
