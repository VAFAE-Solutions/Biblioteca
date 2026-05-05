package org.example.demo.service;

import org.example.demo.model.Exemplar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExemplarServiceTest {

    private ExemplarService exemplarService;

    @BeforeEach
    void setUp() {
        exemplarService = new ExemplarService();
    }

    // ✅ Testes cadastrar — validações sem banco
    @Test
    void cadastrar_comExemplarNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.cadastrar(null));
    }

    @Test
    void cadastrar_semCodigoPatrimonio_deveLancarExcecao() {
        Exemplar exemplar = new Exemplar();
        exemplar.setLivroId(1);
        exemplar.setUnidadeId(1);
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.cadastrar(exemplar));
    }

    @Test
    void cadastrar_semLivroId_deveLancarExcecao() {
        Exemplar exemplar = new Exemplar();
        exemplar.setCodigoPatrimonio("PAT-001");
        exemplar.setUnidadeId(1);
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.cadastrar(exemplar));
    }

    @Test
    void cadastrar_semUnidadeId_deveLancarExcecao() {
        Exemplar exemplar = new Exemplar();
        exemplar.setCodigoPatrimonio("PAT-001");
        exemplar.setLivroId(1);
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.cadastrar(exemplar));
    }

    // ✅ Testes buscarPorId — validações sem banco
    @Test
    void buscarPorId_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarPorId(-1));
    }

    @Test
    void buscarPorId_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarPorId(0));
    }

    // ✅ Testes buscarPorLivro — validações sem banco
    @Test
    void buscarPorLivro_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarPorLivro(-1));
    }

    @Test
    void buscarPorLivro_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarPorLivro(0));
    }

    // ✅ Testes buscarDisponiveisPorLivroEUnidade — validações sem banco
    @Test
    void buscarDisponiveisPorLivroEUnidade_comIdsInvalidos_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarDisponiveisPorLivroEUnidade(-1, 1));
    }

    @Test
    void buscarDisponiveisPorLivroEUnidade_comUnidadeIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarDisponiveisPorLivroEUnidade(1, 0));
    }

    // ✅ Testes atualizarStatus — validações sem banco
    @Test
    void atualizarStatus_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.atualizarStatus(-1, Exemplar.Status.DISPONIVEL));
    }

    @Test
    void atualizarStatus_comStatusNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.atualizarStatus(1, null));
    }

    // ✅ Teste instância
    @Test
    void serviceDeveEstarInstanciado() {
        assertNotNull(exemplarService);
    }
}