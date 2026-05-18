package org.example.demo.service;

import org.example.demo.dao.EmprestimoDAO;
import org.example.demo.dao.UsuarioDAO;
import org.example.demo.model.*;

import java.time.LocalDate;
import java.util.List;

public class EmprestimoService {

    private final EmprestimoDAO emprestimoDAO;
    private final ExemplarService exemplarService;
    private final MultaService multaService;
    private final ReservaService reservaService;
    private final UsuarioDAO usuarioDAO;

    public EmprestimoService() {
        this.emprestimoDAO = new EmprestimoDAO();
        this.exemplarService = new ExemplarService();
        this.multaService = new MultaService();
        this.reservaService = new ReservaService();
        this.usuarioDAO = new UsuarioDAO();
    }

    public EmprestimoService(EmprestimoDAO emprestimoDAO, ExemplarService exemplarService,
                             UsuarioDAO usuarioDAO, MultaService multaService,
                             ReservaService reservaService) {
        this.emprestimoDAO = emprestimoDAO;
        this.exemplarService = exemplarService;
        this.usuarioDAO = usuarioDAO;
        this.multaService = multaService;
        this.reservaService = reservaService;
    }

    public boolean realizarEmprestimo(int exemplarId, int usuarioId) {
        if (exemplarId <= 0 || usuarioId <= 0) {
            throw new IllegalArgumentException("IDs inválidos.");
        }

        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        if (multaService.usuarioPossuiMultaPendente(usuarioId)) {
            throw new IllegalStateException("Usuário possui multa pendente.");
        }

        int emprestimosAtivos = usuarioDAO.contarEmprestimosAtivos(usuarioId);
        if (emprestimosAtivos >= usuario.getLimiteCotas()) {
            throw new IllegalStateException("Limite de empréstimos atingido. "
                    + "Máximo: " + usuario.getLimiteCotas());
        }

        Exemplar exemplar = exemplarService.buscarPorId(exemplarId);
        if (exemplar == null) {
            throw new IllegalArgumentException("Exemplar não encontrado.");
        }
        if (exemplar.getStatus() != Exemplar.Status.DISPONIVEL) {
            throw new IllegalStateException("Exemplar não está disponível.");
        }

        LocalDate hoje = LocalDate.now();
        LocalDate dataPrevista = hoje.plusDays(usuario.getPrazoEmprestimo());

        Emprestimo emprestimo = new Emprestimo(
                exemplarId,
                usuarioId,
                hoje,
                dataPrevista
        );

        return emprestimoDAO.inserir(emprestimo);
    }

    public boolean finalizarEmprestimo(int emprestimoId) {
        if (emprestimoId <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Emprestimo emprestimo = emprestimoDAO.buscarPorId(emprestimoId);
        if (emprestimo == null) {
            throw new IllegalArgumentException("Empréstimo não encontrado.");
        }
        if (emprestimo.getStatus() == Emprestimo.Status.FINALIZADO) {
            throw new IllegalStateException("Empréstimo já finalizado.");
        }

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(Emprestimo.Status.FINALIZADO);

        exemplarService.atualizarStatus(
                emprestimo.getExemplarId(),
                Exemplar.Status.DISPONIVEL
        );

        boolean resultado = emprestimoDAO.atualizar(emprestimo);

        // ✅ Passo 2 — Atende próxima reserva da fila automaticamente
        if (resultado && emprestimo.getExemplar() != null
                && emprestimo.getExemplar().getLivro() != null) {
            try {
                int livroId    = emprestimo.getExemplar().getLivro().getId();
                int unidadeId  = emprestimo.getExemplar().getUnidadeId();
                reservaService.atenderProximaReserva(livroId, unidadeId);
            } catch (Exception ignored) {
                // Sem reservas na fila — ignora silenciosamente
            }
        }

        return resultado;
    }

    public boolean verificarEAtualizarAtrasos() {
        List<Emprestimo> atrasados = emprestimoDAO.buscarAtrasados(LocalDate.now());
        for (Emprestimo emprestimo : atrasados) {
            emprestimo.setStatus(Emprestimo.Status.ATRASADO);
            emprestimoDAO.atualizar(emprestimo);

            // ✅ Passo 1 — Gera multa automaticamente se ainda não existe
            try {
                multaService.gerarMulta(emprestimo.getId());
            } catch (IllegalStateException ignored) {
                // Multa já existe — ignora silenciosamente
            }
        }
        return !atrasados.isEmpty();
    }

    public Emprestimo buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return emprestimoDAO.buscarPorId(id);
    }

    public List<Emprestimo> buscarPorUsuario(int usuarioId) {
        if (usuarioId <= 0) {
            throw new IllegalArgumentException("ID do usuário inválido.");
        }
        return emprestimoDAO.buscarPorUsuario(usuarioId);
    }

    public List<Emprestimo> buscarAtivos() {
        return emprestimoDAO.buscarPorStatus(Emprestimo.Status.ATIVO);
    }

    public List<Emprestimo> buscarAtrasados() {
        return emprestimoDAO.buscarPorStatus(Emprestimo.Status.ATRASADO);
    }
}