package com.erp.pedido.dto;

import lombok.Data;

@Data 
public class ErroResponse {
    private String codigo;
    private String mensagem;
    private String traceId;
}
