package org.example.demo.dao;

import org.example.demo.Database;
import org.example.demo.model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    /**
     * Lista todos os livros cadastrados.
     */
    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros ORDER BY created_at DESC";

        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        livros.add(mapearLivro(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os livros: " + e.getMessage());
        }
        return livros;
    }

    /**
     * Busca livros por título ou autor usando o operador LIKE.
     */
    public List<Livro> buscarLivros(String termo) {
        List<Livro> livros = new ArrayList<>();
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodos();
        }

        String sql = "SELECT * FROM livros WHERE titulo LIKE ? OR autor LIKE ? ORDER BY titulo ASC";

        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    String busca = "%" + termo.trim() + "%";
                    stmt.setString(1, busca);
                    stmt.setString(2, busca);

                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            livros.add(mapearLivro(rs));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros: " + e.getMessage());
        }
        return livros;
    }

    /**
     * NOVO: Busca um livro específico pelo seu ID para a página de detalhes.
     */
    public Livro buscarPorId(int id) {
        String sql = "SELECT * FROM livros WHERE id = ?";
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return mapearLivro(rs);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Método auxiliar para transformar uma linha do banco em um objeto Livro.
     */
    private Livro mapearLivro(ResultSet rs) throws SQLException {
        Livro l = new Livro();
        l.setId(rs.getInt("id"));
        l.setTitulo(rs.getString("titulo"));
        l.setAutor(rs.getString("autor"));
        l.setEditora(rs.getString("editora"));
        l.setCapaUrl(rs.getString("capa_url"));
        return l;
    }
}