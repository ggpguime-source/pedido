package com.erp.pedido.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data 
public class ItemPedidoRequest {
    @Valid
    @NotNull 
    private ProdutoRequest produto;
    @NotNull 
    @Positive
    private Integer quantidade;
}
