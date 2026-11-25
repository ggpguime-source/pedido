package com.erp.pedido.dto;

import com.erp.pedido.enums.StatusPedido;

import lombok.Data;

@Data 
public class FaturamentoResponse {
    private Long pedidoId;
    private StatusPedido status;
}
