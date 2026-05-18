package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilaReservaTest {

    private FilaReserva fila;

    @BeforeEach
    void setUp() {
        fila = new FilaReserva(1, 1);
    }

    // ================================================================
    // Helpers
    // ================================================================

    private Reserva criarReserva(int usuarioId, int posicao,
                                 Reserva.Status status) {
        Reserva reserva = new Reserva(1, usuarioId, 1, LocalDate.now());
        reserva.setId(usuarioId);
        reserva.setPosicaoFila(posicao);
        reserva.setStatus(status);
        return reserva;
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_comIds_deveDefinirValoresCorretamente() {
        FilaReserva f = new FilaReserva(2, 3);

        assertEquals(2, f.getLivroId());
        assertEquals(3, f.getUnidadeId());
        assertNotNull(f.getFila());
        assertTrue(f.getFila().isEmpty());
    }

    @Test
    void construtor_comObjetos_deveDefinirIdsAutomaticamente() {
        Livro livro = new Livro();
        livro.setId(5);

        Unidade unidade = new Unidade();
        unidade.setId(2);

        FilaReserva f = new FilaReserva(livro, unidade);

        assertEquals(5, f.getLivroId());
        assertEquals(2, f.getUnidadeId());
        assertEquals(livro, f.getLivro());
        assertEquals(unidade, f.getUnidade());
    }

    @Test
    void construtor_vazio_devePermitirCriacao() {
        FilaReserva f = new FilaReserva();

        assertNotNull(f);
        assertNotNull(f.getFila());
    }

    // ================================================================
    // proximaPosicao
    // ================================================================

    @Test
    void proximaPosicao_filaVazia_deveRetornar1() {
        assertEquals(1, fila.proximaPosicao(),
                "Fila vazia deve retornar posicao 1");
    }

    @Test
    void proximaPosicao_com1Reserva_deveRetornar2() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.AGUARDANDO)));

        assertEquals(2, fila.proximaPosicao());
    }

    @Test
    void proximaPosicao_com3Reservas_deveRetornar4() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.AGUARDANDO),
                criarReserva(2, 2, Reserva.Status.AGUARDANDO),
                criarReserva(3, 3, Reserva.Status.AGUARDANDO)));

        assertEquals(4, fila.proximaPosicao());
    }

    // ================================================================
    // totalAguardando
    // ================================================================

    @Test
    void totalAguardando_filaVazia_deveRetornarZero() {
        assertEquals(0, fila.totalAguardando());
    }

    @Test
    void totalAguardando_comAguardando_deveContarCorreto() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.AGUARDANDO),
                criarReserva(2, 2, Reserva.Status.AGUARDANDO),
                criarReserva(3, 3, Reserva.Status.CANCELADA)));

        assertEquals(2, fila.totalAguardando(),
                "Deve contar apenas AGUARDANDO, nao CANCELADA");
    }

    @Test
    void totalAguardando_apenasAtendidas_deveRetornarZero() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.ATENDIDA),
                criarReserva(2, 2, Reserva.Status.CANCELADA)));

        assertEquals(0, fila.totalAguardando());
    }

    // ================================================================
    // proximaReserva
    // ================================================================

    @Test
    void proximaReserva_filaVazia_deveRetornarNulo() {
        assertNull(fila.proximaReserva());
    }

    @Test
    void proximaReserva_comAguardando_deveRetornarPrimeira() {
        Reserva primeira = criarReserva(1, 1, Reserva.Status.AGUARDANDO);
        Reserva segunda  = criarReserva(2, 2, Reserva.Status.AGUARDANDO);

        fila.setFila(List.of(primeira, segunda));

        assertEquals(primeira, fila.proximaReserva(),
                "Deve retornar a primeira da fila");
    }

    @Test
    void proximaReserva_apenasAtendidas_deveRetornarNulo() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.ATENDIDA)));

        assertNull(fila.proximaReserva(),
                "Sem aguardando deve retornar nulo");
    }

    @Test
    void proximaReserva_deveIgnorarCanceladas() {
        Reserva cancelada  = criarReserva(1, 1, Reserva.Status.CANCELADA);
        Reserva aguardando = criarReserva(2, 2, Reserva.Status.AGUARDANDO);

        fila.setFila(List.of(cancelada, aguardando));

        assertEquals(aguardando, fila.proximaReserva(),
                "Deve ignorar canceladas e retornar aguardando");
    }

    // ================================================================
    // usuarioNaFila
    // ================================================================

    @Test
    void usuarioNaFila_filaVazia_deveRetornarFalse() {
        assertFalse(fila.usuarioNaFila(1));
    }

    @Test
    void usuarioNaFila_usuarioAguardando_deveRetornarTrue() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.AGUARDANDO)));

        assertTrue(fila.usuarioNaFila(1),
                "Usuario na fila com status AGUARDANDO deve retornar true");
    }

    @Test
    void usuarioNaFila_usuarioCancelado_deveRetornarFalse() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.CANCELADA)));

        assertFalse(fila.usuarioNaFila(1),
                "Usuario com reserva CANCELADA nao esta na fila");
    }

    @Test
    void usuarioNaFila_usuarioAtendido_deveRetornarFalse() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.ATENDIDA)));

        assertFalse(fila.usuarioNaFila(1),
                "Usuario com reserva ATENDIDA nao esta na fila");
    }

    @Test
    void usuarioNaFila_outroUsuario_deveRetornarFalse() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.AGUARDANDO)));

        assertFalse(fila.usuarioNaFila(999),
                "Usuario nao cadastrado na fila deve retornar false");
    }

    // ================================================================
    // isEmpty
    // ================================================================

    @Test
    void isEmpty_filaVazia_deveRetornarTrue() {
        assertTrue(fila.isEmpty());
    }

    @Test
    void isEmpty_comAguardando_deveRetornarFalse() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.AGUARDANDO)));

        assertFalse(fila.isEmpty());
    }

    @Test
    void isEmpty_apenasAtendidas_deveRetornarTrue() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.ATENDIDA)));

        assertTrue(fila.isEmpty(),
                "Fila com apenas atendidas deve ser considerada vazia");
    }

    @Test
    void isEmpty_apenascanceladas_deveRetornarTrue() {
        fila.setFila(List.of(
                criarReserva(1, 1, Reserva.Status.CANCELADA)));

        assertTrue(fila.isEmpty(),
                "Fila com apenas canceladas deve ser considerada vazia");
    }

    // ================================================================
    // livroId e unidadeId
    // ================================================================

    @Test
    void getLivroId_deveRetornarIdDefinido() {
        assertEquals(1, fila.getLivroId());
    }

    @Test
    void setLivroId_deveAtualizarId() {
        fila.setLivroId(5);
        assertEquals(5, fila.getLivroId());
    }

    @Test
    void getUnidadeId_deveRetornarIdDefinido() {
        assertEquals(1, fila.getUnidadeId());
    }

    @Test
    void setUnidadeId_deveAtualizarId() {
        fila.setUnidadeId(3);
        assertEquals(3, fila.getUnidadeId());
    }

    // ================================================================
    // getFila e setFila
    // ================================================================

    @Test
    void getFila_inicialmente_deveSerListaVazia() {
        assertNotNull(fila.getFila());
        assertTrue(fila.getFila().isEmpty());
    }

    @Test
    void setFila_deveAtualizarLista() {
        List<Reserva> novaFila = new ArrayList<>();
        novaFila.add(criarReserva(1, 1, Reserva.Status.AGUARDANDO));

        fila.setFila(novaFila);

        assertEquals(1, fila.getFila().size());
    }

    // ================================================================
    // livro e unidade
    // ================================================================

    @Test
    void getLivro_inicialmente_deveSerNulo() {
        assertNull(fila.getLivro());
    }

    @Test
    void setLivro_deveAtualizarLivroELivroId() {
        Livro livro = new Livro();
        livro.setId(7);
        livro.setTitulo("Clean Code");

        fila.setLivro(livro);

        assertEquals(livro, fila.getLivro());
        assertEquals(7, fila.getLivroId(),
                "LivroId deve ser sincronizado ao setar livro");
    }

    @Test
    void getUnidade_inicialmente_deveSerNulo() {
        assertNull(fila.getUnidade());
    }

    @Test
    void setUnidade_deveAtualizarUnidadeEUnidadeId() {
        Unidade unidade = new Unidade();
        unidade.setId(4);
        unidade.setNome("Unidade Norte");

        fila.setUnidade(unidade);

        assertEquals(unidade, fila.getUnidade());
        assertEquals(4, fila.getUnidadeId(),
                "UnidadeId deve ser sincronizado ao setar unidade");
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterLivroIdUnidadeIdETotalAguardando() {
        String str = fila.toString();

        assertTrue(str.contains("1"),   "Deve conter livroId");
        assertTrue(str.contains("0"),   "Deve conter totalAguardando");
    }
}