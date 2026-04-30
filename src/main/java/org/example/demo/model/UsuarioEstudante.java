package org.example.demo.model;

import java.time.LocalDateTime;

public class UsuarioEstudante extends Usuario {

    private int ra; // obrigatório — constraint chk_estudante_ra no banco

    // Construtor vazio — usado pelo DAO
    public UsuarioEstudante() {
        super();
        setTipo(Tipo.ESTUDANTE);
    }

    // Construtor mínimo — cadastro
    public UsuarioEstudante(String nome, String email, String senhaHash, int ra) {
        super(nome, email, senhaHash, Tipo.ESTUDANTE);
        this.ra = ra;
    }

    // Construtor completo — usado pelo DAO ao reconstruir do banco
    public UsuarioEstudante(int id, String nome, String email, String senhaHash,
                            String telefone, boolean bloqueado, int tentativasLogin,
                            LocalDateTime ultimaTentativa, LocalDateTime createdAt,
                            LocalDateTime updatedAt, int ra) {
        super(id, nome, email, senhaHash, Tipo.ESTUDANTE, telefone,
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