package com.erp.pedido.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.erp.pedido.enums.StatusPedido;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Pedido
 */
@Getter
@Setter 
@Builder 
public class Pedido {
    private Long id;
    private Cliente cliente;
    private BigDecimal subtotal;
    private BigDecimal descontoTotal;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private Instant dataCriacao;
    
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();
}