package org.example.demo.service;

import org.example.demo.dao.UnidadeDAO;
import org.example.demo.model.Unidade;

import java.util.List;

public class UnidadeService {

    private final UnidadeDAO unidadeDAO;

    public UnidadeService() {
        this.unidadeDAO = new UnidadeDAO();
    }

    public boolean cadastrar(Unidade unidade) {
        if (unidade == null) {
            throw new IllegalArgumentException("Unidade não pode ser nula.");
        }
        if (unidade.getNome() == null || unidade.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da unidade é obrigatório.");
        }
        if (unidade.getEndereco() == null || unidade.getEndereco().isBlank()) {
            throw new IllegalArgumentException("Endereço da unidade é obrigatório.");
        }
        return unidadeDAO.inserir(unidade);
    }

    public Unidade buscarPorId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        return unidadeDAO.buscarPorId(id);
    }

    public List<Unidade> listarTodas() {
        return unidadeDAO.listarTodas();
    }

    public boolean atualizar(Unidade unidade) {
        if (unidade == null || unidade.getId() <= 0) {
            throw new IllegalArgumentException("Unidade inválida para atualização.");
        }
        if (unidade.getNome() == null || unidade.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da unidade é obrigatório.");
        }
        return unidadeDAO.atualizar(unidade);
    }

    // ✅ Desativar em vez de deletar
    public boolean desativar(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        return unidadeDAO.desativar(id);
    }

    // ✅ Mantido para compatibilidade
    public boolean deletar(int id) {
        return desativar(id);
    }
}