package com.ib.storageservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "storage")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class Storage {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    int id;

    @NotNull
    @Size(min = 1,max = 200)
    String name;

    double capacity;

    @NotNull
    @Size(min = 1,max = 150)
    String location;

    boolean active = false;

}
