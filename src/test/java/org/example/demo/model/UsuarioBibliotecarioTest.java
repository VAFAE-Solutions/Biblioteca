package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioBibliotecarioTest {

    private UsuarioBibliotecario bibliotecario;

    @BeforeEach
    void setUp() {
        bibliotecario = new UsuarioBibliotecario(
                "Anderson Jesus", "ander@biblioteca.com", "hashsenha", 1);
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_vazio_deveTipoBibliotecario() {
        UsuarioBibliotecario b = new UsuarioBibliotecario();

        assertNotNull(b);
        assertEquals(Usuario.Tipo.BIBLIOTECARIO, b.getTipo(),
                "Tipo deve ser BIBLIOTECARIO automaticamente");
    }

    @Test
    void construtor_simples_deveDefinirValoresCorretamente() {
        UsuarioBibliotecario b = new UsuarioBibliotecario(
                "Maria Silva", "maria@biblioteca.com", "hash123", 2);

        assertEquals("Maria Silva", b.getNome());
        assertEquals("maria@biblioteca.com", b.getEmail());
        assertEquals("hash123", b.getSenhaHash());
        assertEquals(Usuario.Tipo.BIBLIOTECARIO, b.getTipo());
        assertEquals(2, b.getUnidadeId());
    }

    @Test
    void construtor_completo_deveDefinirTodosOsCampos() {
        LocalDateTime agora = LocalDateTime.now();
        UsuarioBibliotecario b = new UsuarioBibliotecario(
                1, "Anderson", "ander@biblioteca.com", "hash123",
                "123.456.789-00", "(11) 99999-9999",
                false, true, 0, null, agora, agora, 3);

        assertEquals(1, b.getId());
        assertEquals("Anderson", b.getNome());
        assertEquals(3, b.getUnidadeId());
        assertEquals(Usuario.Tipo.BIBLIOTECARIO, b.getTipo());
        assertFalse(b.isBloqueado());
        assertTrue(b.isAtivo());
    }

    // ================================================================
    // Heranca de Usuario
    // ================================================================

    @Test
    void bibliotecario_deveHerdarDeUsuario() {
        assertInstanceOf(Usuario.class, bibliotecario);
    }

    @Test
    void bibliotecario_tipoDeveSerSempreBibliotecario() {
        assertEquals(Usuario.Tipo.BIBLIOTECARIO, bibliotecario.getTipo());
    }

    @Test
    void bibliotecario_inicialmente_deveEstarAtivo() {
        assertTrue(bibliotecario.isAtivo());
    }

    @Test
    void bibliotecario_inicialmente_naoDeveEstarBloqueado() {
        assertFalse(bibliotecario.isBloqueado());
    }

    // ================================================================
    // unidadeId
    // ================================================================

    @Test
    void getUnidadeId_deveRetornarIdDefinido() {
        assertEquals(1, bibliotecario.getUnidadeId());
    }

    @Test
    void setUnidadeId_deveAtualizarId() {
        bibliotecario.setUnidadeId(5);
        assertEquals(5, bibliotecario.getUnidadeId());
    }

    // ================================================================
    // unidade
    // ================================================================

    @Test
    void getUnidade_inicialmente_deveSerNulo() {
        assertNull(bibliotecario.getUnidade());
    }

    @Test
    void setUnidade_deveAtualizarUnidadeEUnidadeId() {
        Unidade unidade = new Unidade();
        unidade.setId(3);
        unidade.setNome("Unidade Norte");

        bibliotecario.setUnidade(unidade);

        assertEquals(unidade, bibliotecario.getUnidade());
        assertEquals(3, bibliotecario.getUnidadeId(),
                "UnidadeId deve ser sincronizado ao setar unidade");
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterNomeEUnidadeId() {
        String str = bibliotecario.toString();

        assertTrue(str.contains("Anderson Jesus"),
                "Deve conter nome");
        assertTrue(str.contains("1"),
                "Deve conter unidadeId");
    }

    @Test
    void toString_deveConterId() {
        bibliotecario.setId(1);
        String str = bibliotecario.toString();

        assertTrue(str.contains("1"), "Deve conter id");
    }
}