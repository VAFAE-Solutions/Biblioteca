package org.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class FilaReserva {

    private int livroId;
    private int unidadeId;
    private List<Reserva> fila = new ArrayList<>();

    // objetos completos, populados quando necessário
    private Livro livro;
    private Unidade unidade;

    public FilaReserva() {}

    public FilaReserva(int livroId, int unidadeId) {
        this.livroId = livroId;
        this.unidadeId = unidadeId;
    }

    public FilaReserva(Livro livro, Unidade unidade) {
        this.livro = livro;
        this.livroId = livro.getId();
        this.unidade = unidade;
        this.unidadeId = unidade.getId();
    }

    // Retorna a posição que a próxima reserva ocupará
    public int proximaPosicao() {
        return fila.size() + 1;
    }

    // Retorna quantas reservas estão aguardando
    public long totalAguardando() {
        return fila.stream()
                .filter(r -> r.getStatus() == Reserva.Status.AGUARDANDO)
                .count();
    }

    // Retorna a primeira reserva aguardando
    public Reserva proximaReserva() {
        return fila.stream()
                .filter(r -> r.getStatus() == Reserva.Status.AGUARDANDO)
                .findFirst()
                .orElse(null);
    }

    // Verifica se um usuário já está na fila
    public boolean usuarioNaFila(int usuarioId) {
        return fila.stream()
                .anyMatch(r -> r.getUsuarioId() == usuarioId
                        && r.getStatus() == Reserva.Status.AGUARDANDO);
    }

    // Verifica se a fila está vazia
    public boolean isEmpty() {
        return totalAguardando() == 0;
    }

    public int getLivroId() { return livroId; }
    public void setLivroId(int livroId) { this.livroId = livroId; }

    public int getUnidadeId() { return unidadeId; }
    public void setUnidadeId(int unidadeId) { this.unidadeId = unidadeId; }

    public List<Reserva> getFila() { return fila; }
    public void setFila(List<Reserva> fila) { this.fila = fila; }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) {
        this.livro = livro;
        this.livroId = livro.getId();
    }

    public Unidade getUnidade() { return unidade; }
    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
        this.unidadeId = unidade.getId();
    }

    @Override
    public String toString() {
        return "FilaReserva{livroId=" + livroId + ", unidadeId=" + unidadeId
                + ", totalAguardando=" + totalAguardando() + "}";
    }
}