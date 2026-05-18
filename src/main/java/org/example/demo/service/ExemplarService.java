package org.example.demo.service;

import org.example.demo.dao.ExemplarDAO;
import org.example.demo.model.Exemplar;

import java.util.List;

public class ExemplarService {

    private final ExemplarDAO exemplarDAO;

    public ExemplarService() {
        this.exemplarDAO = new ExemplarDAO();
    }

    public ExemplarService(ExemplarDAO exemplarDAO) {
        this.exemplarDAO = exemplarDAO;
    }

    public boolean cadastrar(Exemplar exemplar) {
        if (exemplar == null) {
            throw new IllegalArgumentException("Exemplar não pode ser nulo.");
        }
        if (exemplar.getCodigoPatrimonio() == null || exemplar.getCodigoPatrimonio().isBlank()) {
            throw new IllegalArgumentException("Código de patrimônio é obrigatório.");
        }
        if (exemplar.getLivroId() <= 0) {
            throw new IllegalArgumentException("Livro é obrigatório.");
        }
        if (exemplar.getUnidadeId() <= 0) {
            throw new IllegalArgumentException("Unidade é obrigatória.");
        }
        return exemplarDAO.inserir(exemplar);
    }

    public Exemplar buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return exemplarDAO.buscarPorId(id);
    }

    public List<Exemplar> buscarPorLivro(int livroId) {
        if (livroId <= 0) {
            throw new IllegalArgumentException("ID do livro inválido.");
        }
        return exemplarDAO.buscarPorLivro(livroId);
    }

    public List<Exemplar> buscarDisponiveisPorLivroEUnidade(int livroId, int unidadeId) {
        if (livroId <= 0 || unidadeId <= 0) {
            throw new IllegalArgumentException("IDs inválidos.");
        }
        return exemplarDAO.buscarDisponiveisPorLivroEUnidade(livroId, unidadeId);
    }

    public boolean atualizarStatus(int exemplarId, Exemplar.Status novoStatus) {
        if (exemplarId <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        if (novoStatus == null) {
            throw new IllegalArgumentException("Status não pode ser nulo.");
        }
        Exemplar exemplar = exemplarDAO.buscarPorId(exemplarId);
        if (exemplar == null) {
            throw new IllegalArgumentException("Exemplar não encontrado.");
        }
        exemplar.setStatus(novoStatus);
        return exemplarDAO.atualizar(exemplar);
    }

    // ✅ Desativar em vez de deletar
    public boolean desativar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        Exemplar exemplar = exemplarDAO.buscarPorId(id);
        if (exemplar == null) {
            throw new IllegalArgumentException("Exemplar não encontrado.");
        }
        return exemplarDAO.desativar(id);
    }
}
