package org.example.demo.model;

import java.time.LocalDate;

public class Reserva {

    public enum Status { AGUARDANDO, ATENDIDA, CANCELADA }

    private int id;
    private int livroId;
    private int usuarioId;
    private int unidadeId;
    private LocalDate dataReserva;
    private Status status = Status.AGUARDANDO;
    private int posicaoFila;

    // objetos completos, populados quando necessário
    private Livro livro;
    private Usuario usuario;
    private Unidade unidade;

    public Reserva() {}

    public Reserva(int livroId, int usuarioId, int unidadeId, LocalDate dataReserva) {
        this.livroId = livroId;
        this.usuarioId = usuarioId;
        this.unidadeId = unidadeId;
        this.dataReserva = dataReserva;
    }

    public Reserva(int id, int livroId, int usuarioId, int unidadeId,
                   LocalDate dataReserva, Status status, int posicaoFila) {
        this(livroId, usuarioId, unidadeId, dataReserva);
        this.id = id;
        this.status = status;
        this.posicaoFila = posicaoFila;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLivroId() { return livroId; }
    public void setLivroId(int livroId) { this.livroId = livroId; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getUnidadeId() { return unidadeId; }
    public void setUnidadeId(int unidadeId) { this.unidadeId = unidadeId; }

    public LocalDate getDataReserva() { return dataReserva; }
    public void setDataReserva(LocalDate dataReserva) { this.dataReserva = dataReserva; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public int getPosicaoFila() { return posicaoFila; }
    public void setPosicaoFila(int posicaoFila) { this.posicaoFila = posicaoFila; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) {
        this.livro = livro;
        this.livroId = livro.getId();
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.usuarioId = usuario.getId();
    }

    public Unidade getUnidade() { return unidade; }
    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
        this.unidadeId = unidade.getId();
    }

    @Override
    public String toString() {
        return "Reserva{id=" + id + ", livroId=" + livroId
                + ", usuarioId=" + usuarioId + ", status=" + status + "}";
    }
}
