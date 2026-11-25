package com.erp.pedido.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ItemPedido
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (name = "item_pedido")
public class ItemPedidoEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private ProdutoEntity produto;
    @Column (nullable=false)
    private Integer quantidade;
    @Column (precision=10, scale=2, nullable=false)
    private BigDecimal precoUnitario;
    @Column (precision=5, scale=2, nullable=false)
    private BigDecimal descontoPercentual;
    @Column (precision=19, scale=2, nullable=false)
    private BigDecimal desconto;
    @Column (precision=19, scale=2, nullable=false)
    private BigDecimal valorTotal;

}