package org.example.demo.service;

import org.example.demo.dao.EmprestimoDAO;
import org.example.demo.dao.MultaDAO;
import org.example.demo.model.Emprestimo;
import org.example.demo.model.Multa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MultaServiceTest {

    @Mock private MultaDAO multaDAO;
    @Mock private EmprestimoDAO emprestimoDAO;

    @InjectMocks
    private MultaService multaService;

    private Emprestimo emprestimoAtrasado;
    private Multa multaPendente;

    @BeforeEach
    void setUp() {
        // ✅ Emprestimo atrasado 5 dias — multa esperada: 5 * 2.00 = R$10,00
        emprestimoAtrasado = new Emprestimo();
        emprestimoAtrasado.setId(1);
        emprestimoAtrasado.setUsuarioId(1);
        emprestimoAtrasado.setStatus(Emprestimo.Status.ATRASADO);
        emprestimoAtrasado.setDataDevolucaoPrevista(LocalDate.now().minusDays(5));

        multaPendente = new Multa();
        multaPendente.setId(1);
        multaPendente.setEmprestimoId(1);
        multaPendente.setUsuarioId(1);
        multaPendente.setValor(new BigDecimal("10.00"));
        multaPendente.setPago(false);
        multaPendente.setDataGeracao(LocalDate.now());
    }

    // ================================================================
    // gerarMulta — validacoes de ID
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void gerarMulta_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.gerarMulta(id));
    }

    @Test
    void gerarMulta_emprestimoNaoEncontrado_deveLancarExcecao() {
        when(emprestimoDAO.buscarPorId(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> multaService.gerarMulta(99));
    }

    @Test
    void gerarMulta_emprestimoNaoAtrasado_deveLancarExcecao() {
        Emprestimo ativo = new Emprestimo();
        ativo.setId(1);
        ativo.setStatus(Emprestimo.Status.ATIVO);

        when(emprestimoDAO.buscarPorId(1)).thenReturn(ativo);

        assertThrows(IllegalStateException.class,
                () -> multaService.gerarMulta(1),
                "Multa so pode ser gerada para emprestimos atrasados");
    }

    @Test
    void gerarMulta_multaJaExiste_deveLancarExcecao() {
        when(emprestimoDAO.buscarPorId(1)).thenReturn(emprestimoAtrasado);
        when(multaDAO.existeMultaPorEmprestimo(1)).thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> multaService.gerarMulta(1),
                "Nao deve gerar multa duplicada para o mesmo emprestimo");
    }

    @Test
    void gerarMulta_5DiasAtraso_deveCalcularR$10() {
        // ✅ Documentado: R$2,00 por dia de atraso
        when(emprestimoDAO.buscarPorId(1)).thenReturn(emprestimoAtrasado);
        when(multaDAO.existeMultaPorEmprestimo(1)).thenReturn(false);
        when(multaDAO.inserir(any())).thenAnswer(inv -> {
            Multa multa = inv.getArgument(0);
            assertEquals(new BigDecimal("10.00"), multa.getValor(),
                    "5 dias * R$2,00 = R$10,00");
            return true;
        });

        multaService.gerarMulta(1);

        verify(multaDAO).inserir(any());
    }

    @Test
    void gerarMulta_1DiaAtraso_deveCalcularR$2() {
        emprestimoAtrasado.setDataDevolucaoPrevista(LocalDate.now().minusDays(1));

        when(emprestimoDAO.buscarPorId(1)).thenReturn(emprestimoAtrasado);
        when(multaDAO.existeMultaPorEmprestimo(1)).thenReturn(false);
        when(multaDAO.inserir(any())).thenAnswer(inv -> {
            Multa multa = inv.getArgument(0);
            assertEquals(new BigDecimal("2.00"), multa.getValor(),
                    "1 dia * R$2,00 = R$2,00");
            return true;
        });

        multaService.gerarMulta(1);

        verify(multaDAO).inserir(any());
    }

    @Test
    void gerarMulta_10DiasAtraso_deveCalcularR$20() {
        emprestimoAtrasado.setDataDevolucaoPrevista(LocalDate.now().minusDays(10));

        when(emprestimoDAO.buscarPorId(1)).thenReturn(emprestimoAtrasado);
        when(multaDAO.existeMultaPorEmprestimo(1)).thenReturn(false);
        when(multaDAO.inserir(any())).thenAnswer(inv -> {
            Multa multa = inv.getArgument(0);
            assertEquals(new BigDecimal("20.00"), multa.getValor(),
                    "10 dias * R$2,00 = R$20,00");
            return true;
        });

        multaService.gerarMulta(1);
    }

    // ================================================================
    // registrarPagamento — validacoes e regras
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void registrarPagamento_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.registrarPagamento(id));
    }

    @Test
    void registrarPagamento_multaNaoEncontrada_deveLancarExcecao() {
        when(multaDAO.buscarPorId(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> multaService.registrarPagamento(99));
    }

    @Test
    void registrarPagamento_multaJaPaga_deveLancarExcecao() {
        multaPendente.setPago(true);
        multaPendente.setDataPagamento(LocalDate.now());

        when(multaDAO.buscarPorId(1)).thenReturn(multaPendente);

        assertThrows(IllegalStateException.class,
                () -> multaService.registrarPagamento(1),
                "Multa ja paga nao deve ser paga novamente");
    }

    @Test
    void registrarPagamento_multaPendente_deveMarcarComoPaga() {
        when(multaDAO.buscarPorId(1)).thenReturn(multaPendente);
        when(multaDAO.atualizar(multaPendente)).thenReturn(true);

        boolean resultado = multaService.registrarPagamento(1);

        assertTrue(resultado);
        assertTrue(multaPendente.isPago());
        assertEquals(LocalDate.now(), multaPendente.getDataPagamento());
        verify(multaDAO).atualizar(multaPendente);
    }

    // ================================================================
    // buscarPorUsuario — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarPorUsuario_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.buscarPorUsuario(id));
    }

    @Test
    void buscarPorUsuario_comIdValido_deveRetornarLista() {
        when(multaDAO.buscarPorUsuario(1)).thenReturn(List.of(multaPendente));

        var resultado = multaService.buscarPorUsuario(1);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(multaDAO).buscarPorUsuario(1);
    }

    // ================================================================
    // buscarPendentes — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarPendentes_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.buscarPendentes(id));
    }

    @Test
    void buscarPendentes_comIdValido_deveRetornarApenasNaoPagas() {
        when(multaDAO.buscarPendentesPorUsuario(1)).thenReturn(List.of(multaPendente));

        var resultado = multaService.buscarPendentes(1);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertFalse(resultado.getFirst().isPago());
    }

    @Test
    void buscarPendentes_semMultas_deveRetornarListaVazia() {
        when(multaDAO.buscarPendentesPorUsuario(1)).thenReturn(List.of());

        var resultado = multaService.buscarPendentes(1);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ================================================================
    // calcularTotalMultasPendentes — validacoes e calculo
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void calcularTotalMultasPendentes_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.calcularTotalMultasPendentes(id));
    }

    @Test
    void calcularTotalMultasPendentes_comMultasDe10E20_deveRetornar30() {
        Multa multa1 = new Multa();
        multa1.setValor(new BigDecimal("10.00"));
        multa1.setPago(false);

        Multa multa2 = new Multa();
        multa2.setValor(new BigDecimal("20.00"));
        multa2.setPago(false);

        when(multaDAO.buscarPendentesPorUsuario(1)).thenReturn(List.of(multa1, multa2));

        double total = multaService.calcularTotalMultasPendentes(1);

        assertEquals(30.0, total, 0.001,
                "Total de multas pendentes deve ser R$30,00");
    }

    @Test
    void calcularTotalMultasPendentes_semMultas_deveRetornarZero() {
        when(multaDAO.buscarPendentesPorUsuario(1)).thenReturn(List.of());

        double total = multaService.calcularTotalMultasPendentes(1);

        assertEquals(0.0, total, 0.001,
                "Sem multas pendentes o total deve ser zero");
    }

    // ================================================================
    // usuarioPossuiMultaPendente — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void usuarioPossuiMultaPendente_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> multaService.usuarioPossuiMultaPendente(id));
    }

    @Test
    void usuarioPossuiMultaPendente_comMulta_deveRetornarTrue() {
        when(multaDAO.buscarPendentesPorUsuario(1)).thenReturn(List.of(multaPendente));

        assertTrue(multaService.usuarioPossuiMultaPendente(1));
    }

    @Test
    void usuarioPossuiMultaPendente_semMulta_deveRetornarFalse() {
        when(multaDAO.buscarPendentesPorUsuario(1)).thenReturn(List.of());

        assertFalse(multaService.usuarioPossuiMultaPendente(1));
    }
}