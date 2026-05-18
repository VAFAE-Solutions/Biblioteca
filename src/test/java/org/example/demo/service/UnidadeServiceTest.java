package org.example.demo.service;

import org.example.demo.dao.UnidadeDAO;
import org.example.demo.model.Unidade;
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
class UnidadeServiceTest {

    @Mock private UnidadeDAO unidadeDAO;

    @InjectMocks
    private UnidadeService unidadeService;

    private Unidade unidadeValida;

    @BeforeEach
    void setUp() {
        unidadeValida = new Unidade();
        unidadeValida.setId(1);
        unidadeValida.setNome("Unidade Central");
        unidadeValida.setEndereco("Rua Principal, 100");
        unidadeValida.setTelefone("(11) 1234-5678");
        unidadeValida.setHorarioFuncionamento("Seg-Sex 8h-20h");
    }

    // ================================================================
    // cadastrar — validacoes
    // ================================================================

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
    void cadastrar_comNomeVazio_deveLancarExcecao() {
        Unidade unidade = new Unidade();
        unidade.setNome("   ");
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

    @Test
    void cadastrar_comEnderecoVazio_deveLancarExcecao() {
        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Teste");
        unidade.setEndereco("   ");
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.cadastrar(unidade));
    }

    @Test
    void cadastrar_comDadosValidos_deveRetornarTrue() {
        when(unidadeDAO.inserir(unidadeValida)).thenReturn(true);

        boolean resultado = unidadeService.cadastrar(unidadeValida);

        assertTrue(resultado);
        verify(unidadeDAO).inserir(unidadeValida);
    }

    @Test
    void cadastrar_semTelefoneEHorario_devePermitir() {
        // ✅ Telefone e horario sao opcionais
        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Teste");
        unidade.setEndereco("Rua Teste, 100");

        when(unidadeDAO.inserir(unidade)).thenReturn(true);

        assertDoesNotThrow(() -> unidadeService.cadastrar(unidade));
        verify(unidadeDAO).inserir(unidade);
    }

    // ================================================================
    // buscarPorId — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarPorId_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.buscarPorId(id));
    }

    @Test
    void buscarPorId_comIdValido_deveRetornarUnidade() {
        when(unidadeDAO.buscarPorId(1)).thenReturn(unidadeValida);

        Unidade resultado = unidadeService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Unidade Central", resultado.getNome());
        verify(unidadeDAO).buscarPorId(1);
    }

    @Test
    void buscarPorId_naoEncontrada_deveRetornarNulo() {
        when(unidadeDAO.buscarPorId(99)).thenReturn(null);

        Unidade resultado = unidadeService.buscarPorId(99);

        assertNull(resultado);
    }

    // ================================================================
    // listarTodas
    // ================================================================

    @Test
    void listarTodas_deveRetornarLista() {
        when(unidadeDAO.listarTodas()).thenReturn(List.of(unidadeValida));

        var resultado = unidadeService.listarTodas();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(unidadeDAO).listarTodas();
    }

    @Test
    void listarTodas_semUnidades_deveRetornarListaVazia() {
        when(unidadeDAO.listarTodas()).thenReturn(List.of());

        var resultado = unidadeService.listarTodas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ================================================================
    // atualizar — validacoes
    // ================================================================

    @Test
    void atualizar_comUnidadeNula_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.atualizar(null));
    }

    @Test
    void atualizar_comUnidadeSemId_deveLancarExcecao() {
        Unidade unidade = new Unidade();
        unidade.setNome("Unidade Teste");
        unidade.setEndereco("Rua Teste, 100");
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

    @Test
    void atualizar_comNomeVazio_deveLancarExcecao() {
        Unidade unidade = new Unidade();
        unidade.setId(1);
        unidade.setNome("   ");
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.atualizar(unidade));
    }

    @Test
    void atualizar_comDadosValidos_deveRetornarTrue() {
        when(unidadeDAO.atualizar(unidadeValida)).thenReturn(true);

        boolean resultado = unidadeService.atualizar(unidadeValida);

        assertTrue(resultado);
        verify(unidadeDAO).atualizar(unidadeValida);
    }

    @Test
    void atualizar_deveAtualizarNomeEEndereco() {
        unidadeValida.setNome("Unidade Norte");
        unidadeValida.setEndereco("Rua Nova, 200");

        when(unidadeDAO.atualizar(unidadeValida)).thenReturn(true);

        boolean resultado = unidadeService.atualizar(unidadeValida);

        assertTrue(resultado);
        assertEquals("Unidade Norte", unidadeValida.getNome());
        assertEquals("Rua Nova, 200", unidadeValida.getEndereco());
    }

    // ================================================================
    // desativar e deletar
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void desativar_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.desativar(id));
    }

    @Test
    void desativar_comIdValido_deveRetornarTrue() {
        when(unidadeDAO.desativar(1)).thenReturn(true);

        boolean resultado = unidadeService.desativar(1);

        assertTrue(resultado);
        verify(unidadeDAO).desativar(1);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void deletar_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> unidadeService.deletar(id));
    }

    @Test
    void deletar_deveChmarDesativar() {
        // ✅ deletar e desativar sao equivalentes
        when(unidadeDAO.desativar(1)).thenReturn(true);

        boolean resultado = unidadeService.deletar(1);

        assertTrue(resultado);
        verify(unidadeDAO).desativar(1);
    }
}