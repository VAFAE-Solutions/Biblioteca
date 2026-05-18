package org.example.demo.service;

import org.example.demo.dao.LivroDAO;
import org.example.demo.model.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock private LivroDAO livroDAO;

    @InjectMocks
    private LivroService livroService;

    private Livro livroValido;

    @BeforeEach
    void setUp() {
        livroValido = new Livro();
        livroValido.setId(1);
        livroValido.setTitulo("Clean Code");
        livroValido.setAutor("Robert C. Martin");
        livroValido.setEditora("Prentice Hall");
        livroValido.setGenero("Tecnologia");
        livroValido.setAnoPublicacao(2008);
    }

    // ================================================================
    // cadastrar — validacoes
    // ================================================================

    @Test
    void cadastrar_comLivroNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(null));
    }

    @Test
    void cadastrar_semTitulo_deveLancarExcecao() {
        Livro livro = new Livro();
        livro.setAutor("Autor Teste");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livro));
    }

    @Test
    void cadastrar_comTituloVazio_deveLancarExcecao() {
        Livro livro = new Livro();
        livro.setTitulo("   ");
        livro.setAutor("Autor Teste");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livro));
    }

    @Test
    void cadastrar_semAutor_deveLancarExcecao() {
        Livro livro = new Livro();
        livro.setTitulo("Titulo Teste");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livro));
    }

    @Test
    void cadastrar_comAutorVazio_deveLancarExcecao() {
        Livro livro = new Livro();
        livro.setTitulo("Titulo Teste");
        livro.setAutor("   ");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livro));
    }

    @Test
    void cadastrar_comDadosValidos_deveRetornarTrue() {
        when(livroDAO.inserir(livroValido)).thenReturn(true);

        boolean resultado = livroService.cadastrar(livroValido);

        assertTrue(resultado);
        verify(livroDAO).inserir(livroValido);
    }

    // ================================================================
    // buscarPorId — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarPorId_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarPorId(id));
    }

    @Test
    void buscarPorId_comIdValido_deveRetornarLivro() {
        when(livroDAO.buscarPorId(1)).thenReturn(livroValido);

        Livro resultado = livroService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Clean Code", resultado.getTitulo());
        verify(livroDAO).buscarPorId(1);
    }

    @Test
    void buscarPorId_naoEncontrado_deveRetornarNulo() {
        when(livroDAO.buscarPorId(99)).thenReturn(null);

        Livro resultado = livroService.buscarPorId(99);

        assertNull(resultado);
    }

    // ================================================================
    // buscarGeral — validacoes e busca
    // ================================================================

    @ParameterizedTest
    @NullAndEmptySource
    void buscarGeral_comTermoNuloOuVazio_deveLancarExcecao(String termo) {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarGeral(termo));
    }

    @Test
    void buscarGeral_comTermoBranco_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarGeral("   "));
    }

    @Test
    void buscarGeral_comTermoValido_deveRetornarLista() {
        when(livroDAO.buscarGeral("Clean")).thenReturn(List.of(livroValido));

        var resultado = livroService.buscarGeral("Clean");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Clean Code", resultado.getFirst().getTitulo());
    }

    @Test
    void buscarGeral_semResultados_deveRetornarListaVazia() {
        when(livroDAO.buscarGeral("xyz")).thenReturn(List.of());

        var resultado = livroService.buscarGeral("xyz");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ================================================================
    // buscarPorTitulo — validacoes
    // ================================================================

    @ParameterizedTest
    @NullAndEmptySource
    void buscarPorTitulo_comTermoNuloOuVazio_deveLancarExcecao(String termo) {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarPorTitulo(termo));
    }

    @Test
    void buscarPorTitulo_comTermoValido_deveRetornarLista() {
        when(livroDAO.buscarPorTitulo("Clean")).thenReturn(List.of(livroValido));

        var resultado = livroService.buscarPorTitulo("Clean");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    // ================================================================
    // buscarPorAutor — validacoes
    // ================================================================

    @ParameterizedTest
    @NullAndEmptySource
    void buscarPorAutor_comTermoNuloOuVazio_deveLancarExcecao(String termo) {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarPorAutor(termo));
    }

    @Test
    void buscarPorAutor_comTermoValido_deveRetornarLista() {
        when(livroDAO.buscarPorAutor("Martin")).thenReturn(List.of(livroValido));

        var resultado = livroService.buscarPorAutor("Martin");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    // ================================================================
    // buscarPorGenero — validacoes
    // ================================================================

    @ParameterizedTest
    @NullAndEmptySource
    void buscarPorGenero_comTermoNuloOuVazio_deveLancarExcecao(String termo) {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarPorGenero(termo));
    }

    @Test
    void buscarPorGenero_comTermoValido_deveRetornarLista() {
        when(livroDAO.buscarPorGenero("Tecnologia")).thenReturn(List.of(livroValido));

        var resultado = livroService.buscarPorGenero("Tecnologia");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    // ================================================================
    // listarTodos
    // ================================================================

    @Test
    void listarTodos_deveRetornarLista() {
        when(livroDAO.listarTodos()).thenReturn(List.of(livroValido));

        var resultado = livroService.listarTodos();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(livroDAO).listarTodos();
    }

    @Test
    void listarTodos_semLivros_deveRetornarListaVazia() {
        when(livroDAO.listarTodos()).thenReturn(List.of());

        var resultado = livroService.listarTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ================================================================
    // atualizar — validacoes
    // ================================================================

    @Test
    void atualizar_comLivroNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.atualizar(null));
    }

    @Test
    void atualizar_comLivroSemId_deveLancarExcecao() {
        Livro livro = new Livro();
        livro.setTitulo("Titulo");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.atualizar(livro));
    }

    @Test
    void atualizar_semTitulo_deveLancarExcecao() {
        Livro livro = new Livro();
        livro.setId(1);
        assertThrows(IllegalArgumentException.class,
                () -> livroService.atualizar(livro));
    }

    @Test
    void atualizar_comDadosValidos_deveRetornarTrue() {
        when(livroDAO.atualizar(livroValido)).thenReturn(true);

        boolean resultado = livroService.atualizar(livroValido);

        assertTrue(resultado);
        verify(livroDAO).atualizar(livroValido);
    }

    // ================================================================
    // desativar e deletar
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void desativar_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.desativar(id));
    }

    @Test
    void desativar_comIdValido_deveRetornarTrue() {
        when(livroDAO.desativar(1)).thenReturn(true);

        boolean resultado = livroService.desativar(1);

        assertTrue(resultado);
        verify(livroDAO).desativar(1);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void deletar_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.deletar(id));
    }

    @Test
    void deletar_deveChmarDesativar() {
        // ✅ deletar e desativar sao equivalentes
        when(livroDAO.desativar(1)).thenReturn(true);

        boolean resultado = livroService.deletar(1);

        assertTrue(resultado);
        verify(livroDAO).desativar(1);
    }
}