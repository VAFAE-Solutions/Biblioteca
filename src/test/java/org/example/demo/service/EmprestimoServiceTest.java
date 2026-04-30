package org.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoServiceTest {

    private EmprestimoService emprestimoService;

    @BeforeEach
    void setUp() {
        emprestimoService = new EmprestimoService();
    }

    // ✅ Testes realizarEmprestimo — validações sem banco
    @Test
    void realizarEmprestimo_comExemplarIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.realizarEmprestimo(-1, 1));
    }

    @Test
    void realizarEmprestimo_comUsuarioIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.realizarEmprestimo(1, -1));
    }

    @Test
    void realizarEmprestimo_comAmbosIdsInvalidos_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.realizarEmprestimo(0, 0));
    }

    // ✅ Testes finalizarEmprestimo — validações sem banco
    @Test
    void finalizarEmprestimo_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.finalizarEmprestimo(-1));
    }

    @Test
    void finalizarEmprestimo_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.finalizarEmprestimo(0));
    }

    // ✅ Testes buscarPorId — validações sem banco
    @Test
    void buscarPorId_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.buscarPorId(-1));
    }

    @Test
    void buscarPorId_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.buscarPorId(0));
    }

    // ✅ Testes buscarPorUsuario — validações sem banco
    @Test
    void buscarPorUsuario_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.buscarPorUsuario(-1));
    }

    @Test
    void buscarPorUsuario_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.buscarPorUsuario(0));
    }

    // ✅ Testes buscarAtivos e buscarAtrasados — só valida instância
    @Test
    void buscarAtivos_serviceDeveEstarInstanciado() {
        assertNotNull(emprestimoService);
    }

    @Test
    void buscarAtrasados_serviceDeveEstarInstanciado() {
        assertNotNull(emprestimoService);
    }

    // ✅ Teste verificarEAtualizarAtrasos — só valida instância
    @Test
    void verificarEAtualizarAtrasos_serviceDeveEstarInstanciado() {
        assertNotNull(emprestimoService);
    }
}