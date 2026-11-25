package com.erp.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.pedido.entity.ClienteEntity;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long>{

}
