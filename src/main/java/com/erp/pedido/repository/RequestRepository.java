package com.erp.pedido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.erp.pedido.entity.RequestEntity;


public interface RequestRepository extends JpaRepository<RequestEntity, String> {
    @Modifying
    @Query(value = """
        INSERT INTO request (request_id, request_hash, status)
        VALUES (:requestId, :requestHash, 'PROCESSANDO')
        ON CONFLICT (request_id) DO NOTHING
        """, nativeQuery = true)
    int registrarRequest(@Param("requestId") String requestId, @Param("requestHash") String requestHash);
}
