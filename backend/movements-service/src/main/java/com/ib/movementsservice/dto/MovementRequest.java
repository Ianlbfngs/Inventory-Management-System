package com.ib.movementsservice.dto;

import com.ib.movementsservice.entity.Movement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovementRequest
{
    StockDTO stockDTO = null;
    Movement movement;
}
