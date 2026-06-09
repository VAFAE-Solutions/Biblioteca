package org.example.demo.dao;

import org.example.demo.database.Database;
import org.example.demo.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO implements Persistivel<Livro> {

    public boolean inserir(Livro livro) {
        String sql = """
                INSERT INTO livros (titulo, autor, editora, ano_publicacao,
                genero, descricao, sumario, capa_url, capa_imagem)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
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
            if (livro.getCapaImagem() != null) {
                ps.setBytes(9, livro.getCapaImagem());
            } else {
                ps.setNull(9, Types.BLOB);
            }

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) livro.setId(keys.getInt(1));
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir livro: " + e.getMessage(), e);
        }
    }

    public Livro buscarPorId(int id) {
        // ✅ Não carrega capa_imagem na listagem — só metadados
        String sql = "SELECT id, titulo, autor, editora, ano_publicacao, genero, descricao, sumario, capa_url, ativo, created_at, (capa_imagem IS NOT NULL AND LENGTH(capa_imagem) > 0) as tem_capa FROM livros WHERE id = ? AND ativo = TRUE";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearLivroSemImagem(rs);
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por ID: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só os bytes da imagem — rápido para o CapaServlet
    public byte[] buscarCapaImagem(int id) {
        String sql = "SELECT capa_imagem FROM livros WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBytes("capa_imagem");
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar capa: " + e.getMessage(), e);
        }
    }

    public List<Livro> buscarGeral(String termo) {
        if (termo == null || termo.trim().isEmpty()) return listarTodos();

        String[] termos = termo.trim().split("[,\\s]+");
        StringBuilder sql = new StringBuilder(
                "SELECT id, titulo, autor, editora, ano_publicacao, genero, descricao, sumario, capa_url, ativo, created_at, (capa_imagem IS NOT NULL AND LENGTH(capa_imagem) > 0) as tem_capa FROM livros WHERE ativo = TRUE AND (");

        for (int i = 0; i < termos.length; i++) {
            if (i > 0) sql.append(" OR ");
            sql.append("(titulo LIKE ? OR autor LIKE ? OR genero LIKE ?)");
        }
        sql.append(") ORDER BY titulo ASC");

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;
            for (String t : termos) {
                String busca = "%" + t.trim() + "%";
                ps.setString(idx++, busca);
                ps.setString(idx++, busca);
                ps.setString(idx++, busca);
            }

            ResultSet rs = ps.executeQuery();
            List<Livro> livros = new ArrayList<>();
            while (rs.next()) livros.add(mapearLivroSemImagem(rs));
            return livros;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros: " + e.getMessage(), e);
        }
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        String sql = "SELECT id, titulo, autor, editora, ano_publicacao, genero, descricao, sumario, capa_url, ativo, created_at, (capa_imagem IS NOT NULL AND LENGTH(capa_imagem) > 0) as tem_capa FROM livros WHERE ativo = TRUE AND titulo LIKE ? ORDER BY titulo ASC";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + titulo.trim() + "%");
            ResultSet rs = ps.executeQuery();
            List<Livro> livros = new ArrayList<>();
            while (rs.next()) livros.add(mapearLivroSemImagem(rs));
            return livros;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por título: " + e.getMessage(), e);
        }
    }

    public List<Livro> buscarPorAutor(String autor) {
        String sql = "SELECT id, titulo, autor, editora, ano_publicacao, genero, descricao, sumario, capa_url, ativo, created_at, (capa_imagem IS NOT NULL AND LENGTH(capa_imagem) > 0) as tem_capa FROM livros WHERE ativo = TRUE AND autor LIKE ? ORDER BY autor ASC";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + autor.trim() + "%");
            ResultSet rs = ps.executeQuery();
            List<Livro> livros = new ArrayList<>();
            while (rs.next()) livros.add(mapearLivroSemImagem(rs));
            return livros;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por autor: " + e.getMessage(), e);
        }
    }

    public List<Livro> buscarPorGenero(String genero) {
        String sql = "SELECT id, titulo, autor, editora, ano_publicacao, genero, descricao, sumario, capa_url, ativo, created_at, (capa_imagem IS NOT NULL AND LENGTH(capa_imagem) > 0) as tem_capa FROM livros WHERE ativo = TRUE AND genero LIKE ? ORDER BY titulo ASC";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + genero.trim() + "%");
            ResultSet rs = ps.executeQuery();
            List<Livro> livros = new ArrayList<>();
            while (rs.next()) livros.add(mapearLivroSemImagem(rs));
            return livros;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livro por gênero: " + e.getMessage(), e);
        }
    }

    public List<Livro> listarTodos() {
        // ✅ Não carrega bytes da imagem — só flag se tem capa
        String sql = "SELECT id, titulo, autor, editora, ano_publicacao, genero, descricao, sumario, capa_url, ativo, created_at, (capa_imagem IS NOT NULL AND LENGTH(capa_imagem) > 0) as tem_capa FROM livros WHERE ativo = TRUE ORDER BY created_at DESC";
        List<Livro> livros = new ArrayList<>();
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) livros.add(mapearLivroSemImagem(rs));
            return livros;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar livros: " + e.getMessage(), e);
        }
    }

    public boolean atualizar(Livro livro) {
        String sql = """
                UPDATE livros SET titulo = ?, autor = ?, editora = ?,
                ano_publicacao = ?, genero = ?, descricao = ?,
                sumario = ?, capa_url = ?, capa_imagem = ?
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
            if (livro.getCapaImagem() != null) {
                ps.setBytes(9, livro.getCapaImagem());
            } else {
                ps.setNull(9, Types.BLOB);
            }
            ps.setInt(10, livro.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar livro: " + e.getMessage(), e);
        }
    }

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

    public boolean deletar(int id) { return desativar(id); }

    // ✅ Mapeia livro SEM carregar bytes da imagem — usa flag tem_capa
    private Livro mapearLivroSemImagem(ResultSet rs) throws SQLException {
        Livro livro = new Livro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("editora"),
                rs.getInt("ano_publicacao"),
                rs.getString("genero"),
                rs.getString("descricao"),
                rs.getString("sumario"),
                rs.getString("capa_url"),
                rs.getBoolean("ativo"),
                rs.getTimestamp("created_at") != null
                        ? rs.getTimestamp("created_at").toLocalDateTime() : null
        );
        // ✅ Marca se tem imagem sem carregar os bytes
        if (rs.getBoolean("tem_capa")) {
            livro.setTemCapaNobanco(true);
        }
        return livro;
    }
}
