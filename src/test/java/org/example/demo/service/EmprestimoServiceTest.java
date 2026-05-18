package org.example.demo.service;

import org.example.demo.dao.EmprestimoDAO;
import org.example.demo.dao.UsuarioDAO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock private EmprestimoDAO emprestimoDAO;
    @Mock private UsuarioDAO usuarioDAO;
    @Mock private MultaService multaService;
    @Mock private ExemplarService exemplarService;

    @InjectMocks
    private EmprestimoService emprestimoService;

    private Usuario usuarioComum;
    private Usuario usuarioEstudante;
    private Exemplar exemplarDisponivel;

    @BeforeEach
    void setUp() {
        // ✅ Usuario COMUM — prazo 7 dias, limite 3
        usuarioComum = new Usuario();
        usuarioComum.setId(1);
        usuarioComum.setNome("Carlos Lima");
        usuarioComum.setTipo(Usuario.Tipo.COMUM);

        // ✅ Usuario ESTUDANTE — prazo 15 dias, limite 5
        usuarioEstudante = new UsuarioEstudante();
        usuarioEstudante.setId(2);
        usuarioEstudante.setNome("Lucas Cardoso");
        usuarioEstudante.setTipo(Usuario.Tipo.ESTUDANTE);

        // ✅ Exemplar disponivel
        exemplarDisponivel = new Exemplar();
        exemplarDisponivel.setId(1);
        exemplarDisponivel.setStatus(Exemplar.Status.DISPONIVEL);
    }

    // ================================================================
    // realizarEmprestimo — validacoes de IDs
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void realizarEmprestimo_comExemplarIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.realizarEmprestimo(id, 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void realizarEmprestimo_comUsuarioIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.realizarEmprestimo(1, id));
    }

    @Test
    void realizarEmprestimo_comAmbosIdsInvalidos_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.realizarEmprestimo(0, 0));
    }

    // ================================================================
    // realizarEmprestimo — regras de negocio (documentadas)
    // ================================================================

    @Test
    void realizarEmprestimo_usuarioNaoEncontrado_deveLancarExcecao() {
        when(usuarioDAO.buscarPorId(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.realizarEmprestimo(1, 1));
    }

    @Test
    void realizarEmprestimo_usuarioComMultaPendente_deveLancarExcecao() {
        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);
        when(multaService.usuarioPossuiMultaPendente(1)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> emprestimoService.realizarEmprestimo(1, 1));

        assertTrue(ex.getMessage().toLowerCase().contains("multa"),
                "Mensagem deve mencionar multa pendente");
    }

    @Test
    void realizarEmprestimo_usuarioAtingiuLimiteCotas_deveLancarExcecao() {
        // COMUM tem limite 3 — simula 3 emprestimos ativos
        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);
        when(multaService.usuarioPossuiMultaPendente(1)).thenReturn(false);
        when(usuarioDAO.contarEmprestimosAtivos(1)).thenReturn(3);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> emprestimoService.realizarEmprestimo(1, 1));

        assertTrue(ex.getMessage().toLowerCase().contains("limite"),
                "Mensagem deve mencionar limite atingido");
    }

    @Test
    void realizarEmprestimo_exemplarNaoEncontrado_deveLancarExcecao() {
        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);
        when(multaService.usuarioPossuiMultaPendente(1)).thenReturn(false);
        when(usuarioDAO.contarEmprestimosAtivos(1)).thenReturn(0);
        when(exemplarService.buscarPorId(1)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.realizarEmprestimo(1, 1));
    }

    @Test
    void realizarEmprestimo_exemplarNaoDisponivel_deveLancarExcecao() {
        Exemplar exemplarEmprestado = new Exemplar();
        exemplarEmprestado.setId(1);
        exemplarEmprestado.setStatus(Exemplar.Status.EMPRESTADO);

        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);
        when(multaService.usuarioPossuiMultaPendente(1)).thenReturn(false);
        when(usuarioDAO.contarEmprestimosAtivos(1)).thenReturn(0);
        when(exemplarService.buscarPorId(1)).thenReturn(exemplarEmprestado);

        assertThrows(IllegalStateException.class,
                () -> emprestimoService.realizarEmprestimo(1, 1));
    }

    @Test
    void realizarEmprestimo_usuarioComum_prazoDeveSer7Dias() {
        // ✅ Documentado: COMUM = 7 dias
        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);
        when(multaService.usuarioPossuiMultaPendente(1)).thenReturn(false);
        when(usuarioDAO.contarEmprestimosAtivos(1)).thenReturn(0);
        when(exemplarService.buscarPorId(1)).thenReturn(exemplarDisponivel);
        when(emprestimoDAO.inserir(any())).thenAnswer(inv -> {
            Emprestimo emp = inv.getArgument(0);
            long dias = java.time.temporal.ChronoUnit.DAYS.between(
                    emp.getDataEmprestimo(),
                    emp.getDataDevolucaoPrevista()
            );
            assertEquals(7, dias, "Prazo para COMUM deve ser 7 dias");
            return true;
        });

        emprestimoService.realizarEmprestimo(1, 1);
        verify(emprestimoDAO).inserir(any());
    }

    @Test
    void realizarEmprestimo_usuarioEstudante_prazoDeveSer15Dias() {
        // ✅ Documentado: ESTUDANTE = 15 dias
        when(usuarioDAO.buscarPorId(2)).thenReturn(usuarioEstudante);
        when(multaService.usuarioPossuiMultaPendente(2)).thenReturn(false);
        when(usuarioDAO.contarEmprestimosAtivos(2)).thenReturn(0);
        when(exemplarService.buscarPorId(1)).thenReturn(exemplarDisponivel);
        when(emprestimoDAO.inserir(any())).thenAnswer(inv -> {
            Emprestimo emp = inv.getArgument(0);
            long dias = java.time.temporal.ChronoUnit.DAYS.between(
                    emp.getDataEmprestimo(),
                    emp.getDataDevolucaoPrevista()
            );
            assertEquals(15, dias, "Prazo para ESTUDANTE deve ser 15 dias");
            return true;
        });

        emprestimoService.realizarEmprestimo(1, 2);
        verify(emprestimoDAO).inserir(any());
    }

    @Test
    void realizarEmprestimo_estudanteComLimite5_devePermitir5Emprestimos() {
        // ✅ Documentado: ESTUDANTE limite = 5
        when(usuarioDAO.buscarPorId(2)).thenReturn(usuarioEstudante);
        when(multaService.usuarioPossuiMultaPendente(2)).thenReturn(false);
        when(usuarioDAO.contarEmprestimosAtivos(2)).thenReturn(4); // 4 ativos, limite 5
        when(exemplarService.buscarPorId(1)).thenReturn(exemplarDisponivel);
        when(emprestimoDAO.inserir(any())).thenReturn(true);

        assertDoesNotThrow(() -> emprestimoService.realizarEmprestimo(1, 2),
                "Estudante com 4 emprestimos ativos ainda pode emprestar");
    }

    @Test
    void realizarEmprestimo_estudanteAtingiuLimite5_deveLancarExcecao() {
        when(usuarioDAO.buscarPorId(2)).thenReturn(usuarioEstudante);
        when(multaService.usuarioPossuiMultaPendente(2)).thenReturn(false);
        when(usuarioDAO.contarEmprestimosAtivos(2)).thenReturn(5); // limite atingido

        assertThrows(IllegalStateException.class,
                () -> emprestimoService.realizarEmprestimo(1, 2),
                "Estudante com 5 emprestimos ativos nao pode emprestar");
    }

    // ================================================================
    // finalizarEmprestimo — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void finalizarEmprestimo_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.finalizarEmprestimo(id));
    }

    @Test
    void finalizarEmprestimo_emprestimoNaoEncontrado_deveLancarExcecao() {
        when(emprestimoDAO.buscarPorId(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.finalizarEmprestimo(99));
    }

    @Test
    void finalizarEmprestimo_emprestimoJaFinalizado_deveLancarExcecao() {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(1);
        emprestimo.setStatus(Emprestimo.Status.FINALIZADO);

        when(emprestimoDAO.buscarPorId(1)).thenReturn(emprestimo);

        assertThrows(IllegalStateException.class,
                () -> emprestimoService.finalizarEmprestimo(1),
                "Emprestimo ja finalizado nao pode ser finalizado novamente");
    }

    @Test
    void finalizarEmprestimo_ativo_deveFinalizarComSucesso() {
        Livro livro = new Livro();
        livro.setId(1);

        Exemplar exemplar = new Exemplar();
        exemplar.setId(1);
        exemplar.setUnidadeId(1);
        exemplar.setLivro(livro);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(1);
        emprestimo.setExemplarId(1);
        emprestimo.setStatus(Emprestimo.Status.ATIVO);
        emprestimo.setExemplar(exemplar);

        when(emprestimoDAO.buscarPorId(1)).thenReturn(emprestimo);
        when(emprestimoDAO.atualizar(any())).thenReturn(true);
        when(exemplarService.atualizarStatus(anyInt(), any())).thenReturn(true);

        assertDoesNotThrow(() -> emprestimoService.finalizarEmprestimo(1));

        assertEquals(Emprestimo.Status.FINALIZADO, emprestimo.getStatus());
        assertNotNull(emprestimo.getDataDevolucao());
        assertEquals(LocalDate.now(), emprestimo.getDataDevolucao());
    }

    // ================================================================
    // buscarPorId e buscarPorUsuario — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void buscarPorId_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.buscarPorId(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void buscarPorUsuario_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> emprestimoService.buscarPorUsuario(id));
    }

    // ================================================================
    // verificarEAtualizarAtrasos
    // ================================================================

    @Test
    void verificarEAtualizarAtrasos_semEmprestimosAtrasados_deveRetornarFalse() {
        when(emprestimoDAO.buscarAtrasados(any())).thenReturn(java.util.List.of());

        boolean resultado = emprestimoService.verificarEAtualizarAtrasos();

        assertFalse(resultado, "Sem atrasados deve retornar false");
    }

    @Test
    void verificarEAtualizarAtrasos_comEmprestimosAtrasados_deveGerarMulta() {
        Emprestimo atrasado = new Emprestimo();
        atrasado.setId(1);
        atrasado.setUsuarioId(1);
        atrasado.setStatus(Emprestimo.Status.ATIVO);
        atrasado.setDataDevolucaoPrevista(LocalDate.now().minusDays(3));

        when(emprestimoDAO.buscarAtrasados(any())).thenReturn(java.util.List.of(atrasado));
        when(emprestimoDAO.atualizar(any())).thenReturn(true);

        boolean resultado = emprestimoService.verificarEAtualizarAtrasos();

        assertTrue(resultado, "Com atrasados deve retornar true");
        assertEquals(Emprestimo.Status.ATRASADO, atrasado.getStatus());
        verify(multaService).gerarMulta(1);
    }
}