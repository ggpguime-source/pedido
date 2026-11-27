package com.erp.pedido.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.erp.pedido.entity.PedidoEntity;

import jakarta.persistence.LockModeType;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoRepository extends JpaRepository<PedidoEntity,Long> {
    @EntityGraph(attributePaths = "cliente")
    Page<PedidoEntity> findByClienteId(Long clienteId, Pageable pageable);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PedidoEntity p WHERE p.id = :id")
    Optional<PedidoEntity> findByIdForUpdate(@Param("id") Long id);

    @Override
    @EntityGraph(attributePaths = "cliente")
    Page<PedidoEntity> findAll(Pageable pageable);
}
