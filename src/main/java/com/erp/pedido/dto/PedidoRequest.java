package com.erp.pedido.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class PedidoRequest {
    @Valid
    @NotNull 
    private ClienteRequest cliente;

    @NotEmpty 
    @Valid
    private List<@NotNull ItemPedidoRequest> itens;
}
