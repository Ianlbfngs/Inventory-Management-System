package com.ib.userservice.repository;

import com.ib.userservice.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<Credential,Integer> {
    boolean existsByUsername(String username);

    Optional<Credential> findCredentialByUsername(String username);
    Optional<Credential> findCredentialById(int id);
}
