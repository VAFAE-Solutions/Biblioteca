package org.example.demo.service;

import org.example.demo.dao.ReservaDAO;
import org.example.demo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock private ReservaDAO reservaDAO;
    @Mock private MultaService multaService;
    @Mock private ExemplarService exemplarService;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reservaAguardando;
    private FilaReserva filaVazia;
    private FilaReserva filaComReserva;

    @BeforeEach
    void setUp() {
        reservaAguardando = new Reserva();
        reservaAguardando.setId(1);
        reservaAguardando.setLivroId(1);
        reservaAguardando.setUsuarioId(1);
        reservaAguardando.setUnidadeId(1);
        reservaAguardando.setDataReserva(LocalDate.now());
        reservaAguardando.setStatus(Reserva.Status.AGUARDANDO);
        reservaAguardando.setPosicaoFila(1);

        filaVazia = new FilaReserva(1, 1);
        filaVazia.setFila(List.of());

        filaComReserva = new FilaReserva(1, 1);
        filaComReserva.setFila(List.of(reservaAguardando));
    }

    // ================================================================
    // realizarReserva — validacoes de IDs
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void realizarReserva_comLivroIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.realizarReserva(id, 1, 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void realizarReserva_comUsuarioIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.realizarReserva(1, id, 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void realizarReserva_comUnidadeIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.realizarReserva(1, 1, id));
    }

    @Test
    void realizarReserva_comTodosIdsInvalidos_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.realizarReserva(0, 0, 0));
    }

    // ================================================================
    // realizarReserva — regras de negocio (documentadas)
    // ================================================================

    @Test
    void realizarReserva_livroDisponivel_naoDevePermitirReserva() {
        Exemplar exemplarDisponivel = new Exemplar();
        exemplarDisponivel.setId(1);
        exemplarDisponivel.setStatus(Exemplar.Status.DISPONIVEL);

        when(exemplarService.buscarDisponiveisPorLivroEUnidade(1, 1))
                .thenReturn(List.of(exemplarDisponivel));

        assertThrows(IllegalStateException.class,
                () -> reservaService.realizarReserva(1, 1, 1),
                "Nao deve reservar livro disponivel");
    }

    @Test
    void realizarReserva_usuarioComMultaPendente_deveLancarExcecao() {
        when(exemplarService.buscarDisponiveisPorLivroEUnidade(1, 1))
                .thenReturn(List.of());
        when(multaService.usuarioPossuiMultaPendente(1)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> reservaService.realizarReserva(1, 1, 1));

        assertTrue(ex.getMessage().toLowerCase().contains("multa"),
                "Nao deve reservar com multa pendente");
    }

    @Test
    void realizarReserva_usuarioJaNaFila_deveLancarExcecao() {
        // ✅ Nao pode reservar o mesmo livro duas vezes
        when(exemplarService.buscarDisponiveisPorLivroEUnidade(1, 1))
                .thenReturn(List.of());
        when(multaService.usuarioPossuiMultaPendente(1)).thenReturn(false);
        when(reservaDAO.buscarFilaPorLivroEUnidade(1, 1)).thenReturn(filaComReserva);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> reservaService.realizarReserva(1, 1, 1));

        assertTrue(ex.getMessage().toLowerCase().contains("reserva") ||
                        ex.getMessage().toLowerCase().contains("fila"),
                "Nao deve reservar se ja esta na fila");
    }

    @Test
    void realizarReserva_comDadosValidos_devePosicionarNaFila() {
        when(exemplarService.buscarDisponiveisPorLivroEUnidade(1, 1))
                .thenReturn(List.of());
        when(multaService.usuarioPossuiMultaPendente(1)).thenReturn(false);
        when(reservaDAO.buscarFilaPorLivroEUnidade(1, 1)).thenReturn(filaVazia);
        when(reservaDAO.inserir(any())).thenAnswer(inv -> {
            Reserva r = inv.getArgument(0);
            assertEquals(1, r.getPosicaoFila(),
                    "Primeira reserva deve ser posicao 1");
            return true;
        });

        assertDoesNotThrow(() -> reservaService.realizarReserva(1, 1, 1));
        verify(reservaDAO).inserir(any());
    }

    @Test
    void realizarReserva_segundoNaFila_devePosicionarComoSegundo() {
        FilaReserva filaComUm = new FilaReserva(1, 1);
        filaComUm.setFila(List.of(reservaAguardando));

        // Segundo usuario diferente
        when(exemplarService.buscarDisponiveisPorLivroEUnidade(1, 1))
                .thenReturn(List.of());
        when(multaService.usuarioPossuiMultaPendente(2)).thenReturn(false);
        when(reservaDAO.buscarFilaPorLivroEUnidade(1, 1)).thenReturn(filaComUm);
        when(reservaDAO.inserir(any())).thenAnswer(inv -> {
            Reserva r = inv.getArgument(0);
            assertEquals(2, r.getPosicaoFila(),
                    "Segunda reserva deve ser posicao 2");
            return true;
        });

        assertDoesNotThrow(() -> reservaService.realizarReserva(1, 2, 1));
    }

    // ================================================================
    // cancelarReserva — validacoes e regras
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void cancelarReserva_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.cancelarReserva(id));
    }

    @Test
    void cancelarReserva_reservaNaoEncontrada_deveLancarExcecao() {
        when(reservaDAO.buscarPorId(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> reservaService.cancelarReserva(99));
    }

    @Test
    void cancelarReserva_reservaAtendida_deveLancarExcecao() {
        reservaAguardando.setStatus(Reserva.Status.ATENDIDA);
        when(reservaDAO.buscarPorId(1)).thenReturn(reservaAguardando);

        assertThrows(IllegalStateException.class,
                () -> reservaService.cancelarReserva(1),
                "Reserva ja atendida nao pode ser cancelada");
    }

    @Test
    void cancelarReserva_reservaCancelada_deveLancarExcecao() {
        reservaAguardando.setStatus(Reserva.Status.CANCELADA);
        when(reservaDAO.buscarPorId(1)).thenReturn(reservaAguardando);

        assertThrows(IllegalStateException.class,
                () -> reservaService.cancelarReserva(1),
                "Reserva ja cancelada nao pode ser cancelada novamente");
    }

    @Test
    void cancelarReserva_aguardando_deveCancelarComSucesso() {
        when(reservaDAO.buscarPorId(1)).thenReturn(reservaAguardando);
        when(reservaDAO.atualizar(reservaAguardando)).thenReturn(true);

        boolean resultado = reservaService.cancelarReserva(1);

        assertTrue(resultado);
        assertEquals(Reserva.Status.CANCELADA, reservaAguardando.getStatus());
        verify(reservaDAO).atualizar(reservaAguardando);
    }

    // ================================================================
    // atenderProximaReserva
    // ================================================================

    @Test
    void atenderProximaReserva_filaVazia_deveRetornarFalse() {
        when(reservaDAO.buscarFilaPorLivroEUnidade(1, 1)).thenReturn(filaVazia);

        boolean resultado = reservaService.atenderProximaReserva(1, 1);

        assertFalse(resultado, "Fila vazia nao deve atender ninguem");
    }

    @Test
    void atenderProximaReserva_comReserva_deveAtenderPrimeiroDaFila() {
        when(reservaDAO.buscarFilaPorLivroEUnidade(1, 1)).thenReturn(filaComReserva);
        when(reservaDAO.atualizar(reservaAguardando)).thenReturn(true);

        boolean resultado = reservaService.atenderProximaReserva(1, 1);

        assertTrue(resultado);
        assertEquals(Reserva.Status.ATENDIDA, reservaAguardando.getStatus());
        verify(reservaDAO).atualizar(reservaAguardando);
    }

    // ================================================================
    // buscarFila — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarFila_comLivroIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.buscarFila(id, 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarFila_comUnidadeIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.buscarFila(1, id));
    }

    @Test
    void buscarFila_comDadosValidos_deveRetornarFila() {
        when(reservaDAO.buscarFilaPorLivroEUnidade(1, 1)).thenReturn(filaComReserva);

        FilaReserva resultado = reservaService.buscarFila(1, 1);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(reservaDAO).buscarFilaPorLivroEUnidade(1, 1);
    }

    // ================================================================
    // buscarPorUsuario — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarPorUsuario_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.buscarPorUsuario(id));
    }

    @Test
    void buscarPorUsuario_comIdValido_deveRetornarLista() {
        when(reservaDAO.buscarPorUsuario(1)).thenReturn(List.of(reservaAguardando));

        var resultado = reservaService.buscarPorUsuario(1);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(reservaDAO).buscarPorUsuario(1);
    }

    // ================================================================
    // buscarFilaCompleta
    // ================================================================

    @Test
    void buscarFilaCompleta_comUnidadeIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.buscarFilaCompleta(-1));
    }

    @Test
    void buscarFilaCompleta_comIdValido_deveRetornarLista() {
        when(reservaDAO.buscarAguardandoPorUnidade(1))
                .thenReturn(List.of(reservaAguardando));

        var resultado = reservaService.buscarFilaCompleta(1);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(reservaDAO).buscarAguardandoPorUnidade(1);
    }
}