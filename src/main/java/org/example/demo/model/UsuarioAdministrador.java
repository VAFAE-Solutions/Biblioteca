package org.example.demo.model;

import java.time.LocalDateTime;

public class UsuarioAdministrador extends Usuario {

    // Construtor vazio — usado pelo DAO
    public UsuarioAdministrador() {
        super();
        setTipo(Tipo.ADMIN);
    }

    // Construtor mínimo — cadastro
    public UsuarioAdministrador(String nome, String email, String senhaHash) {
        super(nome, email, senhaHash, Tipo.ADMIN);
    }

    // Construtor completo — usado pelo DAO ao reconstruir do banco
    public UsuarioAdministrador(int id, String nome, String email, String senhaHash,
                                String telefone, boolean bloqueado, int tentativasLogin,
                                LocalDateTime ultimaTentativa, LocalDateTime createdAt,
                                LocalDateTime updatedAt) {
        super(id, nome, email, senhaHash, Tipo.ADMIN, telefone,
                bloqueado, tentativasLogin, ultimaTentativa, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "UsuarioAdministrador{id=" + getId() + ", nome=" + getNome() + "}";
    }
}
