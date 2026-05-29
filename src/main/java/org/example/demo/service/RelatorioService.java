package org.example.demo.service;

import org.example.demo.dao.RelatorioDAO;

import java.util.List;
import java.util.Map;

public class RelatorioService {

    private final RelatorioDAO relatorioDAO;

    public RelatorioService() {
        this.relatorioDAO = new RelatorioDAO();
    }

    public RelatorioService(RelatorioDAO relatorioDAO) {
        this.relatorioDAO = relatorioDAO;
    }

    public List<Map<String, Object>> livrosMaisEmprestados() {
        return relatorioDAO.livrosMaisEmprestados(10);
    }

    public Map<String, Object> taxaAtraso() {
        return relatorioDAO.taxaAtraso();
    }

    public List<Map<String, Object>> emprestimosPorUsuario() {
        return relatorioDAO.emprestimosPorUsuario(10);
    }

    public List<Map<String, Object>> livrosAtrasados() {
        return relatorioDAO.livrosAtrasados();
    }

    public Map<String, Object> resumoMultas() {
        return relatorioDAO.resumoMultas();
    }
}