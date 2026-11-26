package com.erp.pedido.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.erp.pedido.domain.Cliente;
import com.erp.pedido.domain.ItemPedido;
import com.erp.pedido.domain.Pedido;
import com.erp.pedido.domain.Produto;
import com.erp.pedido.dto.ClienteRequest;
import com.erp.pedido.dto.ClienteResponse;
import com.erp.pedido.dto.ItemPedidoRequest;
import com.erp.pedido.dto.ItemPedidoResponse;
import com.erp.pedido.dto.PedidoRequest;
import com.erp.pedido.dto.PedidoResponse;
import com.erp.pedido.dto.PedidoResumoResponse;
import com.erp.pedido.dto.ProdutoRequest;
import com.erp.pedido.dto.ProdutoResponse;

/**
 * DomainMapper
 */
@Component 
public class DomainMapper {
    /**
     * Mapeia Cliente
     * @param request
     * @return
     */
    public Cliente toClienteDomain(ClienteRequest request) {
        return Cliente.builder()
                .id(request.getId())
                .build();
    }
    /**
     * Mapeia Produto
     * @param request
     * @return
     */
    public Produto toProdutoDomain(ProdutoRequest request){
        return Produto.builder()
                .id(request.getId())
                .build();
    }
    /**
     * Mapeia Item
     * @param request
     * @return
     */
    public ItemPedido toItemPedidoDomain(ItemPedidoRequest request){
        return ItemPedido.builder()
                .produto(toProdutoDomain(request.getProduto()))
                .quantidade(request.getQuantidade())
                .build();
    }
    /**
     * Mapeia Lista de Itens
     * @param requests
     * @return
     */
    public List<ItemPedido> toListItemPedidoDomain(List<ItemPedidoRequest> requests){
        var listItemPedido = new ArrayList<ItemPedido>();
        for (ItemPedidoRequest item : requests) {
            listItemPedido.add(toItemPedidoDomain(item));
        }
        return listItemPedido;
    }
    /**
     * Mapeia Pedido
     * @param request
     * @return
     */
    public Pedido toPedidoDomain(PedidoRequest request){
        return Pedido.builder()
                .cliente(toClienteDomain(request.getCliente()))
                .itens(toListItemPedidoDomain(request.getItens()))
                .build();
    }
    /**
     * Mapeia Response DTO
     * @param cliente
     * @return
     */
    public ClienteResponse toClienteResponse(Cliente cliente){
        var response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setNome(cliente.getNome());
        return response;
    }
    /**
     * Mapeia Response DTO
     * @param produto
     * @return
     */
    public ProdutoResponse toProdutoResponse(Produto produto){
        var response = new ProdutoResponse();
        response.setId(produto.getId());
        response.setNome(produto.getNome());
        response.setDescricao(produto.getDescricao());
        return response;
    }
    /**
     * Mapeia Response DTO
     * @param item
     * @return
     */
    public ItemPedidoResponse toItemResponse(ItemPedido item){
        var response = new ItemPedidoResponse();
        response.setProduto(toProdutoResponse(item.getProduto()));
        response.setQuantidade(item.getQuantidade());
        response.setDesconto(item.getDesconto());
        response.setDescontoPercentual(item.getDescontoPercentual());
        response.setPrecoUnitario(item.getPrecoUnitario());
        response.setValorTotal(item.getValorTotal());
        return response;
    }
    /**
     * Mapeia Response DTO
     * @param itemList
     * @return
     */
    public List<ItemPedidoResponse> toListItemPedidoResponse(List<ItemPedido> itemList){
        var response = new ArrayList<ItemPedidoResponse>();
        for (ItemPedido item : itemList) {
            response.add(toItemResponse(item));
        }
        return response;
    }
    /**
     * Mapeia Response DTO
     * @param pedido
     * @return
     */
    public PedidoResponse toPedidoResponse(Pedido pedido){
        var response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setItens(toListItemPedidoResponse(pedido.getItens()));
        response.setCliente(toClienteResponse(pedido.getCliente()));
        response.setDataCriacao(pedido.getDataCriacao());
        response.setDescontoTotal(pedido.getDescontoTotal());
        response.setStatus(pedido.getStatus());
        response.setSubtotal(pedido.getSubtotal());
        response.setValorTotal(pedido.getValorTotal());
        return response;
    }
    /**
     * Mapeia Response DTO
     * @param pedido
     * @return
     */
    public PedidoResumoResponse toPedidoResumoResponse(Pedido pedido){
        var response = new PedidoResumoResponse();
        response.setId(pedido.getId());
        response.setCliente(toClienteResponse(pedido.getCliente()));
        response.setDataCriacao(pedido.getDataCriacao());
        response.setStatus(pedido.getStatus());
        response.setValorTotal(pedido.getValorTotal());
        return response;
    }

}