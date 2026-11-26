package com.erp.pedido.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.erp.pedido.entity.PedidoEntity;
import com.erp.pedido.enums.StatusPedido;
import com.erp.pedido.exception.ConflictException;
import com.erp.pedido.exception.NotFoundException;
import com.erp.pedido.mapper.RepositoryMapper;
import com.erp.pedido.repository.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {
    @Mock private PedidoRepository pedidoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ProdutoRepository produtoRepository;
    @Mock private RequestRepository requestRepository;
    @Mock private RepositoryMapper mapper;
    @InjectMocks private PedidoService pedidoService;

    @ParameterizedTest
    @CsvSource({"CRIADO, PROCESSANDO", "CRIADO, CANCELADO",
        "PROCESSANDO, FATURADO", "PROCESSANDO, CANCELADO"})
    void devePermitirTransicaoValida(StatusPedido atual, StatusPedido novo) {
        var pedido = new PedidoEntity();
        pedido.setStatus(atual);
        when(pedidoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(pedido));

        pedidoService.alterarStatus(1L, novo);

        assertEquals(novo, pedido.getStatus());
        verify(pedidoRepository).findByIdForUpdate(1L);
    }

    @ParameterizedTest
    @CsvSource({"CRIADO, CRIADO", "CRIADO, FATURADO",
        "PROCESSANDO, CRIADO", "PROCESSANDO, PROCESSANDO",
        "FATURADO, CRIADO", "FATURADO, PROCESSANDO", "FATURADO, FATURADO", "FATURADO, CANCELADO",
        "CANCELADO, CRIADO", "CANCELADO, PROCESSANDO", "CANCELADO, FATURADO", "CANCELADO, CANCELADO"})
    void deveRejeitarTransicaoInvalida(StatusPedido atual, StatusPedido novo) {
        var pedido = new PedidoEntity();
        pedido.setStatus(atual);
        when(pedidoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(pedido));

        assertThrows(ConflictException.class, () -> pedidoService.alterarStatus(1L, novo));
        assertEquals(atual, pedido.getStatus());
    }

    @Test
    void deveRejeitarConsultaDePedidoInexistente() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> pedidoService.consultarPedido(999L));
    }

    @Test
    void deveRejeitarAlteracaoDePedidoInexistente() {
        when(pedidoRepository.findByIdForUpdate(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
            () -> pedidoService.alterarStatus(999L, StatusPedido.CANCELADO));
    }
}
