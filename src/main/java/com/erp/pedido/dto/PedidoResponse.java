package com.erp.pedido.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.erp.pedido.enums.StatusPedido;

import lombok.Data;

@Data 
public class PedidoResponse {
    private Long id;
    private ClienteResponse cliente;
    private BigDecimal subtotal;
    private BigDecimal descontoTotal;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private Instant dataCriacao;
    private List<ItemPedidoResponse> itens;
}
