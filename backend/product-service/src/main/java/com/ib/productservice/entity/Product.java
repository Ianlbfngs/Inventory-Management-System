package com.ib.productservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class Product implements IEntity{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    int id;

    @NotNull
    @Size(min = 13,max = 13, message = "Size must be 13")
    String SKU;

    @NotNull
    @Size(min = 1,max = 45, message = "Size must be between 1 and 45")
    String name;

    @ManyToOne
    @JoinColumn(name ="category_id",nullable = false)
    Category category;

    double weight;
    boolean active = false;
}
