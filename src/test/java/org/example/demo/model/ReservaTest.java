package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    private Reserva reserva;
    private static final LocalDate HOJE = LocalDate.now();

    @BeforeEach
    void setUp() {
        reserva = new Reserva(1, 1, 1, HOJE);
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_simples_deveDefinirValoresCorretamente() {
        Reserva r = new Reserva(1, 2, 3, HOJE);

        assertEquals(1, r.getLivroId());
        assertEquals(2, r.getUsuarioId());
        assertEquals(3, r.getUnidadeId());
        assertEquals(HOJE, r.getDataReserva());
        assertEquals(Reserva.Status.AGUARDANDO, r.getStatus(),
                "Status padrao deve ser AGUARDANDO");
    }

    @Test
    void construtor_completo_deveDefinirTodosOsCampos() {
        Reserva r = new Reserva(
                10, 1, 2, 3, HOJE, Reserva.Status.ATENDIDA, 2);

        assertEquals(10, r.getId());
        assertEquals(1, r.getLivroId());
        assertEquals(2, r.getUsuarioId());
        assertEquals(3, r.getUnidadeId());
        assertEquals(HOJE, r.getDataReserva());
        assertEquals(Reserva.Status.ATENDIDA, r.getStatus());
        assertEquals(2, r.getPosicaoFila());
    }

    @Test
    void construtor_vazio_devePermitirCriacao() {
        Reserva r = new Reserva();

        assertNotNull(r);
        assertEquals(Reserva.Status.AGUARDANDO, r.getStatus(),
                "Status padrao deve ser AGUARDANDO");
    }

    // ================================================================
    // id
    // ================================================================

    @Test
    void getId_deveRetornarIdDefinido() {
        reserva.setId(42);
        assertEquals(42, reserva.getId());
    }

    @Test
    void setId_deveAtualizarId() {
        reserva.setId(99);
        assertEquals(99, reserva.getId());
    }

    // ================================================================
    // livroId
    // ================================================================

    @Test
    void getLivroId_deveRetornarIdDefinido() {
        assertEquals(1, reserva.getLivroId());
    }

    @Test
    void setLivroId_deveAtualizarId() {
        reserva.setLivroId(5);
        assertEquals(5, reserva.getLivroId());
    }

    // ================================================================
    // usuarioId
    // ================================================================

    @Test
    void getUsuarioId_deveRetornarIdDefinido() {
        assertEquals(1, reserva.getUsuarioId());
    }

    @Test
    void setUsuarioId_deveAtualizarId() {
        reserva.setUsuarioId(7);
        assertEquals(7, reserva.getUsuarioId());
    }

    // ================================================================
    // unidadeId
    // ================================================================

    @Test
    void getUnidadeId_deveRetornarIdDefinido() {
        assertEquals(1, reserva.getUnidadeId());
    }

    @Test
    void setUnidadeId_deveAtualizarId() {
        reserva.setUnidadeId(3);
        assertEquals(3, reserva.getUnidadeId());
    }

    // ================================================================
    // dataReserva
    // ================================================================

    @Test
    void getDataReserva_deveRetornarDataDefinida() {
        assertEquals(HOJE, reserva.getDataReserva());
    }

    @Test
    void setDataReserva_deveAtualizarData() {
        LocalDate amanha = LocalDate.now().plusDays(1);
        reserva.setDataReserva(amanha);
        assertEquals(amanha, reserva.getDataReserva());
    }

    // ================================================================
    // status
    // ================================================================

    @Test
    void getStatus_inicialmente_deveSerAguardando() {
        assertEquals(Reserva.Status.AGUARDANDO, reserva.getStatus());
    }

    @Test
    void setStatus_paraAtendida_deveAtualizar() {
        reserva.setStatus(Reserva.Status.ATENDIDA);
        assertEquals(Reserva.Status.ATENDIDA, reserva.getStatus());
    }

    @Test
    void setStatus_paraCancelada_deveAtualizar() {
        reserva.setStatus(Reserva.Status.CANCELADA);
        assertEquals(Reserva.Status.CANCELADA, reserva.getStatus());
    }

    @Test
    void status_deveConterTresValores() {
        assertEquals(3, Reserva.Status.values().length,
                "Status deve ter AGUARDANDO, ATENDIDA e CANCELADA");
    }

    // ================================================================
    // posicaoFila
    // ================================================================

    @Test
    void getPosicaoFila_inicialmente_deveSerZero() {
        assertEquals(0, reserva.getPosicaoFila());
    }

    @Test
    void setPosicaoFila_deveAtualizarPosicao() {
        reserva.setPosicaoFila(3);
        assertEquals(3, reserva.getPosicaoFila());
    }

    @Test
    void setPosicaoFila_primeiroDaFila_deveSer1() {
        reserva.setPosicaoFila(1);
        assertEquals(1, reserva.getPosicaoFila());
    }

    // ================================================================
    // livro
    // ================================================================

    @Test
    void getLivro_inicialmente_deveSerNulo() {
        assertNull(reserva.getLivro());
    }

    @Test
    void setLivro_deveAtualizarLivroELivroId() {
        Livro livro = new Livro();
        livro.setId(5);
        livro.setTitulo("Clean Code");

        reserva.setLivro(livro);

        assertEquals(livro, reserva.getLivro());
        assertEquals(5, reserva.getLivroId(),
                "LivroId deve ser sincronizado ao setar livro");
    }

    // ================================================================
    // usuario
    // ================================================================

    @Test
    void getUsuario_inicialmente_deveSerNulo() {
        assertNull(reserva.getUsuario());
    }

    @Test
    void setUsuario_deveAtualizarUsuarioEUsuarioId() {
        Usuario usuario = new Usuario();
        usuario.setId(3);
        usuario.setNome("Carlos Lima");

        reserva.setUsuario(usuario);

        assertEquals(usuario, reserva.getUsuario());
        assertEquals(3, reserva.getUsuarioId(),
                "UsuarioId deve ser sincronizado ao setar usuario");
    }

    // ================================================================
    // unidade
    // ================================================================

    @Test
    void getUnidade_inicialmente_deveSerNulo() {
        assertNull(reserva.getUnidade());
    }

    @Test
    void setUnidade_deveAtualizarUnidadeEUnidadeId() {
        Unidade unidade = new Unidade();
        unidade.setId(4);
        unidade.setNome("Unidade Norte");

        reserva.setUnidade(unidade);

        assertEquals(unidade, reserva.getUnidade());
        assertEquals(4, reserva.getUnidadeId(),
                "UnidadeId deve ser sincronizado ao setar unidade");
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterCamposEssenciais() {
        reserva.setId(1);

        String str = reserva.toString();

        assertTrue(str.contains("1"),          "Deve conter id");
        assertTrue(str.contains("AGUARDANDO"), "Deve conter status");
    }
}