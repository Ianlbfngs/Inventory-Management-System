package com.ib.productservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "batches")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class Batch implements IEntity{

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    int id;

    @NotNull
    @Column(name = "expiration_date")
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "America/Argentina/Buenos_Aires")
    LocalDate expirationDate;

    int amount;

    @NotNull
    @Size(min = 1,max = 45)
    @Column(name = "batch_code")
    String batchCode;

    @ManyToOne
    @JoinColumn(name ="product_id",nullable = false)
    Product product;


    @NotNull
    @Column(name = "arrival_date")
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "America/Argentina/Buenos_Aires")
    LocalDate arrivalDate;


}
