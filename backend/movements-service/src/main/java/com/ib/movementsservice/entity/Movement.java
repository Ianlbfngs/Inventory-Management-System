package com.ib.movementsservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @Column(name="origin_storage_id")
    int originStorageId; // id 0 means that the movement comes from an outer source (like a supplier)

    @Column(name="target_storage_id")
    int targetStorageId; // id 0 means that the movement goes to an outer source/not to a storage (like a supermarket)

    @Column(name = "batch_code")
    @Size(min = 1,max = 45)
    @NotNull
    String batchCode;


    int amount;

    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "America/Argentina/Buenos_Aires")
    @Column(name = "issue_date")
    LocalDate issueDate = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));

    @ManyToOne
    @JoinColumn(name ="type_id",nullable = false)
    MovementType movementType;

    @Column(name = "user_id")
    int userId;



}
