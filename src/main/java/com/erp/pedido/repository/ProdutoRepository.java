package com.erp.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.pedido.entity.ProdutoEntity;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

}
