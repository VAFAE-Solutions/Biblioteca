package org.example.demo.model;

import java.time.LocalDateTime;

public class UsuarioBibliotecario extends Usuario {

    private int unidadeId;
    private Unidade unidade;

    public UsuarioBibliotecario() {
        super();
        setTipo(Tipo.BIBLIOTECARIO);
    }

    public UsuarioBibliotecario(String nome, String email, String senhaHash,
                                int unidadeId) {
        super(nome, email, senhaHash, Tipo.BIBLIOTECARIO);
        this.unidadeId = unidadeId;
    }

    public UsuarioBibliotecario(int id, String nome, String email, String senhaHash,
                                String cpf, String telefone, boolean bloqueado,
                                int tentativasLogin, LocalDateTime ultimaTentativa,
                                LocalDateTime createdAt, LocalDateTime updatedAt,
                                int unidadeId) {
        super(id, nome, email, senhaHash, Tipo.BIBLIOTECARIO, cpf, telefone,
                bloqueado, tentativasLogin, ultimaTentativa, createdAt, updatedAt);
        this.unidadeId = unidadeId;
    }

    public int getUnidadeId() { return unidadeId; }
    public void setUnidadeId(int unidadeId) { this.unidadeId = unidadeId; }

    public Unidade getUnidade() { return unidade; }
    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
        this.unidadeId = unidade.getId();
    }

    @Override
    public String toString() {
        return "UsuarioBibliotecario{id=" + getId() + ", nome=" + getNome()
                + ", unidadeId=" + unidadeId + "}";
    }
}