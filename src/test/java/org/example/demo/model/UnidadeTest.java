package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnidadeTest {

    private Unidade unidade;

    @BeforeEach
    void setUp() {
        unidade = new Unidade(
                "Unidade Central", "Rua Principal, 100",
                "(11) 1234-5678", "Seg-Sex 8h-20h");
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_simples_deveDefinirValoresCorretamente() {
        Unidade u = new Unidade(
                "Unidade Norte", "Rua Norte, 200",
                "(11) 9999-9999", "Seg-Sex 9h-18h");

        assertEquals("Unidade Norte", u.getNome());
        assertEquals("Rua Norte, 200", u.getEndereco());
        assertEquals("(11) 9999-9999", u.getTelefone());
        assertEquals("Seg-Sex 9h-18h", u.getHorarioFuncionamento());
        assertEquals(0, u.getId(), "ID deve ser 0 antes de persistir");
    }

    @Test
    void construtor_completo_deveDefinirTodosOsCampos() {
        Unidade u = new Unidade(
                10, "Unidade Sul", "Rua Sul, 300",
                "(11) 8888-8888", "Seg-Dom 8h-22h");

        assertEquals(10, u.getId());
        assertEquals("Unidade Sul", u.getNome());
        assertEquals("Rua Sul, 300", u.getEndereco());
        assertEquals("(11) 8888-8888", u.getTelefone());
        assertEquals("Seg-Dom 8h-22h", u.getHorarioFuncionamento());
    }

    @Test
    void construtor_vazio_devePermitirCriacao() {
        Unidade u = new Unidade();

        assertNotNull(u);
        assertEquals(0, u.getId());
        assertNull(u.getNome());
        assertNull(u.getEndereco());
    }

    // ================================================================
    // id
    // ================================================================

    @Test
    void getId_deveRetornarIdDefinido() {
        unidade.setId(42);
        assertEquals(42, unidade.getId());
    }

    @Test
    void setId_deveAtualizarId() {
        unidade.setId(99);
        assertEquals(99, unidade.getId());
    }

    // ================================================================
    // nome
    // ================================================================

    @Test
    void getNome_deveRetornarNomeDefinido() {
        assertEquals("Unidade Central", unidade.getNome());
    }

    @Test
    void setNome_deveAtualizarNome() {
        unidade.setNome("Unidade Leste");
        assertEquals("Unidade Leste", unidade.getNome());
    }

    // ================================================================
    // endereco
    // ================================================================

    @Test
    void getEndereco_deveRetornarEnderecoDefinido() {
        assertEquals("Rua Principal, 100", unidade.getEndereco());
    }

    @Test
    void setEndereco_deveAtualizarEndereco() {
        unidade.setEndereco("Av. Paulista, 1000");
        assertEquals("Av. Paulista, 1000", unidade.getEndereco());
    }

    // ================================================================
    // telefone
    // ================================================================

    @Test
    void getTelefone_deveRetornarTelefoneDefinido() {
        assertEquals("(11) 1234-5678", unidade.getTelefone());
    }

    @Test
    void setTelefone_deveAtualizarTelefone() {
        unidade.setTelefone("(11) 9876-5432");
        assertEquals("(11) 9876-5432", unidade.getTelefone());
    }

    @Test
    void setTelefone_nulo_devePermitir() {
        // ✅ Telefone e opcional
        unidade.setTelefone(null);
        assertNull(unidade.getTelefone());
    }

    // ================================================================
    // horarioFuncionamento
    // ================================================================

    @Test
    void getHorarioFuncionamento_deveRetornarHorarioDefinido() {
        assertEquals("Seg-Sex 8h-20h", unidade.getHorarioFuncionamento());
    }

    @Test
    void setHorarioFuncionamento_deveAtualizarHorario() {
        unidade.setHorarioFuncionamento("Seg-Dom 8h-22h");
        assertEquals("Seg-Dom 8h-22h", unidade.getHorarioFuncionamento());
    }

    @Test
    void setHorarioFuncionamento_nulo_devePermitir() {
        // ✅ Horario e opcional
        unidade.setHorarioFuncionamento(null);
        assertNull(unidade.getHorarioFuncionamento());
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterIdENome() {
        unidade.setId(1);

        String str = unidade.toString();

        assertTrue(str.contains("Unidade Central"), "Deve conter nome");
    }

    @Test
    void toString_semId_deveConterZero() {
        String str = unidade.toString();

        assertTrue(str.contains("0"), "Deve conter id 0");
    }
}