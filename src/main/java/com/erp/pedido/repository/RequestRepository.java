package com.erp.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.pedido.entity.RequestEntity;

public interface RequestRepository extends JpaRepository<RequestEntity, String> {

}
