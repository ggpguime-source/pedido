package com.erp.pedido.dto;

import com.erp.pedido.enums.StatusPedido;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class StatusPedidoRequest {
    @NotNull 
    private StatusPedido status;
}
