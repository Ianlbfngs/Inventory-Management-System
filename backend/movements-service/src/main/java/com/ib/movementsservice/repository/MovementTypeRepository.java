package com.ib.movementsservice.repository;

import com.ib.movementsservice.entity.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementTypeRepository extends JpaRepository<MovementType,Integer> {
}
