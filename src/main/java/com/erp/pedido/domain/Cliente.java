package com.erp.pedido.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Builder 
public class Cliente {
    private Long id;
    private String nome;
}
