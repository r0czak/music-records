package com.ws.musicrecords.repository;

import com.ws.musicrecords.entities.RoleEntity;
import com.ws.musicrecords.entities.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName(ERole name);
}
