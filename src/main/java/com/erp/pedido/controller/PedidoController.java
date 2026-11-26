package com.erp.pedido.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.pedido.dto.PageResponse;
import com.erp.pedido.dto.PedidoRequest;
import com.erp.pedido.dto.PedidoResponse;
import com.erp.pedido.dto.PedidoResumoResponse;
import com.erp.pedido.dto.StatusPedidoRequest;
import com.erp.pedido.mapper.DomainMapper;
import com.erp.pedido.service.PedidoService;

import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




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

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> getMethodName(@PathVariable Long id) {
        var pedidoDomain = pedidoService.consultarPedido(id);
        var pedidoResponse = mapper.toPedidoResponse(pedidoDomain);
        return ResponseEntity.ok().body(pedidoResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PedidoResumoResponse>> listarPedidos(
                @PageableDefault(page = 0, size = 20, sort = {"dataCriacao", "id"}, direction = Sort.Direction.DESC) Pageable pageable, 
                @RequestParam(required = false) Long clienteId) {
        var pedidosDomain = pedidoService.listarPedidos(clienteId, pageable);
        var pedidosResponse = pedidosDomain.map(pedido -> mapper.toPedidoResumoResponse(pedido));
        var pageResponse = mapper.toPageResponse(pedidosResponse);
        return ResponseEntity.ok(pageResponse);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(@PathVariable Long id,@Valid @RequestBody StatusPedidoRequest status){
        pedidoService.alterarStatus(id, status.getStatus());
        return ResponseEntity.noContent().build();
    }

}