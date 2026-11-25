package com.erp.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.pedido.entity.PedidoEntity;

public interface PedidoRepository extends JpaRepository<PedidoEntity,Long> {

}
