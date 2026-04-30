package org.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultaServiceTest {

    private MultaService multaService;

    @BeforeEach
    void setUp() {
        multaService = new MultaService();
    }

    // ✅ Testes gerarMulta — validações sem banco
    @Test
    void gerarMulta_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.gerarMulta(-1));
    }

    @Test
    void gerarMulta_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.gerarMulta(0));
    }

    // ✅ Testes registrarPagamento — validações sem banco
    @Test
    void registrarPagamento_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.registrarPagamento(-1));
    }

    @Test
    void registrarPagamento_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.registrarPagamento(0));
    }

    // ✅ Testes buscarPorUsuario — validações sem banco
    @Test
    void buscarPorUsuario_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.buscarPorUsuario(-1));
    }

    @Test
    void buscarPorUsuario_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.buscarPorUsuario(0));
    }

    // ✅ Testes buscarPendentes — validações sem banco
    @Test
    void buscarPendentes_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.buscarPendentes(-1));
    }

    @Test
    void buscarPendentes_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.buscarPendentes(0));
    }

    // ✅ Testes calcularTotalMultasPendentes — validações sem banco
    @Test
    void calcularTotalMultasPendentes_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.calcularTotalMultasPendentes(-1));
    }

    @Test
    void calcularTotalMultasPendentes_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.calcularTotalMultasPendentes(0));
    }

    // ✅ Testes usuarioPossuiMultaPendente — validações sem banco
    @Test
    void usuarioPossuiMultaPendente_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.usuarioPossuiMultaPendente(-1));
    }

    @Test
    void serviceDeveEstarInstanciado() {
        assertNotNull(multaService);
    }
}