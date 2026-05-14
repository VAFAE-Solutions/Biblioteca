package org.example.demo.model;

public class Exemplar {

    public enum Status { DISPONIVEL, EMPRESTADO, RESERVADO }

    private int id;
    private int livroId;
    private int unidadeId;
    private String codigoPatrimonio;
    private Status status = Status.DISPONIVEL;
    private boolean ativo = true; // ✅ novo campo

    // objetos completos, populados quando necessário
    private Livro livro;
    private Unidade unidade;

    public Exemplar() {}

    public Exemplar(int livroId, int unidadeId, String codigoPatrimonio) {
        this.livroId = livroId;
        this.unidadeId = unidadeId;
        this.codigoPatrimonio = codigoPatrimonio;
    }

    public Exemplar(int id, int livroId, int unidadeId,
                    String codigoPatrimonio, Status status) {
        this(livroId, unidadeId, codigoPatrimonio);
        this.id = id;
        this.status = status;
    }

    public Exemplar(int id, int livroId, int unidadeId,
                    String codigoPatrimonio, Status status, boolean ativo) {
        this(id, livroId, unidadeId, codigoPatrimonio, status);
        this.ativo = ativo; // ✅ construtor completo com ativo
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLivroId() { return livroId; }
    public void setLivroId(int livroId) { this.livroId = livroId; }

    public int getUnidadeId() { return unidadeId; }
    public void setUnidadeId(int unidadeId) { this.unidadeId = unidadeId; }

    public String getCodigoPatrimonio() { return codigoPatrimonio; }
    public void setCodigoPatrimonio(String codigoPatrimonio) {
        this.codigoPatrimonio = codigoPatrimonio;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    // ✅ getter/setter ativo
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

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
        return "Exemplar{id=" + id + ", codigoPatrimonio=" + codigoPatrimonio
                + ", status=" + status + ", ativo=" + ativo + "}";
    }
}