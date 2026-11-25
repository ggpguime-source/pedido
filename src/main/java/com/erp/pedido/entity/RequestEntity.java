package com.erp.pedido.entity;

import com.erp.pedido.enums.StatusRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@Entity 
@Table (name = "request")
public class RequestEntity {
    @Id
    @Column (length = 100, nullable = false)
    private String requestId;
    @Column (length = 64, nullable=false)
    private String requestHash;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private PedidoEntity pedido;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private StatusRequest status;
}
