package org.example.demo.model;

public class Unidade {

    private int id;
    private String nome;
    private String endereco;
    private String telefone;
    private String horarioFuncionamento;

    public Unidade() {}

    public Unidade(String nome, String endereco, String telefone, String horarioFuncionamento) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public Unidade(int id, String nome, String endereco, String telefone, String horarioFuncionamento) {
        this(nome, endereco, telefone, horarioFuncionamento);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    @Override
    public String toString() {
        return "Unidade{id=" + id + ", nome=" + nome + "}";
    }
}