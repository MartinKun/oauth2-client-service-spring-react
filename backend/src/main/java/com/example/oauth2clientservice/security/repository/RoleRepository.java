package com.example.oauth2clientservice.security.repository;

import com.example.oauth2clientservice.security.model.entity.Role;
import com.example.oauth2clientservice.security.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleEnum name);
}
