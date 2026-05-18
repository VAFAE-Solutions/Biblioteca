package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExemplarTest {

    private Exemplar exemplar;

    @BeforeEach
    void setUp() {
        exemplar = new Exemplar(1, 1, "PAT-001");
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_simples_deveDefinirValoresCorretamente() {
        Exemplar emp = new Exemplar(1, 1, "PAT-001");

        assertEquals(1, emp.getLivroId());
        assertEquals(1, emp.getUnidadeId());
        assertEquals("PAT-001", emp.getCodigoPatrimonio());
        assertEquals(Exemplar.Status.DISPONIVEL, emp.getStatus(),
                "Status padrao deve ser DISPONIVEL");
        assertTrue(emp.isAtivo(),
                "Ativo padrao deve ser true");
    }

    @Test
    void construtor_comId_deveDefinirTodosOsValores() {
        Exemplar emp = new Exemplar(
                10, 2, 3, "PAT-002", Exemplar.Status.EMPRESTADO);

        assertEquals(10, emp.getId());
        assertEquals(2, emp.getLivroId());
        assertEquals(3, emp.getUnidadeId());
        assertEquals("PAT-002", emp.getCodigoPatrimonio());
        assertEquals(Exemplar.Status.EMPRESTADO, emp.getStatus());
    }

    @Test
    void construtor_completo_deveDefinirAtivo() {
        Exemplar emp = new Exemplar(
                10, 2, 3, "PAT-003", Exemplar.Status.DISPONIVEL, false);

        assertEquals(10, emp.getId());
        assertFalse(emp.isAtivo());
    }

    @Test
    void construtor_vazio_devePermitirCriacao() {
        Exemplar emp = new Exemplar();

        assertNotNull(emp);
        assertEquals(Exemplar.Status.DISPONIVEL, emp.getStatus(),
                "Status padrao deve ser DISPONIVEL");
        assertTrue(emp.isAtivo(),
                "Ativo padrao deve ser true");
    }

    // ================================================================
    // id
    // ================================================================

    @Test
    void getId_deveRetornarIdDefinido() {
        exemplar.setId(42);
        assertEquals(42, exemplar.getId());
    }

    @Test
    void setId_deveAtualizarId() {
        exemplar.setId(99);
        assertEquals(99, exemplar.getId());
    }

    // ================================================================
    // livroId
    // ================================================================

    @Test
    void getLivroId_deveRetornarLivroIdDefinido() {
        assertEquals(1, exemplar.getLivroId());
    }

    @Test
    void setLivroId_deveAtualizarLivroId() {
        exemplar.setLivroId(5);
        assertEquals(5, exemplar.getLivroId());
    }

    // ================================================================
    // unidadeId
    // ================================================================

    @Test
    void getUnidadeId_deveRetornarUnidadeIdDefinido() {
        assertEquals(1, exemplar.getUnidadeId());
    }

    @Test
    void setUnidadeId_deveAtualizarUnidadeId() {
        exemplar.setUnidadeId(3);
        assertEquals(3, exemplar.getUnidadeId());
    }

    // ================================================================
    // codigoPatrimonio
    // ================================================================

    @Test
    void getCodigoPatrimonio_deveRetornarCodigoDefinido() {
        assertEquals("PAT-001", exemplar.getCodigoPatrimonio());
    }

    @Test
    void setCodigoPatrimonio_deveAtualizarCodigo() {
        exemplar.setCodigoPatrimonio("PAT-999");
        assertEquals("PAT-999", exemplar.getCodigoPatrimonio());
    }

    // ================================================================
    // status
    // ================================================================

    @Test
    void getStatus_inicialmente_deveSerDisponivel() {
        assertEquals(Exemplar.Status.DISPONIVEL, exemplar.getStatus());
    }

    @Test
    void setStatus_paraEmprestado_deveAtualizar() {
        exemplar.setStatus(Exemplar.Status.EMPRESTADO);
        assertEquals(Exemplar.Status.EMPRESTADO, exemplar.getStatus());
    }

    @Test
    void setStatus_paraReservado_deveAtualizar() {
        exemplar.setStatus(Exemplar.Status.RESERVADO);
        assertEquals(Exemplar.Status.RESERVADO, exemplar.getStatus());
    }

    @Test
    void setStatus_paraDisponivel_deveAtualizar() {
        exemplar.setStatus(Exemplar.Status.EMPRESTADO);
        exemplar.setStatus(Exemplar.Status.DISPONIVEL);
        assertEquals(Exemplar.Status.DISPONIVEL, exemplar.getStatus());
    }

    @Test
    void status_deveConterTresValores() {
        assertEquals(3, Exemplar.Status.values().length,
                "Status deve ter DISPONIVEL, EMPRESTADO e RESERVADO");
    }

    // ================================================================
    // ativo
    // ================================================================

    @Test
    void isAtivo_inicialmente_deveSerTrue() {
        assertTrue(exemplar.isAtivo(),
                "Exemplar deve estar ativo por padrao");
    }

    @Test
    void setAtivo_false_deveDesativar() {
        exemplar.setAtivo(false);
        assertFalse(exemplar.isAtivo());
    }

    @Test
    void setAtivo_true_deveReativar() {
        exemplar.setAtivo(false);
        exemplar.setAtivo(true);
        assertTrue(exemplar.isAtivo());
    }

    // ================================================================
    // livro
    // ================================================================

    @Test
    void getLivro_inicialmente_deveSerNulo() {
        assertNull(exemplar.getLivro());
    }

    @Test
    void setLivro_deveAtualizarLivroELivroId() {
        Livro livro = new Livro();
        livro.setId(5);
        livro.setTitulo("Clean Code");

        exemplar.setLivro(livro);

        assertEquals(livro, exemplar.getLivro());
        assertEquals(5, exemplar.getLivroId(),
                "LivroId deve ser sincronizado ao setar livro");
    }

    // ================================================================
    // unidade
    // ================================================================

    @Test
    void getUnidade_inicialmente_deveSerNulo() {
        assertNull(exemplar.getUnidade());
    }

    @Test
    void setUnidade_deveAtualizarUnidadeEUnidadeId() {
        Unidade unidade = new Unidade();
        unidade.setId(3);
        unidade.setNome("Unidade Norte");

        exemplar.setUnidade(unidade);

        assertEquals(unidade, exemplar.getUnidade());
        assertEquals(3, exemplar.getUnidadeId(),
                "UnidadeId deve ser sincronizado ao setar unidade");
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterCamposEssenciais() {
        exemplar.setId(1);

        String str = exemplar.toString();

        assertTrue(str.contains("PAT-001"),   "Deve conter codigoPatrimonio");
        assertTrue(str.contains("DISPONIVEL"), "Deve conter status");
        assertTrue(str.contains("true"),       "Deve conter ativo");
    }
}