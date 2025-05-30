package com.ib.productservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class Category implements IEntity{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    int id;

    @NotNull
    @Size(min = 1,max = 45)
    String description;
}
