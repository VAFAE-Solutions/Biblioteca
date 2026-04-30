package org.example.demo.model;

import java.time.LocalDate;

public class Emprestimo {

    public enum Status { ATIVO, FINALIZADO, ATRASADO }

    private int id;
    private int exemplarId;
    private int usuarioId;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucao; // null enquanto não devolvido
    private Status status = Status.ATIVO;

    // objetos completos, populados quando necessário
    private Exemplar exemplar;
    private Usuario usuario;

    public Emprestimo() {}

    public Emprestimo(int exemplarId, int usuarioId,
                      LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        this.exemplarId = exemplarId;
        this.usuarioId = usuarioId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public Emprestimo(int id, int exemplarId, int usuarioId, LocalDate dataEmprestimo,
                      LocalDate dataDevolucaoPrevista, LocalDate dataDevolucao, Status status) {
        this(exemplarId, usuarioId, dataEmprestimo, dataDevolucaoPrevista);
        this.id = id;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getExemplarId() { return exemplarId; }
    public void setExemplarId(int exemplarId) { this.exemplarId = exemplarId; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }

    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) { this.dataDevolucao = dataDevolucao; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Exemplar getExemplar() { return exemplar; }
    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
        this.exemplarId = exemplar.getId();
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.usuarioId = usuario.getId();
    }

    @Override
    public String toString() {
        return "Emprestimo{id=" + id + ", exemplarId=" + exemplarId
                + ", usuarioId=" + usuarioId + ", status=" + status + "}";
    }
}