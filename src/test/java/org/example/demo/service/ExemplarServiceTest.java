package org.example.demo.service;

import org.example.demo.dao.ExemplarDAO;
import org.example.demo.model.Exemplar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExemplarServiceTest {

    @Mock private ExemplarDAO exemplarDAO;

    @InjectMocks
    private ExemplarService exemplarService;

    private Exemplar exemplarValido;

    @BeforeEach
    void setUp() {
        exemplarValido = new Exemplar();
        exemplarValido.setId(1);
        exemplarValido.setLivroId(1);
        exemplarValido.setUnidadeId(1);
        exemplarValido.setCodigoPatrimonio("PAT-001");
        exemplarValido.setStatus(Exemplar.Status.DISPONIVEL);
    }

    // ================================================================
    // cadastrar — validacoes
    // ================================================================

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
    void cadastrar_comCodigoPatrimonioVazio_deveLancarExcecao() {
        Exemplar exemplar = new Exemplar();
        exemplar.setCodigoPatrimonio("   ");
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

    @Test
    void cadastrar_comDadosValidos_deveRetornarTrue() {
        when(exemplarDAO.inserir(exemplarValido)).thenReturn(true);

        boolean resultado = exemplarService.cadastrar(exemplarValido);

        assertTrue(resultado);
        verify(exemplarDAO).inserir(exemplarValido);
    }

    // ================================================================
    // buscarPorId — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarPorId_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarPorId(id));
    }

    @Test
    void buscarPorId_comIdValido_deveRetornarExemplar() {
        when(exemplarDAO.buscarPorId(1)).thenReturn(exemplarValido);

        Exemplar resultado = exemplarService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(exemplarDAO).buscarPorId(1);
    }

    @Test
    void buscarPorId_naoEncontrado_deveRetornarNulo() {
        when(exemplarDAO.buscarPorId(99)).thenReturn(null);

        Exemplar resultado = exemplarService.buscarPorId(99);

        assertNull(resultado);
    }

    // ================================================================
    // buscarPorLivro — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarPorLivro_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarPorLivro(id));
    }

    @Test
    void buscarPorLivro_comIdValido_deveRetornarLista() {
        when(exemplarDAO.buscarPorLivro(1)).thenReturn(List.of(exemplarValido));

        var resultado = exemplarService.buscarPorLivro(1);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    // ================================================================
    // buscarDisponiveisPorLivroEUnidade — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarDisponiveisPorLivroEUnidade_comLivroIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarDisponiveisPorLivroEUnidade(id, 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarDisponiveisPorLivroEUnidade_comUnidadeIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.buscarDisponiveisPorLivroEUnidade(1, id));
    }

    @Test
    void buscarDisponiveisPorLivroEUnidade_comDadosValidos_deveRetornarDisponiveis() {
        when(exemplarDAO.buscarDisponiveisPorLivroEUnidade(1, 1))
                .thenReturn(List.of(exemplarValido));

        var resultado = exemplarService.buscarDisponiveisPorLivroEUnidade(1, 1);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(Exemplar.Status.DISPONIVEL, resultado.getFirst().getStatus());
    }

    @Test
    void buscarDisponiveisPorLivroEUnidade_semDisponiveis_deveRetornarListaVazia() {
        when(exemplarDAO.buscarDisponiveisPorLivroEUnidade(1, 1))
                .thenReturn(List.of());

        var resultado = exemplarService.buscarDisponiveisPorLivroEUnidade(1, 1);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ================================================================
    // atualizarStatus — validacoes e regras
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void atualizarStatus_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.atualizarStatus(id, Exemplar.Status.DISPONIVEL));
    }

    @Test
    void atualizarStatus_comStatusNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.atualizarStatus(1, null));
    }

    @Test
    void atualizarStatus_exemplarNaoEncontrado_deveLancarExcecao() {
        when(exemplarDAO.buscarPorId(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.atualizarStatus(99, Exemplar.Status.DISPONIVEL));
    }

    @Test
    void atualizarStatus_paraEmprestado_deveAtualizarComSucesso() {
        when(exemplarDAO.buscarPorId(1)).thenReturn(exemplarValido);
        when(exemplarDAO.atualizar(exemplarValido)).thenReturn(true);

        boolean resultado = exemplarService.atualizarStatus(1, Exemplar.Status.EMPRESTADO);

        assertTrue(resultado);
        assertEquals(Exemplar.Status.EMPRESTADO, exemplarValido.getStatus());
        verify(exemplarDAO).atualizar(exemplarValido);
    }

    @Test
    void atualizarStatus_paraDisponivel_deveAtualizarComSucesso() {
        exemplarValido.setStatus(Exemplar.Status.EMPRESTADO);
        when(exemplarDAO.buscarPorId(1)).thenReturn(exemplarValido);
        when(exemplarDAO.atualizar(exemplarValido)).thenReturn(true);

        boolean resultado = exemplarService.atualizarStatus(1, Exemplar.Status.DISPONIVEL);

        assertTrue(resultado);
        assertEquals(Exemplar.Status.DISPONIVEL, exemplarValido.getStatus());
    }

    // ================================================================
    // desativar — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void desativar_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.desativar(id));
    }

    @Test
    void desativar_exemplarNaoEncontrado_deveLancarExcecao() {
        when(exemplarDAO.buscarPorId(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> exemplarService.desativar(99));
    }

    @Test
    void desativar_exemplarExistente_deveRetornarTrue() {
        when(exemplarDAO.buscarPorId(1)).thenReturn(exemplarValido);
        when(exemplarDAO.desativar(1)).thenReturn(true);

        boolean resultado = exemplarService.desativar(1);

        assertTrue(resultado);
        verify(exemplarDAO).desativar(1);
    }
}