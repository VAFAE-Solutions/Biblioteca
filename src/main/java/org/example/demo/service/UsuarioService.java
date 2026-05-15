package org.example.demo.service;

import org.example.demo.dao.UsuarioDAO;
import org.example.demo.model.*;
import org.example.demo.service.AutenticacaoService;

import java.util.List;
import java.util.UUID;

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

    public List<Usuario> listarTodosIncluindoInativos() {
        return usuarioDAO.listarTodosIncluindoInativos();
    }

    // ✅ Lista usuários bloqueados ou com solicitação de reset
    public List<Usuario> listarBloqueadosOuComReset() {
        return usuarioDAO.listarBloqueadosOuComReset();
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

    public boolean desativar(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        Usuario usuario = buscarPorId(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        return usuarioDAO.desativar(id);
    }

    public boolean reativar(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        return usuarioDAO.reativar(id);
    }

    // ✅ Solicitar reset de senha — marca no banco e bloqueia
    public boolean solicitarReset(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("E-mail não pode ser vazio.");
        }
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null) {
            throw new IllegalArgumentException("E-mail não encontrado.");
        }
        return usuarioDAO.solicitarReset(usuario.getId());
    }

    // ✅ Aprovar reset — admin gera senha temporária
    public String aprovarReset(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");

        // Gera senha temporária aleatória de 8 caracteres
        String senhaTemporaria = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        String senhaHash = AutenticacaoService.gerarHash(senhaTemporaria);
        usuarioDAO.aprovarReset(id, senhaHash);

        // Retorna a senha em texto para o admin mostrar na tela
        return senhaTemporaria;
    }

    // ✅ Atualizar senha no perfil
    public boolean atualizarSenha(int id, String senhaAtual, String novaSenha) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        if (novaSenha == null || novaSenha.length() < 6) {
            throw new IllegalArgumentException("Nova senha deve ter ao menos 6 caracteres.");
        }

        Usuario usuario = buscarPorId(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");

        // Verifica senha atual
        String senhaAtualHash = AutenticacaoService.gerarHash(senhaAtual);
        if (!senhaAtualHash.equals(usuario.getSenhaHash())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }

        String novaSenhaHash = AutenticacaoService.gerarHash(novaSenha);
        return usuarioDAO.atualizarSenha(id, novaSenhaHash);
    }

    public boolean ajustarLimiteCotas(int id, Integer novoLimite) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        if (novoLimite != null && novoLimite <= 0) {
            throw new IllegalArgumentException("Limite deve ser maior que zero.");
        }
        if (novoLimite != null && novoLimite > 20) {
            throw new IllegalArgumentException("Limite não pode ser maior que 20.");
        }
        Usuario usuario = buscarPorId(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        if (usuario.getTipo() != Usuario.Tipo.ESTUDANTE &&
                usuario.getTipo() != Usuario.Tipo.COMUM) {
            throw new IllegalArgumentException("Ajuste de limite só é permitido para Estudantes e Usuários Comuns.");
        }
        return usuarioDAO.ajustarLimiteCotas(id, novoLimite);
    }

    public boolean resetarLimiteCotas(int id) {
        return ajustarLimiteCotas(id, null);
    }

    public boolean deletar(int id) {
        return desativar(id);
    }
}