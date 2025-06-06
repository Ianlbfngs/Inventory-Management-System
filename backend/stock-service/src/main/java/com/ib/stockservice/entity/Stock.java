package com.ib.stockservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "stock")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class Stock {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    int id;

    @Column(name = "storage_id")
    int storageId;

    @NotNull
    @Column(name = "batch_code")
    String batchCode;

    @Column(name = "available_stock")
    Integer availableStock;
    @Column(name = "pending_stock")
    Integer pendingStock;

}
