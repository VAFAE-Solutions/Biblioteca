package org.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservaServiceTest {

    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        reservaService = new ReservaService();
    }

    // ✅ Testes realizarReserva — validações sem banco
    @Test
    void realizarReserva_comLivroIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.realizarReserva(-1, 1, 1));
    }

    @Test
    void realizarReserva_comUsuarioIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.realizarReserva(1, -1, 1));
    }

    @Test
    void realizarReserva_comUnidadeIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.realizarReserva(1, 1, -1));
    }

    @Test
    void realizarReserva_comTodosIdsInvalidos_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.realizarReserva(0, 0, 0));
    }

    // ✅ Testes cancelarReserva — validações sem banco
    @Test
    void cancelarReserva_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.cancelarReserva(-1));
    }

    @Test
    void cancelarReserva_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.cancelarReserva(0));
    }

    // ✅ Testes buscarFila — validações sem banco
    @Test
    void buscarFila_comLivroIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.buscarFila(-1, 1));
    }

    @Test
    void buscarFila_comUnidadeIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.buscarFila(1, -1));
    }

    @Test
    void buscarFila_comAmbosIdsInvalidos_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.buscarFila(0, 0));
    }

    // ✅ Testes buscarPorUsuario — validações sem banco
    @Test
    void buscarPorUsuario_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.buscarPorUsuario(-1));
    }

    @Test
    void buscarPorUsuario_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.buscarPorUsuario(0));
    }

    // ✅ Teste atenderProximaReserva — só valida instância
    @Test
    void serviceDeveEstarInstanciado() {
        assertNotNull(reservaService);
    }
}