package com.erp.pedido.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.erp.pedido.enums.StatusPedido;

import lombok.Data;

@Data 
public class PedidoResumoResponse {
    private Long id;
    private ClienteResponse cliente;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private Instant dataCriacao;
}
