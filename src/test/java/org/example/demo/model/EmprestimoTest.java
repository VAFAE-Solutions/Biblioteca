package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {

    private Emprestimo emprestimo;
    private static final LocalDate HOJE       = LocalDate.now();
    private static final LocalDate PREVISTA   = LocalDate.now().plusDays(7);
    private static final LocalDate DEVOLUCAO  = LocalDate.now().plusDays(5);

    @BeforeEach
    void setUp() {
        emprestimo = new Emprestimo(1, 1, HOJE, PREVISTA);
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_semId_deveDefinirValoresCorretamente() {
        Emprestimo emp = new Emprestimo(1, 2, HOJE, PREVISTA);

        assertEquals(1, emp.getExemplarId());
        assertEquals(2, emp.getUsuarioId());
        assertEquals(HOJE, emp.getDataEmprestimo());
        assertEquals(PREVISTA, emp.getDataDevolucaoPrevista());
        assertEquals(Emprestimo.Status.ATIVO, emp.getStatus(),
                "Status padrao deve ser ATIVO");
        assertNull(emp.getDataDevolucao(),
                "Data devolucao deve ser nula ao criar");
    }

    @Test
    void construtor_comId_deveDefinirTodosOsValores() {
        Emprestimo emp = new Emprestimo(
                10, 1, 2, HOJE, PREVISTA, DEVOLUCAO,
                Emprestimo.Status.FINALIZADO);

        assertEquals(10, emp.getId());
        assertEquals(1, emp.getExemplarId());
        assertEquals(2, emp.getUsuarioId());
        assertEquals(HOJE, emp.getDataEmprestimo());
        assertEquals(PREVISTA, emp.getDataDevolucaoPrevista());
        assertEquals(DEVOLUCAO, emp.getDataDevolucao());
        assertEquals(Emprestimo.Status.FINALIZADO, emp.getStatus());
    }

    @Test
    void construtor_vazio_devePermitirCriacao() {
        Emprestimo emp = new Emprestimo();

        assertNotNull(emp);
        assertEquals(Emprestimo.Status.ATIVO, emp.getStatus(),
                "Status padrao deve ser ATIVO mesmo no construtor vazio");
    }

    // ================================================================
    // id
    // ================================================================

    @Test
    void getId_deveRetornarIdDefinido() {
        emprestimo.setId(42);
        assertEquals(42, emprestimo.getId());
    }

    @Test
    void setId_deveAtualizarId() {
        emprestimo.setId(99);
        assertEquals(99, emprestimo.getId());
    }

    // ================================================================
    // exemplarId
    // ================================================================

    @Test
    void getExemplarId_deveRetornarExemplarIdDefinido() {
        assertEquals(1, emprestimo.getExemplarId());
    }

    @Test
    void setExemplarId_deveAtualizarExemplarId() {
        emprestimo.setExemplarId(5);
        assertEquals(5, emprestimo.getExemplarId());
    }

    // ================================================================
    // usuarioId
    // ================================================================

    @Test
    void getUsuarioId_deveRetornarUsuarioIdDefinido() {
        assertEquals(1, emprestimo.getUsuarioId());
    }

    @Test
    void setUsuarioId_deveAtualizarUsuarioId() {
        emprestimo.setUsuarioId(7);
        assertEquals(7, emprestimo.getUsuarioId());
    }

    // ================================================================
    // dataEmprestimo
    // ================================================================

    @Test
    void getDataEmprestimo_deveRetornarDataDefinida() {
        assertEquals(HOJE, emprestimo.getDataEmprestimo());
    }

    @Test
    void setDataEmprestimo_deveAtualizarData() {
        LocalDate novaData = LocalDate.now().minusDays(1);
        emprestimo.setDataEmprestimo(novaData);
        assertEquals(novaData, emprestimo.getDataEmprestimo());
    }

    // ================================================================
    // dataDevolucaoPrevista
    // ================================================================

    @Test
    void getDataDevolucaoPrevista_deveRetornarDataDefinida() {
        assertEquals(PREVISTA, emprestimo.getDataDevolucaoPrevista());
    }

    @Test
    void setDataDevolucaoPrevista_deveAtualizarData() {
        LocalDate novaPrevista = LocalDate.now().plusDays(15);
        emprestimo.setDataDevolucaoPrevista(novaPrevista);
        assertEquals(novaPrevista, emprestimo.getDataDevolucaoPrevista());
    }

    @Test
    void dataDevolucaoPrevista_comum_deveSer7DiasApos() {
        // ✅ Documentado: COMUM = 7 dias
        long dias = java.time.temporal.ChronoUnit.DAYS.between(
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucaoPrevista());
        assertEquals(7, dias,
                "Prazo para usuario COMUM deve ser 7 dias");
    }

    @Test
    void dataDevolucaoPrevista_estudante_deveSer15DiasApos() {
        // ✅ Documentado: ESTUDANTE = 15 dias
        Emprestimo empEstudante = new Emprestimo(
                1, 1, HOJE, HOJE.plusDays(15));
        long dias = java.time.temporal.ChronoUnit.DAYS.between(
                empEstudante.getDataEmprestimo(),
                empEstudante.getDataDevolucaoPrevista());
        assertEquals(15, dias,
                "Prazo para usuario ESTUDANTE deve ser 15 dias");
    }

    // ================================================================
    // dataDevolucao
    // ================================================================

    @Test
    void getDataDevolucao_inicialmente_deveSerNula() {
        assertNull(emprestimo.getDataDevolucao(),
                "Data devolucao deve ser nula enquanto nao devolvido");
    }

    @Test
    void setDataDevolucao_deveAtualizarData() {
        emprestimo.setDataDevolucao(DEVOLUCAO);
        assertEquals(DEVOLUCAO, emprestimo.getDataDevolucao());
    }

    // ================================================================
    // status
    // ================================================================

    @Test
    void getStatus_inicialmente_deveSerAtivo() {
        assertEquals(Emprestimo.Status.ATIVO, emprestimo.getStatus());
    }

    @Test
    void setStatus_paraFinalizado_deveAtualizar() {
        emprestimo.setStatus(Emprestimo.Status.FINALIZADO);
        assertEquals(Emprestimo.Status.FINALIZADO, emprestimo.getStatus());
    }

    @Test
    void setStatus_paraAtrasado_deveAtualizar() {
        emprestimo.setStatus(Emprestimo.Status.ATRASADO);
        assertEquals(Emprestimo.Status.ATRASADO, emprestimo.getStatus());
    }

    @Test
    void status_deveConterTresValores() {
        assertEquals(3, Emprestimo.Status.values().length,
                "Status deve ter ATIVO, FINALIZADO e ATRASADO");
    }

    // ================================================================
    // exemplar
    // ================================================================

    @Test
    void getExemplar_inicialmente_deveSerNulo() {
        assertNull(emprestimo.getExemplar());
    }

    @Test
    void setExemplar_deveAtualizarExemplarEExemplarId() {
        Exemplar exemplar = new Exemplar();
        exemplar.setId(5);
        exemplar.setLivroId(1);
        exemplar.setUnidadeId(1);
        exemplar.setCodigoPatrimonio("PAT-001");

        emprestimo.setExemplar(exemplar);

        assertEquals(exemplar, emprestimo.getExemplar());
        assertEquals(5, emprestimo.getExemplarId(),
                "ExemplarId deve ser sincronizado ao setar exemplar");
    }

    // ================================================================
    // usuario
    // ================================================================

    @Test
    void getUsuario_inicialmente_deveSerNulo() {
        assertNull(emprestimo.getUsuario());
    }

    @Test
    void setUsuario_deveAtualizarUsuarioEUsuarioId() {
        Usuario usuario = new Usuario();
        usuario.setId(3);
        usuario.setNome("Carlos Lima");

        emprestimo.setUsuario(usuario);

        assertEquals(usuario, emprestimo.getUsuario());
        assertEquals(3, emprestimo.getUsuarioId(),
                "UsuarioId deve ser sincronizado ao setar usuario");
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterIdExemplarIdUsuarioIdEStatus() {
        emprestimo.setId(1);

        String str = emprestimo.toString();

        assertTrue(str.contains("1"),     "Deve conter o id");
        assertTrue(str.contains("ATIVO"), "Deve conter o status");
    }
}