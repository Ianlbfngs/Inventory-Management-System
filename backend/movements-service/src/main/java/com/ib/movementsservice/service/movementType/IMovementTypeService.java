package com.ib.movementsservice.service.movementType;

import com.ib.movementsservice.entity.MovementType;
import com.ib.movementsservice.response.Response;
import com.ib.movementsservice.response.Statuses;

import java.util.Optional;

public interface IMovementTypeService {
    Object obtainAll();

    Optional<MovementType> obtainSpecificMovementType(int id);

    MovementType createMovementType(MovementType type);

    Response<Statuses.UpdateMovementTypeStatus, MovementType> updateMovementType(int id, MovementType type);

    Response<Statuses.HardDeleteMovementTypeStatus, MovementType> hardDeleteMovementType(int id);
}
