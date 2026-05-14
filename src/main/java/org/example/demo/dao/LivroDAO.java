package org.example.demo.dao;

import org.example.demo.Database;
import org.example.demo.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public boolean inserir(Livro livro) {
        String sql = """
                INSERT INTO livros (titulo, autor, editora, ano_publicacao,
                genero, descricao, sumario, capa_url)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setString(3, livro.getEditora());
            ps.setInt(4, livro.getAnoPublicacao());
            ps.setString(5, livro.getGenero());
            ps.setString(6, livro.getDescricao());
            ps.setString(7, livro.getSumario());
            ps.setString(8, livro.getCapaUrl());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    livro.setId(keys.getInt(1));
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir livro: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só livros ativos
    public Livro buscarPorId(int id) {
        String sql = "SELECT * FROM livros WHERE id = ? AND ativo = TRUE";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearLivro(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ID: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só livros ativos
    public List<Livro> buscarGeral(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodos();
        }

        String sql = """
                SELECT * FROM livros
                WHERE ativo = TRUE AND (titulo LIKE ? OR autor LIKE ? OR genero LIKE ?)
                ORDER BY titulo ASC
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String busca = "%" + termo.trim() + "%";
            ps.setString(1, busca);
            ps.setString(2, busca);
            ps.setString(3, busca);

            ResultSet rs = ps.executeQuery();
            List<Livro> livros = new ArrayList<>();

            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }
            return livros;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só livros ativos
    public List<Livro> buscarPorTitulo(String titulo) {
        String sql = "SELECT * FROM livros WHERE ativo = TRUE AND titulo LIKE ? ORDER BY titulo ASC";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + titulo.trim() + "%");
            ResultSet rs = ps.executeQuery();
            List<Livro> livros = new ArrayList<>();

            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }
            return livros;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por título: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só livros ativos
    public List<Livro> buscarPorAutor(String autor) {
        String sql = "SELECT * FROM livros WHERE ativo = TRUE AND autor LIKE ? ORDER BY autor ASC";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + autor.trim() + "%");
            ResultSet rs = ps.executeQuery();
            List<Livro> livros = new ArrayList<>();

            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }
            return livros;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por autor: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só livros ativos
    public List<Livro> buscarPorGenero(String genero) {
        String sql = "SELECT * FROM livros WHERE ativo = TRUE AND genero LIKE ? ORDER BY titulo ASC";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + genero.trim() + "%");
            ResultSet rs = ps.executeQuery();
            List<Livro> livros = new ArrayList<>();

            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }
            return livros;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por gênero: " + e.getMessage(), e);
        }
    }

    // ✅ Lista só livros ativos
    public List<Livro> listarTodos() {
        String sql = "SELECT * FROM livros WHERE ativo = TRUE ORDER BY created_at DESC";
        List<Livro> livros = new ArrayList<>();

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                livros.add(mapearLivro(rs));
            }
            return livros;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros: " + e.getMessage(), e);
        }
    }

    public boolean atualizar(Livro livro) {
        String sql = """
                UPDATE livros SET titulo = ?, autor = ?, editora = ?,
                ano_publicacao = ?, genero = ?, descricao = ?,
                sumario = ?, capa_url = ?
                WHERE id = ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setString(3, livro.getEditora());
            ps.setInt(4, livro.getAnoPublicacao());
            ps.setString(5, livro.getGenero());
            ps.setString(6, livro.getDescricao());
            ps.setString(7, livro.getSumario());
            ps.setString(8, livro.getCapaUrl());
            ps.setInt(9, livro.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro: " + e.getMessage(), e);
        }
    }

    // ✅ Desativar em vez de deletar
    public boolean desativar(int id) {
        String sql = "UPDATE livros SET ativo = FALSE WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desativar livro: " + e.getMessage(), e);
        }
    }

    // ✅ Mantido para compatibilidade
    public boolean deletar(int id) {
        return desativar(id);
    }

    private Livro mapearLivro(ResultSet rs) throws SQLException {
        return new Livro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("editora"),
                rs.getInt("ano_publicacao"),
                rs.getString("genero"),
                rs.getString("descricao"),
                rs.getString("sumario"),
                rs.getString("capa_url"),
                rs.getBoolean("ativo"), // ✅ novo campo
                rs.getTimestamp("created_at") != null
                        ? rs.getTimestamp("created_at").toLocalDateTime() : null
        );
    }
}