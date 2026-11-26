package com.erp.pedido.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.pedido.dto.PedidoRequest;
import com.erp.pedido.dto.PedidoResponse;
import com.erp.pedido.mapper.DomainMapper;
import com.erp.pedido.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * PedidoController
 */
@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor 
public class PedidoController {
    private final PedidoService pedidoService;
    private final DomainMapper mapper;

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody PedidoRequest request, @RequestHeader("RequestID") String requestId) {
        var requestDomain = mapper.toPedidoDomain(request);
        var pedidoDomainResponse = pedidoService.criarPedido(requestDomain, requestId);
        var pedidoResponse = mapper.toPedidoResponse(pedidoDomainResponse);
        var uri = URI.create("/pedidos/" + pedidoResponse.getId());
        return ResponseEntity.created(uri).body(pedidoResponse) ;
    }
    
}