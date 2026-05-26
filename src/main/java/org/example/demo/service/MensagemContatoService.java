package org.example.demo.service;

import org.example.demo.dao.MensagemContatoDAO;
import org.example.demo.model.MensagemContato;

import java.util.List;

public class MensagemContatoService {

    private final MensagemContatoDAO mensagemDAO;

    public MensagemContatoService() {
        this.mensagemDAO = new MensagemContatoDAO();
    }

    public MensagemContatoService(MensagemContatoDAO mensagemDAO) {
        this.mensagemDAO = mensagemDAO;
    }

    public boolean enviar(Integer usuarioId, String nome, String email,
                          String assunto, String mensagem) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome é obrigatório.");
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("E-mail é obrigatório.");
        if (mensagem == null || mensagem.isBlank())
            throw new IllegalArgumentException("Mensagem é obrigatória.");

        MensagemContato m = new MensagemContato(
                usuarioId, nome, email, assunto, mensagem);
        return mensagemDAO.inserir(m);
    }

    public List<MensagemContato> listarTodas() {
        return mensagemDAO.listarTodas();
    }

    public List<MensagemContato> listarNaoLidas() {
        return mensagemDAO.listarNaoLidas();
    }

    public boolean marcarComoLida(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("ID inválido.");
        return mensagemDAO.marcarComoLida(id);
    }

    public int contarNaoLidas() {
        return mensagemDAO.contarNaoLidas();
    }
}