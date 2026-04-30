package org.example.demo.service;

import org.example.demo.dao.LivroDAO;
import org.example.demo.model.Livro;

import java.util.List;

public class LivroService {

    private final LivroDAO livroDAO;

    public LivroService() {
        this.livroDAO = new LivroDAO();
    }

    public boolean cadastrar(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo.");
        }
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório.");
        }
        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            throw new IllegalArgumentException("Autor é obrigatório.");
        }
        return livroDAO.inserir(livro);
    }

    public Livro buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return livroDAO.buscarPorId(id);
    }

    // ✅ Busca simultânea por título, autor e gênero
    public List<Livro> buscarGeral(String termo) {
        if (termo == null || termo.isBlank()) {
            throw new IllegalArgumentException("Termo de busca não pode ser vazio.");
        }
        return livroDAO.buscarGeral(termo);
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título não pode ser vazio.");
        }
        return livroDAO.buscarPorTitulo(titulo);
    }

    public List<Livro> buscarPorAutor(String autor) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("Autor não pode ser vazio.");
        }
        return livroDAO.buscarPorAutor(autor);
    }

    public List<Livro> buscarPorGenero(String genero) {
        if (genero == null || genero.isBlank()) {
            throw new IllegalArgumentException("Gênero não pode ser vazio.");
        }
        return livroDAO.buscarPorGenero(genero);
    }

    public List<Livro> listarTodos() {
        return livroDAO.listarTodos();
    }

    public boolean atualizar(Livro livro) {
        if (livro == null || livro.getId() <= 0) {
            throw new IllegalArgumentException("Livro inválido para atualização.");
        }
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório.");
        }
        return livroDAO.atualizar(livro);
    }

    public boolean deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return livroDAO.deletar(id);
    }
}
