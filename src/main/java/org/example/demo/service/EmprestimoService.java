package org.example.demo.service;

import org.example.demo.dao.EmprestimoDAO;
import org.example.demo.model.Emprestimo;
import org.example.demo.model.Exemplar;
import org.example.demo.model.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EmprestimoService {

    private static final int DIAS_EMPRESTIMO = 7;

    // ✅ Limite por tipo de usuário
    private static final Map<String, Integer> LIMITES = Map.of(
            "ESTUDANTE",     2,
            "ADMIN",         5,
            "BIBLIOTECARIO", 5
    );

    private final EmprestimoDAO emprestimoDAO;
    private final ExemplarService exemplarService;

    public EmprestimoService() {
        this.emprestimoDAO = new EmprestimoDAO();
        this.exemplarService = new ExemplarService();
    }

    // ✅ ALTERADO — recebe Usuario inteiro para verificar o limite por tipo
    public boolean realizarEmprestimo(int exemplarId, Usuario usuario) {
        if (exemplarId <= 0 || usuario == null) {
            throw new IllegalArgumentException("Dados inválidos.");
        }

        Exemplar exemplar = exemplarService.buscarPorId(exemplarId);
        if (exemplar == null) {
            throw new IllegalArgumentException("Exemplar não encontrado.");
        }
        if (exemplar.getStatus() != Exemplar.Status.DISPONIVEL) {
            throw new IllegalStateException("Exemplar não está disponível para empréstimo.");
        }

        // Verifica limite por tipo de usuário
        int limite = LIMITES.getOrDefault(usuario.getTipo().name(), 3);
        int ativos  = emprestimoDAO.contarEmprestimosAtivos(usuario.getId());

        if (ativos >= limite) {
            throw new IllegalStateException("Limite de " + limite + " empréstimos atingido.");
        }

        LocalDate hoje = LocalDate.now();
        Emprestimo emprestimo = new Emprestimo(
                exemplarId,
                usuario.getId(),
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

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(Emprestimo.Status.FINALIZADO);

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