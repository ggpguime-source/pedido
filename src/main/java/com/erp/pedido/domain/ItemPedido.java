package com.erp.pedido.domain;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ItemPedido
 */
@Getter
@Setter
@Builder 
public class ItemPedido {
    private Long id;
    private Produto produto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal descontoPercentual;
    private BigDecimal desconto;
    private BigDecimal valorTotal;
}