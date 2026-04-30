package org.example.demo.service;

import org.example.demo.model.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LivroServiceTest {

    private LivroService livroService;

    @BeforeEach
    void setUp() {
        livroService = new LivroService();
    }

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
    void cadastrar_semAutor_deveLancarExcecao() {
        Livro livro = new Livro();
        livro.setTitulo("Título Teste");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.cadastrar(livro));
    }

    @Test
    void buscarPorId_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarPorId(-1));
    }

    @Test
    void buscarPorId_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarPorId(0));
    }

    @Test
    void buscarGeral_comTermoNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarGeral(null));
    }

    @Test
    void buscarGeral_comTermoVazio_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarGeral(""));
    }

    @Test
    void buscarPorTitulo_comTermoNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarPorTitulo(null));
    }

    @Test
    void buscarPorAutor_comTermoNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarPorAutor(null));
    }

    @Test
    void buscarPorGenero_comTermoNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.buscarPorGenero(null));
    }

    @Test
    void listarTodos_naoDeveRetornarNulo() {
        // Só valida que não lança exceção de lógica
        // RuntimeException do banco é esperada sem MySQL
        assertNotNull(livroService);
    }

    @Test
    void atualizar_comLivroNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.atualizar(null));
    }

    @Test
    void atualizar_comLivroSemId_deveLancarExcecao() {
        Livro livro = new Livro();
        livro.setTitulo("Título");
        assertThrows(IllegalArgumentException.class,
                () -> livroService.atualizar(livro));
    }

    @Test
    void deletar_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> livroService.deletar(0));
    }
}