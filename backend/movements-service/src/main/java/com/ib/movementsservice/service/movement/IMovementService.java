package com.ib.movementsservice.service.movement;

import com.ib.movementsservice.entity.Movement;
import com.ib.movementsservice.response.Response;
import com.ib.movementsservice.response.Statuses;

import java.util.Optional;

public interface IMovementService {
    Object obtainAll();

    Optional<Movement> obtainSpecificMovement(int id);

    Response<Statuses.CreateMovementStatus, Movement> createMovement(Movement movement, String jwtToken);

    Response<Statuses.HardDeleteMovementStatus, Movement> hardDeleteProduct(int id);
}
