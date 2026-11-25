package com.erp.pedido.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
@Entity
@Table (name = "produto")
public class ProdutoEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (length=255, nullable=false)
    private String nome;
    @Column (length=255)
    private String descricao;
    @Column(name = "preco_atual", precision=10, scale=2, nullable=false)
    private BigDecimal precoAtual;
    @Column (precision=5, scale=2, nullable=false)
    private BigDecimal descontoPercentual;
}
