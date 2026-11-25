package com.erp.pedido.dto;

import lombok.Data;

@Data 
public class ProdutoResponse {
    private Long id;
    private String nome;
    private String descricao;
}
