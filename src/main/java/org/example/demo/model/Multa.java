package org.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Multa {

    private int id;
    private int emprestimoId;
    private int usuarioId;
    private BigDecimal valor; // decimal(10,2) no banco
    private boolean pago = false;
    private LocalDate dataGeracao;
    private LocalDate dataPagamento; // null enquanto não pago

    // objetos completos, populados quando necessário
    private Emprestimo emprestimo;
    private Usuario usuario;

    public Multa() {}

    public Multa(int emprestimoId, int usuarioId, BigDecimal valor, LocalDate dataGeracao) {
        this.emprestimoId = emprestimoId;
        this.usuarioId = usuarioId;
        this.valor = valor;
        this.dataGeracao = dataGeracao;
    }

    public Multa(int id, int emprestimoId, int usuarioId, BigDecimal valor,
                 boolean pago, LocalDate dataGeracao, LocalDate dataPagamento) {
        this(emprestimoId, usuarioId, valor, dataGeracao);
        this.id = id;
        this.pago = pago;
        this.dataPagamento = dataPagamento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmprestimoId() { return emprestimoId; }
    public void setEmprestimoId(int emprestimoId) { this.emprestimoId = emprestimoId; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }

    public LocalDate getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDate dataGeracao) { this.dataGeracao = dataGeracao; }

    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

    public Emprestimo getEmprestimo() { return emprestimo; }
    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
        this.emprestimoId = emprestimo.getId();
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.usuarioId = usuario.getId();
    }

    @Override
    public String toString() {
        return "Multa{id=" + id + ", valor=" + valor + ", pago=" + pago + "}";
    }
}
