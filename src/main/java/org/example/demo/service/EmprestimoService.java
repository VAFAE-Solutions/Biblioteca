package org.example.demo.service;

import org.example.demo.dao.EmprestimoDAO;
import org.example.demo.model.Emprestimo;
import org.example.demo.model.Exemplar;

import java.time.LocalDate;
import java.util.List;

public class EmprestimoService {

    private static final int DIAS_EMPRESTIMO = 7; // prazo padrão

    private final EmprestimoDAO emprestimoDAO;
    private final ExemplarService exemplarService;

    public EmprestimoService() {
        this.emprestimoDAO = new EmprestimoDAO();
        this.exemplarService = new ExemplarService();
    }

    public boolean realizarEmprestimo(int exemplarId, int usuarioId) {
        if (exemplarId <= 0 || usuarioId <= 0) {
            throw new IllegalArgumentException("IDs inválidos.");
        }

        // Verifica se o exemplar está disponível
        Exemplar exemplar = exemplarService.buscarPorId(exemplarId);
        if (exemplar == null) {
            throw new IllegalArgumentException("Exemplar não encontrado.");
        }
        if (exemplar.getStatus() != Exemplar.Status.DISPONIVEL) {
            throw new IllegalStateException("Exemplar não está disponível para empréstimo.");
        }

        // Cria o empréstimo
        LocalDate hoje = LocalDate.now();
        Emprestimo emprestimo = new Emprestimo(
                exemplarId,
                usuarioId,
                hoje,
                hoje.plusDays(DIAS_EMPRESTIMO)
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

        // Registra devolução
        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(Emprestimo.Status.FINALIZADO);

        // Libera o exemplar
        exemplarService.atualizarStatus(emprestimo.getExemplarId(), Exemplar.Status.DISPONIVEL);

        return emprestimoDAO.atualizar(emprestimo);
    }

    public boolean verificarEAtualizarAtrasos() {
        List<Emprestimo> atrasados = emprestimoDAO.buscarAtrasados(LocalDate.now());
        for (Emprestimo emprestimo : atrasados) {
            emprestimo.setStatus(Emprestimo.Status.ATRASADO);
            emprestimoDAO.atualizar(emprestimo);
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