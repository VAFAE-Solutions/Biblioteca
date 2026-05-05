package org.example.demo.service;

import org.example.demo.model.Unidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnidadeServiceTest {

    private UnidadeService unidadeService;

    @BeforeEach
    void setUp() {
        unidadeService = new UnidadeService();
    }

    // ✅ Testes cadastrar — validações sem banco
    @Test
    void cadastrar_comUnidadeNula_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.cadastrar(null));
    }

    @Test
    void cadastrar_semNome_deveLancarExcecao() {
        Unidade unidade = new Unidade();
        unidade.setEndereco("Rua Teste, 100");
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.cadastrar(unidade));
    }

    @Test
    void cadastrar_semEndereco_deveLancarExcecao() {
        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Teste");
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.cadastrar(unidade));
    }

    // ✅ Testes buscarPorId — validações sem banco
    @Test
    void buscarPorId_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.buscarPorId(-1));
    }

    @Test
    void buscarPorId_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.buscarPorId(0));
    }

    // ✅ Teste listarTodas — só valida instância
    @Test
    void listarTodas_serviceDeveEstarInstanciado() {
        assertNotNull(unidadeService);
    }

    // ✅ Testes atualizar — validações sem banco
    @Test
    void atualizar_comUnidadeNula_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.atualizar(null));
    }

    @Test
    void atualizar_comUnidadeSemId_deveLancarExcecao() {
        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Teste");
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.atualizar(unidade));
    }

    @Test
    void atualizar_semNome_deveLancarExcecao() {
        Unidade unidade = new Unidade();
        unidade.setId(1);
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.atualizar(unidade));
    }

    // ✅ Testes deletar — validações sem banco
    @Test
    void deletar_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.deletar(-1));
    }

    @Test
    void deletar_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.deletar(0));
    }
}