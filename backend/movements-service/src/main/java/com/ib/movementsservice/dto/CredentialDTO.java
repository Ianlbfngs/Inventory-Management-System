package com.ib.movementsservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CredentialDTO {
    @NotNull
    int id;
    @NotNull
    @Size(min = 1,max = 45)
    String username;

}
