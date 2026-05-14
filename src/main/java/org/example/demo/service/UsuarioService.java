package org.example.demo.service;

import org.example.demo.dao.UsuarioDAO;
import org.example.demo.model.*;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean cadastrar(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        if (usuario.getSenhaHash() == null || usuario.getSenhaHash().isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }
        if (usuario.getTipo() == null) {
            throw new IllegalArgumentException("Tipo de usuário é obrigatório.");
        }

        Usuario existente = usuarioDAO.buscarPorEmail(usuario.getEmail());
        if (existente != null) {
            throw new IllegalArgumentException("E-mail já cadastrado. Use outro e-mail ou faça login.");
        }

        switch (usuario.getTipo()) {
            case ESTUDANTE     -> validarEstudante(usuario);
            case BIBLIOTECARIO -> validarBibliotecario(usuario);
            default            -> {}
        }

        return usuarioDAO.inserir(usuario);
    }

    private void validarEstudante(Usuario usuario) {
        if (!(usuario instanceof UsuarioEstudante estudante)) {
            throw new IllegalArgumentException("Tipo ESTUDANTE requer instância de UsuarioEstudante.");
        }
        if (estudante.getRa() == 0) {
            throw new IllegalArgumentException("RA é obrigatório para Estudante.");
        }
    }

    private void validarBibliotecario(Usuario usuario) {
        if (!(usuario instanceof UsuarioBibliotecario bibliotecario)) {
            throw new IllegalArgumentException("Tipo BIBLIOTECARIO requer instância de UsuarioBibliotecario.");
        }
        if (bibliotecario.getUnidadeId() == 0) {
            throw new IllegalArgumentException("Unidade é obrigatória para Bibliotecário.");
        }
    }

    public Usuario buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser vazio.");
        }
        return usuarioDAO.buscarPorEmail(email);
    }

    public Usuario buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return usuarioDAO.buscarPorId(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    // ✅ Lista todos incluindo inativos — para admin ver histórico
    public List<Usuario> listarTodosIncluindoInativos() {
        return usuarioDAO.listarTodosIncluindoInativos();
    }

    public boolean atualizar(Usuario usuario) {
        if (usuario == null || usuario.getId() <= 0) {
            throw new IllegalArgumentException("Usuário inválido para atualização.");
        }
        return usuarioDAO.atualizar(usuario);
    }

    public boolean bloquear(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        Usuario usuario = buscarPorId(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        return usuarioDAO.atualizarBloqueio(id, true, usuario.getTentativasLogin());
    }

    public boolean desbloquear(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        Usuario usuario = buscarPorId(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        return usuarioDAO.atualizarBloqueio(id, false, 0);
    }

    // ✅ Desativar em vez de deletar
    public boolean desativar(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        Usuario usuario = buscarPorId(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        return usuarioDAO.desativar(id);
    }

    // ✅ Reativar usuário
    public boolean reativar(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        return usuarioDAO.reativar(id);
    }

    // ✅ Mantido para compatibilidade — agora chama desativar
    public boolean deletar(int id) {
        return desativar(id);
    }
}