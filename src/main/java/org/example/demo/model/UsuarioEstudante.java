package org.example.demo.model;

import java.time.LocalDateTime;

public class UsuarioEstudante extends Usuario {

    private int ra;

    public UsuarioEstudante() {
        super();
        setTipo(Tipo.ESTUDANTE);
    }

    public UsuarioEstudante(String nome, String email, String senhaHash, int ra) {
        super(nome, email, senhaHash, Tipo.ESTUDANTE);
        this.ra = ra;
    }

    public UsuarioEstudante(int id, String nome, String email, String senhaHash,
                            String cpf, String telefone, boolean bloqueado,
                            int tentativasLogin, LocalDateTime ultimaTentativa,
                            LocalDateTime createdAt, LocalDateTime updatedAt,
                            int ra) {
        super(id, nome, email, senhaHash, Tipo.ESTUDANTE, cpf, telefone,
                bloqueado, tentativasLogin, ultimaTentativa, createdAt, updatedAt);
        this.ra = ra;
    }

    public int getRa() { return ra; }
    public void setRa(int ra) { this.ra = ra; }

    @Override
    public String toString() {
        return "UsuarioEstudante{id=" + getId() + ", nome=" + getNome() + ", ra=" + ra + "}";
    }
}