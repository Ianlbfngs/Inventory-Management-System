package com.ib.movementsservice.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class StockDTO {
    int id;
    int storageId;
    @NotNull
    String batchCode;

    Integer availableStock;
    Integer pendingStock;

}
