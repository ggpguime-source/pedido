package com.erp.pedido.domain;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Builder 
public class Produto {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal precoAtual;
    private BigDecimal descontoPercentual;
}
