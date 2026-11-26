package com.erp.pedido.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.erp.pedido.domain.ItemPedido;
import com.erp.pedido.domain.Pedido;
import com.erp.pedido.entity.ClienteEntity;
import com.erp.pedido.entity.ProdutoEntity;
import com.erp.pedido.enums.StatusPedido;
import com.erp.pedido.mapper.RepositoryMapper;
import com.erp.pedido.repository.ClienteRepository;
import com.erp.pedido.repository.PedidoRepository;
import com.erp.pedido.repository.ProdutoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final RepositoryMapper mapper;

    @Transactional 
    public Pedido criarPedido(Pedido pedidoRequest, String requestId){
        var cliente = consultarCliente(pedidoRequest.getCliente().getId());
        var produtos = consultarProdutos(pedidoRequest.getItens());

        calcularPedido(pedidoRequest, produtos);

        pedidoRequest.setStatus(StatusPedido.CRIADO);
        pedidoRequest.setDataCriacao(Instant.now());

        var pedido = mapper.toPedidoEntity( pedidoRequest, cliente, produtos);

        return mapper.toPedidoDomain(pedidoRepository.save(pedido));
    }

    private ClienteEntity consultarCliente(Long clienteId){
        return clienteRepository.findById(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + clienteId));
    }

    private Map<Long, ProdutoEntity> consultarProdutos(List<ItemPedido> itens){
        var idsProduto = new HashSet<Long>();
        for (ItemPedido item : itens) {
            idsProduto.add(item.getProduto().getId());
        }

        var produtosEntity = produtoRepository.findAllById(idsProduto);

        if (produtosEntity.size() != idsProduto.size()) {
            throw new IllegalArgumentException("Um ou mais produtos não foram encontrados");
        }

        Map<Long, ProdutoEntity> produtos = new HashMap<>();

        for (ProdutoEntity produto : produtosEntity) {
            produtos.put(produto.getId(), produto);
        }

        return produtos;
    }

    private void calcularItem(ItemPedido item, ProdutoEntity produto){
        var precoUnitario = produto.getPrecoAtual();
        var subtotal = precoUnitario.multiply(BigDecimal.valueOf(item.getQuantidade()));
        var percentutal = produto.getDescontoPercentual();
        var desconto = subtotal.multiply(percentutal.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
        var valorTotal = subtotal.subtract(desconto);

        item.setPrecoUnitario(precoUnitario);
        item.setDescontoPercentual(percentutal);
        item.setDesconto(desconto);
        item.setValorTotal(valorTotal);
    }

    private void calcularPedido(Pedido pedido, Map<Long, ProdutoEntity> produtos){
        var subtotal = BigDecimal.ZERO;
        var descontoTotal = BigDecimal.ZERO;
        var valorTotal = BigDecimal.ZERO;

        for (ItemPedido item : pedido.getItens()) {

            var produto = produtos.get(item.getProduto().getId());

            calcularItem(item, produto);
            subtotal = subtotal.add(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())));
            descontoTotal = descontoTotal.add(item.getDesconto());
            valorTotal = valorTotal.add(item.getValorTotal());
        }

        pedido.setDescontoTotal(descontoTotal);
        pedido.setSubtotal(subtotal);
        pedido.setValorTotal(valorTotal);
    }

}
