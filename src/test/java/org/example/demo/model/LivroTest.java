package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LivroTest {

    private Livro livro;

    @BeforeEach
    void setUp() {
        livro = new Livro(
                "Clean Code", "Robert C. Martin", "Prentice Hall",
                2008, "Tecnologia", "Guia de boas praticas", "Sumario teste");
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_simples_deveDefinirValoresCorretamente() {
        Livro l = new Livro(
                "Clean Code", "Robert C. Martin", "Prentice Hall",
                2008, "Tecnologia", "Descricao", "Sumario");

        assertEquals("Clean Code", l.getTitulo());
        assertEquals("Robert C. Martin", l.getAutor());
        assertEquals("Prentice Hall", l.getEditora());
        assertEquals(2008, l.getAnoPublicacao());
        assertEquals("Tecnologia", l.getGenero());
        assertEquals("Descricao", l.getDescricao());
        assertEquals("Sumario", l.getSumario());
        assertTrue(l.isAtivo(), "Ativo padrao deve ser true");
    }

    @Test
    void construtor_completo_deveDefinirTodosOsCampos() {
        LocalDateTime agora = LocalDateTime.now();
        Livro l = new Livro(
                10, "Clean Code", "Robert C. Martin", "Prentice Hall",
                2008, "Tecnologia", "Descricao", "Sumario",
                "https://capa.jpg", false, agora);

        assertEquals(10, l.getId());
        assertEquals("Clean Code", l.getTitulo());
        assertEquals("https://capa.jpg", l.getCapaUrl());
        assertFalse(l.isAtivo());
        assertEquals(agora, l.getCreatedAt());
    }

    @Test
    void construtor_vazio_devePermitirCriacao() {
        Livro l = new Livro();

        assertNotNull(l);
        assertTrue(l.isAtivo(), "Ativo padrao deve ser true");
    }

    // ================================================================
    // id
    // ================================================================

    @Test
    void getId_deveRetornarIdDefinido() {
        livro.setId(42);
        assertEquals(42, livro.getId());
    }

    @Test
    void setId_deveAtualizarId() {
        livro.setId(99);
        assertEquals(99, livro.getId());
    }

    // ================================================================
    // titulo
    // ================================================================

    @Test
    void getTitulo_deveRetornarTituloDefinido() {
        assertEquals("Clean Code", livro.getTitulo());
    }

    @Test
    void setTitulo_deveAtualizarTitulo() {
        livro.setTitulo("The Pragmatic Programmer");
        assertEquals("The Pragmatic Programmer", livro.getTitulo());
    }

    // ================================================================
    // autor
    // ================================================================

    @Test
    void getAutor_deveRetornarAutorDefinido() {
        assertEquals("Robert C. Martin", livro.getAutor());
    }

    @Test
    void setAutor_deveAtualizarAutor() {
        livro.setAutor("Martin Fowler");
        assertEquals("Martin Fowler", livro.getAutor());
    }

    // ================================================================
    // editora
    // ================================================================

    @Test
    void getEditora_deveRetornarEditoraDefinida() {
        assertEquals("Prentice Hall", livro.getEditora());
    }

    @Test
    void setEditora_deveAtualizarEditora() {
        livro.setEditora("O'Reilly");
        assertEquals("O'Reilly", livro.getEditora());
    }

    // ================================================================
    // anoPublicacao
    // ================================================================

    @Test
    void getAnoPublicacao_deveRetornarAnoDefinido() {
        assertEquals(2008, livro.getAnoPublicacao());
    }

    @Test
    void setAnoPublicacao_deveAtualizarAno() {
        livro.setAnoPublicacao(2024);
        assertEquals(2024, livro.getAnoPublicacao());
    }

    // ================================================================
    // genero
    // ================================================================

    @Test
    void getGenero_deveRetornarGeneroDefinido() {
        assertEquals("Tecnologia", livro.getGenero());
    }

    @Test
    void setGenero_deveAtualizarGenero() {
        livro.setGenero("Romance");
        assertEquals("Romance", livro.getGenero());
    }

    // ================================================================
    // descricao
    // ================================================================

    @Test
    void getDescricao_deveRetornarDescricaoDefinida() {
        assertEquals("Guia de boas praticas", livro.getDescricao());
    }

    @Test
    void setDescricao_deveAtualizarDescricao() {
        livro.setDescricao("Nova descricao");
        assertEquals("Nova descricao", livro.getDescricao());
    }

    // ================================================================
    // sumario
    // ================================================================

    @Test
    void getSumario_deveRetornarSumarioDefinido() {
        assertEquals("Sumario teste", livro.getSumario());
    }

    @Test
    void setSumario_deveAtualizarSumario() {
        livro.setSumario("Novo sumario");
        assertEquals("Novo sumario", livro.getSumario());
    }

    // ================================================================
    // capaUrl
    // ================================================================

    @Test
    void getCapaUrl_inicialmente_deveSerNulo() {
        assertNull(livro.getCapaUrl());
    }

    @Test
    void setCapaUrl_deveAtualizarUrl() {
        livro.setCapaUrl("https://capa.jpg");
        assertEquals("https://capa.jpg", livro.getCapaUrl());
    }

    // ================================================================
    // ativo
    // ================================================================

    @Test
    void isAtivo_inicialmente_deveSerTrue() {
        assertTrue(livro.isAtivo(),
                "Livro deve estar ativo por padrao");
    }

    @Test
    void setAtivo_false_deveDesativar() {
        livro.setAtivo(false);
        assertFalse(livro.isAtivo());
    }

    @Test
    void setAtivo_true_deveReativar() {
        livro.setAtivo(false);
        livro.setAtivo(true);
        assertTrue(livro.isAtivo());
    }

    // ================================================================
    // createdAt
    // ================================================================

    @Test
    void getCreatedAt_inicialmente_deveSerNulo() {
        assertNull(livro.getCreatedAt());
    }

    @Test
    void setCreatedAt_deveAtualizarData() {
        LocalDateTime agora = LocalDateTime.now();
        livro.setCreatedAt(agora);
        assertEquals(agora, livro.getCreatedAt());
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterCamposEssenciais() {
        livro.setId(1);

        String str = livro.toString();

        assertTrue(str.contains("Clean Code"),       "Deve conter titulo");
        assertTrue(str.contains("Robert C. Martin"), "Deve conter autor");
        assertTrue(str.contains("true"),             "Deve conter ativo");
    }
}