package org.example.demo.model;

import java.time.LocalDateTime;

public class Usuario {

    public enum Tipo { ADMIN, COMUM, ESTUDANTE, BIBLIOTECARIO }

    private int id;
    private String nome;
    private String email;
    private String senhaHash;
    private Tipo tipo;
    private String cpf;
    private String telefone;
    private boolean bloqueado = false;
    private boolean ativo = true; // ✅ novo campo
    private int tentativasLogin = 0;
    private LocalDateTime ultimaTentativa;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Regras de negócio por tipo
    private int prazoEmprestimo;
    private int limiteCotas;

    public Usuario() {}

    public Usuario(String nome, String email, String senhaHash, Tipo tipo) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.tipo = tipo;
        this.prazoEmprestimo = calcularPrazoPorTipo(tipo);
        this.limiteCotas = calcularLimitePorTipo(tipo);
    }

    public Usuario(int id, String nome, String email, String senhaHash,
                   Tipo tipo, String cpf, String telefone, boolean bloqueado,
                   boolean ativo, int tentativasLogin, LocalDateTime ultimaTentativa,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(nome, email, senhaHash, tipo);
        this.id = id;
        this.cpf = cpf;
        this.telefone = telefone;
        this.bloqueado = bloqueado;
        this.ativo = ativo;
        this.tentativasLogin = tentativasLogin;
        this.ultimaTentativa = ultimaTentativa;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private int calcularPrazoPorTipo(Tipo tipo) {
        if (tipo == Tipo.ESTUDANTE) return 15;
        return 7;
    }

    private int calcularLimitePorTipo(Tipo tipo) {
        if (tipo == Tipo.ESTUDANTE) return 5;
        return 3;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
        this.prazoEmprestimo = calcularPrazoPorTipo(tipo);
        this.limiteCotas = calcularLimitePorTipo(tipo);
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public boolean isBloqueado() { return bloqueado; }
    public void setBloqueado(boolean bloqueado) { this.bloqueado = bloqueado; }

    // ✅ getter/setter ativo
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public int getTentativasLogin() { return tentativasLogin; }
    public void setTentativasLogin(int tentativasLogin) { this.tentativasLogin = tentativasLogin; }

    public LocalDateTime getUltimaTentativa() { return ultimaTentativa; }
    public void setUltimaTentativa(LocalDateTime ultimaTentativa) { this.ultimaTentativa = ultimaTentativa; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getPrazoEmprestimo() { return prazoEmprestimo; }
    public void setPrazoEmprestimo(int prazoEmprestimo) { this.prazoEmprestimo = prazoEmprestimo; }

    public int getLimiteCotas() { return limiteCotas; }
    public void setLimiteCotas(int limiteCotas) { this.limiteCotas = limiteCotas; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nome=" + nome + ", tipo=" + tipo + ", ativo=" + ativo + "}";
    }
}