package com.erp.pedido.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.erp.pedido.domain.Cliente;
import com.erp.pedido.domain.ItemPedido;
import com.erp.pedido.domain.Pedido;
import com.erp.pedido.domain.Produto;
import com.erp.pedido.entity.ClienteEntity;
import com.erp.pedido.entity.ItemPedidoEntity;
import com.erp.pedido.entity.PedidoEntity;
import com.erp.pedido.entity.ProdutoEntity;

/**
 * RepositoryMapper
 */
@Component
public class RepositoryMapper {

    /**
     * Mapeia Cliente
     * @param entity
     * @return
     */
    public Cliente toClienteDomain(ClienteEntity entity){
        return Cliente.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .build();
    }

    /**
     * Mapeia Produto
     * @param entity
     * @return
     */
    public Produto toProdutoDomain(ProdutoEntity entity){
        return Produto.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .precoAtual(entity.getPrecoAtual())
                .descontoPercentual(entity.getDescontoPercentual())
                .build();
    }

    /**
     * Mapeia Item
     * @param entity
     * @return
     */
    public ItemPedido toItemPedidoDomain(ItemPedidoEntity entity){
        return ItemPedido.builder()
                .id(entity.getId())
                .produto(toProdutoDomain(entity.getProduto()))
                .quantidade(entity.getQuantidade())
                .precoUnitario(entity.getPrecoUnitario())
                .descontoPercentual(entity.getDescontoPercentual())
                .desconto(entity.getDesconto())
                .valorTotal(entity.getValorTotal())
                .build();
    }

    /**
     * Mapeia Lista de Itens
     * @param entities
     * @return
     */
    public List<ItemPedido> toListItemPedidoDomain(List<ItemPedidoEntity> entities){
        var listItemPedido = new ArrayList<ItemPedido>();

        for (ItemPedidoEntity item : entities) {
            listItemPedido.add(toItemPedidoDomain(item));
        }

        return listItemPedido;
    }

    /**
     * Mapeia Pedido
     * @param entity
     * @return
     */
    public Pedido toPedidoDomain(PedidoEntity entity){
        return Pedido.builder()
                .id(entity.getId())
                .cliente(toClienteDomain(entity.getCliente()))
                .itens(toListItemPedidoDomain(entity.getItens()))
                .subtotal(entity.getSubtotal())
                .descontoTotal(entity.getDescontoTotal())
                .valorTotal(entity.getValorTotal())
                .status(entity.getStatus())
                .dataCriacao(entity.getDataCriacao())
                .build();
    }

    /**
     * Mapeia Resumo do Pedido
     * @param entity
     * @return
     */
    public Pedido toResumoDomain(PedidoEntity entity){
        return Pedido.builder()
                .id(entity.getId())
                .cliente(toClienteDomain(entity.getCliente()))
                .valorTotal(entity.getValorTotal())
                .status(entity.getStatus())
                .dataCriacao(entity.getDataCriacao())
                .build();
    }

    /**
     * Mapeia ItemPedidoEntity
     * @param item
     * @param produto
     * @return
     */
    public ItemPedidoEntity toEntity(
            ItemPedido item,
            ProdutoEntity produto){

        var entity = new ItemPedidoEntity();

        entity.setProduto(produto);
        entity.setQuantidade(item.getQuantidade());
        entity.setPrecoUnitario(item.getPrecoUnitario());
        entity.setDescontoPercentual(item.getDescontoPercentual());
        entity.setDesconto(item.getDesconto());
        entity.setValorTotal(item.getValorTotal());

        return entity;
    }

    /**
     * Mapeia PedidoEntity para criação
     * @param pedido
     * @param cliente
     * @param produtos
     * @return
     */
    public PedidoEntity toPedidoEntity(
            Pedido pedido,
            ClienteEntity cliente,
            Map<Long, ProdutoEntity> produtos){

        var entity = new PedidoEntity();

        entity.setCliente(cliente);
        entity.setSubtotal(pedido.getSubtotal());
        entity.setDescontoTotal(pedido.getDescontoTotal());
        entity.setValorTotal(pedido.getValorTotal());
        entity.setStatus(pedido.getStatus());
        entity.setDataCriacao(pedido.getDataCriacao());

        for (ItemPedido item : pedido.getItens()) {
            var produto = produtos.get(item.getProduto().getId());
            entity.adicionarItem(toEntity(item, produto));
        }

        return entity;
    }
}