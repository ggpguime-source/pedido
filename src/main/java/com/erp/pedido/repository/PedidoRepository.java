package com.erp.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.pedido.entity.PedidoEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoRepository extends JpaRepository<PedidoEntity,Long> {
    Page<PedidoEntity> findByClienteId(Long clienteId, Pageable pageable);
}
