package com.erp.pedido.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data 
public class ItemPedidoResponse {
    private ProdutoResponse produto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal descontoPercentual;
    private BigDecimal desconto;
    private BigDecimal valorTotal;
}
