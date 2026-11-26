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
import com.erp.pedido.entity.PedidoEntity;
import com.erp.pedido.entity.ProdutoEntity;
import com.erp.pedido.enums.StatusPedido;
import com.erp.pedido.enums.StatusRequest;
import com.erp.pedido.mapper.RepositoryMapper;
import com.erp.pedido.repository.ClienteRepository;
import com.erp.pedido.repository.PedidoRepository;
import com.erp.pedido.repository.ProdutoRepository;
import com.erp.pedido.repository.RequestRepository;
import com.erp.pedido.util.RequestHashUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final RequestRepository requestRepository;
    private final RepositoryMapper mapper;

    @Transactional 
    public Pedido criarPedido(Pedido pedidoRequest, String requestId){

        var requestHash = RequestHashUtil.gerarHash(pedidoRequest);

        var requestRegistrada = requestRepository.registrarRequest(requestId, requestHash);

        if (requestRegistrada == 0) {
            return consultarRequestExistente(requestId, requestHash);
        }

        var cliente = consultarCliente(pedidoRequest.getCliente().getId());
        var produtos = consultarProdutos(pedidoRequest.getItens());

        calcularPedido(pedidoRequest, produtos);

        pedidoRequest.setStatus(StatusPedido.CRIADO);
        pedidoRequest.setDataCriacao(Instant.now());

        var pedido = mapper.toPedidoEntity( pedidoRequest, cliente, produtos);

        var pedidoSalvo = pedidoRepository.save(pedido);

        concluirRequest(requestId, pedidoSalvo);

        return mapper.toPedidoDomain(pedidoSalvo);
    }

    @Transactional(readOnly = true)
    public Pedido consultarPedido(Long id) {

        var pedidoEntity = pedidoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
        var pedidoDomain = mapper.toPedidoDomain(pedidoEntity);

        return pedidoDomain;

    }

    @Transactional(readOnly = true)
    public Page<Pedido> listarPedidos(Long clienteId, Pageable pageable) {
        Page<PedidoEntity> pedidos;

        if (clienteId != null) {
            pedidos = pedidoRepository.findByClienteId(clienteId, pageable);
        } else {
            pedidos = pedidoRepository.findAll(pageable);
        }
        return pedidos.map(pedido -> mapper.toResumoDomain(pedido));
    }

    private Pedido consultarRequestExistente(String requestId, String requestHash){
        var requestEntity = requestRepository.findById(requestId).orElseThrow(() -> new IllegalStateException("Request não encontrada: " + requestId));
        if (!requestEntity.getRequestHash().equals(requestHash)) {
            throw new IllegalArgumentException("RequestID já utilizado com conteúdo diferente");
        }
        return mapper.toPedidoDomain(requestEntity.getPedido());
    }

    private void concluirRequest(String requestId, PedidoEntity pedido) {
        var requestEntity = requestRepository.findById(requestId).orElseThrow(() -> new IllegalStateException("Request não encontrada: " + requestId));
        requestEntity.setPedido(pedido);
        requestEntity.setStatus(StatusRequest.CONCLUIDO);
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
