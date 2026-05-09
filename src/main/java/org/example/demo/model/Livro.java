package org.example.demo.model;

import java.time.LocalDateTime;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private String editora;
    private int anoPublicacao;
    private String genero;
    private String descricao;
    private String sumario;
    private String capaUrl;        // agora existe no banco ✅
    private LocalDateTime createdAt;

    public Livro() {}

    public Livro(String titulo, String autor, String editora,
                 int anoPublicacao, String genero, String descricao, String sumario) {
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.descricao = descricao;
        this.sumario = sumario;
    }

    public Livro(int id, String titulo, String autor, String editora,
                 int anoPublicacao, String genero, String descricao,
                 String sumario, String capaUrl, LocalDateTime createdAt) {
        this(titulo, autor, editora, anoPublicacao, genero, descricao, sumario);
        this.id = id;
        this.capaUrl = capaUrl;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }

    public int getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getSumario() { return sumario; }
    public void setSumario(String sumario) { this.sumario = sumario; }

    public String getCapaUrl() { return capaUrl; }
    public void setCapaUrl(String capaUrl) { this.capaUrl = capaUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Livro{id=" + id + ", titulo=" + titulo + ", autor=" + autor + "}";
    }
}