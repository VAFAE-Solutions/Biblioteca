package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioEstudanteTest {

    private UsuarioEstudante estudante;

    @BeforeEach
    void setUp() {
        estudante = new UsuarioEstudante(
                "Lucas Cardoso", "lucas@email.com", "hashsenha", 2402432);
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_vazio_deveTipoEstudante() {
        UsuarioEstudante e = new UsuarioEstudante();

        assertNotNull(e);
        assertEquals(Usuario.Tipo.ESTUDANTE, e.getTipo(),
                "Tipo deve ser ESTUDANTE automaticamente");
    }

    @Test
    void construtor_simples_deveDefinirValoresCorretamente() {
        UsuarioEstudante e = new UsuarioEstudante(
                "Joao Silva", "joao@email.com", "hash123", 1234567);

        assertEquals("Joao Silva", e.getNome());
        assertEquals("joao@email.com", e.getEmail());
        assertEquals("hash123", e.getSenhaHash());
        assertEquals(Usuario.Tipo.ESTUDANTE, e.getTipo());
        assertEquals(1234567, e.getRa());
    }

    @Test
    void construtor_completo_deveDefinirTodosOsCampos() {
        LocalDateTime agora = LocalDateTime.now();
        UsuarioEstudante e = new UsuarioEstudante(
                1, "Lucas", "lucas@email.com", "hash123",
                "123.456.789-00", "(11) 99999-9999",
                false, true, 0, null, agora, agora, 9999999);

        assertEquals(1, e.getId());
        assertEquals("Lucas", e.getNome());
        assertEquals(9999999, e.getRa());
        assertEquals(Usuario.Tipo.ESTUDANTE, e.getTipo());
        assertFalse(e.isBloqueado());
        assertTrue(e.isAtivo());
    }

    // ================================================================
    // Heranca de Usuario
    // ================================================================

    @Test
    void estudante_deveHerdarDeUsuario() {
        assertInstanceOf(Usuario.class, estudante);
    }

    @Test
    void estudante_tipoDeveSerSempreEstudante() {
        assertEquals(Usuario.Tipo.ESTUDANTE, estudante.getTipo());
    }

    @Test
    void estudante_prazoEmprestimo_deveSer15Dias() {
        // ✅ Documentado: ESTUDANTE = 15 dias
        assertEquals(15, estudante.getPrazoEmprestimo(),
                "Prazo de emprestimo para estudante deve ser 15 dias");
    }

    @Test
    void estudante_limiteCotas_deveSer5() {
        // ✅ Documentado: ESTUDANTE = 5 emprestimos simultaneos
        assertEquals(5, estudante.getLimiteCotas(),
                "Limite de cotas para estudante deve ser 5");
    }

    @Test
    void estudante_inicialmente_deveEstarAtivo() {
        assertTrue(estudante.isAtivo());
    }

    @Test
    void estudante_inicialmente_naoDeveEstarBloqueado() {
        assertFalse(estudante.isBloqueado());
    }

    @Test
    void estudante_tentativasLogin_inicialmente_deveSerZero() {
        assertEquals(0, estudante.getTentativasLogin());
    }

    // ================================================================
    // ra
    // ================================================================

    @Test
    void getRa_deveRetornarRaDefinido() {
        assertEquals(2402432, estudante.getRa());
    }

    @Test
    void setRa_deveAtualizarRa() {
        estudante.setRa(9999999);
        assertEquals(9999999, estudante.getRa());
    }

    @Test
    void setRa_zero_devePermitir() {
        // ✅ Validacao de RA e feita no Service, nao no Model
        estudante.setRa(0);
        assertEquals(0, estudante.getRa());
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterNomeERa() {
        String str = estudante.toString();

        assertTrue(str.contains("Lucas Cardoso"),
                "Deve conter nome");
        assertTrue(str.contains("2402432"),
                "Deve conter RA");
    }

    @Test
    void toString_deveConterId() {
        estudante.setId(1);
        String str = estudante.toString();

        assertTrue(str.contains("1"), "Deve conter id");
    }
}