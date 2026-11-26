package com.erp.pedido.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import com.erp.pedido.domain.*;
import com.erp.pedido.enums.StatusPedido;
import com.erp.pedido.exception.BusinessException;
import com.erp.pedido.exception.ConflictException;
import com.erp.pedido.repository.PedidoRepository;
import com.erp.pedido.repository.RequestRepository;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@Testcontainers
class PedidoServiceIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:17"));

    @Autowired private PedidoService pedidoService;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private RequestRepository requestRepository;

    private Pedido novoPedido(int quantidade) {
        return Pedido.builder()
            .cliente(Cliente.builder().id(1L).build())
            .itens(List.of(
                ItemPedido.builder().produto(Produto.builder().id(1L).build()).quantidade(quantidade).build(),
                ItemPedido.builder().produto(Produto.builder().id(3L).build()).quantidade(1).build()))
            .build();
    }

    private String requestId() {
        return UUID.randomUUID().toString();
    }

    @Test
    void deveCriarCalcularEConsultarPedido() {
        var criado = pedidoService.criarPedido(novoPedido(2), requestId());
        var consultado = pedidoService.consultarPedido(criado.getId());
        assertNotNull(criado.getId());
        assertEquals(StatusPedido.CRIADO, consultado.getStatus());
        assertEquals(new BigDecimal("67.70"), consultado.getSubtotal());
        assertEquals(new BigDecimal("5.98"), consultado.getDescontoTotal());
        assertEquals(new BigDecimal("61.72"), consultado.getValorTotal());
        assertEquals(2, consultado.getItens().size());
        assertNotNull(consultado.getDataCriacao());
    }

    @Test
    void deveRetornarMesmoPedidoEmRetry() {
        var chave = requestId();
        long antes = pedidoRepository.count();
        var primeiro = pedidoService.criarPedido(novoPedido(2), chave);
        var segundo = pedidoService.criarPedido(novoPedido(2), chave);
        assertEquals(primeiro.getId(), segundo.getId());
        assertEquals(antes + 1, pedidoRepository.count());
    }

    @Test
    void deveRejeitarRequestIdComConteudoDiferente() {
        var chave = requestId();
        pedidoService.criarPedido(novoPedido(2), chave);
        long antes = pedidoRepository.count();
        assertThrows(ConflictException.class, () -> pedidoService.criarPedido(novoPedido(3), chave));
        assertEquals(antes, pedidoRepository.count());
    }

    @Test
    void deveReverterRegistroDaRequestQuandoProdutoNaoExiste() {
        var chave = requestId();
        var pedido = novoPedido(2);
        pedido.getItens().get(0).getProduto().setId(999999L);
        long antes = pedidoRepository.count();
        assertThrows(BusinessException.class, () -> pedidoService.criarPedido(pedido, chave));
        assertFalse(requestRepository.existsById(chave));
        assertEquals(antes, pedidoRepository.count());
        assertNotNull(pedidoService.criarPedido(novoPedido(2), chave).getId());
    }

    @Test
    void deveListarPedidosPorClienteComPaginacao() {
        pedidoService.criarPedido(novoPedido(2), requestId());
        var pagina = pedidoService.listarPedidos(1L, PageRequest.of(0, 1));
        assertEquals(1, pagina.getContent().size());
        assertEquals(1L, pagina.getContent().get(0).getCliente().getId());
        assertTrue(pedidoService.listarPedidos(999999L, PageRequest.of(0, 1)).isEmpty());
    }

    @Test
    void deveAceitarSomenteUmaAlteracaoConcorrente() throws Exception {
        var pedido = pedidoService.criarPedido(novoPedido(2), requestId());
        pedidoService.alterarStatus(pedido.getId(), StatusPedido.PROCESSANDO);
        var executor = Executors.newFixedThreadPool(2);
        var prontos = new CountDownLatch(2);
        var iniciar = new CountDownLatch(1);
        try {
            var faturar = executor.submit(() -> alterarConcorrentemente(pedido.getId(), StatusPedido.FATURADO, prontos, iniciar));
            var cancelar = executor.submit(() -> alterarConcorrentemente(pedido.getId(), StatusPedido.CANCELADO, prontos, iniciar));
            assertTrue(prontos.await(10, TimeUnit.SECONDS));
            iniciar.countDown();
            boolean faturado = faturar.get(30, TimeUnit.SECONDS);
            boolean cancelado = cancelar.get(30, TimeUnit.SECONDS);
            assertNotEquals(faturado, cancelado);
            assertEquals(faturado ? StatusPedido.FATURADO : StatusPedido.CANCELADO,
                pedidoService.consultarPedido(pedido.getId()).getStatus());
        } finally {
            iniciar.countDown();
            executor.shutdownNow();
        }
    }

    private boolean alterarConcorrentemente(Long id, StatusPedido status,
            CountDownLatch prontos, CountDownLatch iniciar) throws InterruptedException {
        prontos.countDown();
        if (!iniciar.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Tempo de espera excedido");
        }
        try {
            pedidoService.alterarStatus(id, status);
            return true;
        } catch (ConflictException ex) {
            return false;
        }
    }
}
