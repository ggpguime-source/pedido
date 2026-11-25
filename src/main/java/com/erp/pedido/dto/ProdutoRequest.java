package com.erp.pedido.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data 
public class ProdutoRequest {
    @NotNull
    @Positive
    private Long id;
}
