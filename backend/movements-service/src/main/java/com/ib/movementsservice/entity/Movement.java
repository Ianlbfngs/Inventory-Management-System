package com.ib.movementsservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ib.movementsservice.dto.StockDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;

@Entity
@Table(name = "movements")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class Movement implements IEntity{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    int id;

    @Column(name="origin_stock_id")
    int originStockId =-1;
    @Column(name="target_stock_id")
    int targetStockId = -1;

    @Column(name = "stock_amount")
    int stockAmount = -1;

    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "America/Argentina/Buenos_Aires")
    @Column(name = "issue_date")
    @NotNull
    LocalDate issueDate = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));

    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "America/Argentina/Buenos_Aires")
    @Column(name = "receipt_date")
    LocalDate receiptDate = null;

    @ManyToOne
    @JoinColumn(name ="type_id",nullable = false)
    MovementType movementType;

    @Column(name = "creator_username")
    @NotNull
    String createdByUser;

    int status = 0; //0 == pending, 1 == received, -1 == cancelled


}
