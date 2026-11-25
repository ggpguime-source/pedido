package com.erp.pedido.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data 
public class ClienteRequest {
    @NotNull 
    @Positive
    private Long id;
}
