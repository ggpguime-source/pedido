package com.erp.pedido.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.erp.pedido.enums.StatusPedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Pedido
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;
    @Column (precision=19, scale=2, nullable=false)
    private BigDecimal subtotal;
    @Column (precision=19, scale=2, nullable=false)
    private BigDecimal descontoTotal;
    @Column (precision=19, scale=2, nullable=false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(length=20, nullable=false)
    private StatusPedido status;
    @Column (nullable=false)
    private Instant dataCriacao;

    @OneToMany(
        mappedBy = "pedido",
        cascade = CascadeType.PERSIST,
        fetch = FetchType.LAZY
    )
    private List<ItemPedidoEntity> itens = new ArrayList<>();

    public void adicionarItem(ItemPedidoEntity item) {
        this.itens.add(item);
        item.setPedido(this);
    }
}