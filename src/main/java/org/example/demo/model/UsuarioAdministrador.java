package org.example.demo.model;

import java.time.LocalDateTime;

public class UsuarioAdministrador extends Usuario {

    public UsuarioAdministrador() {
        super();
        setTipo(Tipo.ADMIN);
    }

    public UsuarioAdministrador(String nome, String email, String senhaHash) {
        super(nome, email, senhaHash, Tipo.ADMIN);
    }

    public UsuarioAdministrador(int id, String nome, String email, String senhaHash,
                                String cpf, String telefone, boolean bloqueado,
                                boolean ativo, int tentativasLogin,
                                LocalDateTime ultimaTentativa,
                                LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, nome, email, senhaHash, Tipo.ADMIN, cpf, telefone,
                bloqueado, ativo, tentativasLogin, ultimaTentativa, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "UsuarioAdministrador{id=" + getId() + ", nome=" + getNome() + "}";
    }
}
