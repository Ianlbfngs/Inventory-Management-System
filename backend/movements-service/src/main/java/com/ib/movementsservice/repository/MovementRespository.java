package com.ib.movementsservice.repository;

import com.ib.movementsservice.entity.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRespository extends JpaRepository<Movement,Integer> {
}
