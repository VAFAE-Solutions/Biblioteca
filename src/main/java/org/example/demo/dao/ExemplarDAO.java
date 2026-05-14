package org.example.demo.dao;

import org.example.demo.Database;
import org.example.demo.model.Exemplar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExemplarDAO {

    public boolean inserir(Exemplar exemplar) {
        String sql = """
                INSERT INTO exemplar (livro_id, unidade_id, codigo_patrimonio, status)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, exemplar.getLivroId());
            ps.setInt(2, exemplar.getUnidadeId());
            ps.setString(3, exemplar.getCodigoPatrimonio());
            ps.setString(4, exemplar.getStatus().name());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    exemplar.setId(keys.getInt(1));
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir exemplar: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só exemplares ativos
    public Exemplar buscarPorId(int id) {
        String sql = "SELECT * FROM exemplar WHERE id = ? AND ativo = TRUE";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearExemplar(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar exemplar por ID: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só exemplares ativos
    public List<Exemplar> buscarPorLivro(int livroId) {
        String sql = "SELECT * FROM exemplar WHERE livro_id = ? AND ativo = TRUE";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, livroId);
            ResultSet rs = ps.executeQuery();
            List<Exemplar> exemplares = new ArrayList<>();

            while (rs.next()) {
                exemplares.add(mapearExemplar(rs));
            }
            return exemplares;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar exemplares por livro: " + e.getMessage(), e);
        }
    }

    // ✅ Busca só exemplares ativos e disponíveis
    public List<Exemplar> buscarDisponiveisPorLivroEUnidade(int livroId, int unidadeId) {
        String sql = """
                SELECT * FROM exemplar
                WHERE livro_id = ? AND unidade_id = ?
                AND status = 'DISPONIVEL' AND ativo = TRUE
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, livroId);
            ps.setInt(2, unidadeId);
            ResultSet rs = ps.executeQuery();
            List<Exemplar> exemplares = new ArrayList<>();

            while (rs.next()) {
                exemplares.add(mapearExemplar(rs));
            }
            return exemplares;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar exemplares disponíveis: " + e.getMessage(), e);
        }
    }

    public boolean atualizar(Exemplar exemplar) {
        String sql = "UPDATE exemplar SET status = ? WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, exemplar.getStatus().name());
            ps.setInt(2, exemplar.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar exemplar: " + e.getMessage(), e);
        }
    }

    // ✅ Desativar em vez de deletar
    public boolean desativar(int id) {
        String sql = "UPDATE exemplar SET ativo = FALSE WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desativar exemplar: " + e.getMessage(), e);
        }
    }

    private Exemplar mapearExemplar(ResultSet rs) throws SQLException {
        return new Exemplar(
                rs.getInt("id"),
                rs.getInt("livro_id"),
                rs.getInt("unidade_id"),
                rs.getString("codigo_patrimonio"),
                Exemplar.Status.valueOf(rs.getString("status")),
                rs.getBoolean("ativo") // ✅ novo campo
        );
    }
}
