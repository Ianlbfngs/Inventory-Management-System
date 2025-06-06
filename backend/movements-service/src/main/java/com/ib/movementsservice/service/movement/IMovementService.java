package com.ib.movementsservice.service.movement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ib.movementsservice.dto.StockDTO;
import com.ib.movementsservice.entity.Movement;
import com.ib.movementsservice.response.Response;
import com.ib.movementsservice.response.Statuses;

import java.util.Optional;

public interface IMovementService {
    Object obtainAll();

    Optional<Movement> obtainSpecificMovement(int id);

    Response<Statuses.CreateMovementStatus, Movement> createMovement(Movement movement, String jwtToken) throws JsonProcessingException;

    Response<Statuses.CreateMovementStatus, Movement> createMovement(Movement movement, StockDTO stockDTO, String jwtToken) throws JsonProcessingException;

    Response<Statuses.HardDeleteMovementStatus, Movement> hardDeleteProduct(int id);

    Response<Statuses.MovementReceptionStatus, Movement> movementReception(int movementId, boolean receptionStatus, String jwtToken);

}
