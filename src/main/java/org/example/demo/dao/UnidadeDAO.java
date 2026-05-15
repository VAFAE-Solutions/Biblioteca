package org.example.demo.dao;

import org.example.demo.database.Database;
import org.example.demo.model.Unidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnidadeDAO {

    public boolean inserir(Unidade unidade) {
        String sql = """
                INSERT INTO unidade (nome, endereco, telefone, horario_funcionamento)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, unidade.getNome());
            ps.setString(2, unidade.getEndereco());
            ps.setString(3, unidade.getTelefone());
            ps.setString(4, unidade.getHorarioFuncionamento());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    unidade.setId(keys.getInt(1));
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir unidade: " + e.getMessage(), e);
        }
    }

    public Unidade buscarPorId(int id) {
        String sql = "SELECT * FROM unidade WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearUnidade(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar unidade por ID: " + e.getMessage(), e);
        }
    }

    public List<Unidade> listarTodas() {
        String sql = "SELECT * FROM unidade";
        List<Unidade> unidades = new ArrayList<>();

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                unidades.add(mapearUnidade(rs));
            }
            return unidades;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar unidades: " + e.getMessage(), e);
        }
    }

    public boolean atualizar(Unidade unidade) {
        String sql = """
                UPDATE unidade SET nome = ?, endereco = ?, telefone = ?,
                horario_funcionamento = ? WHERE id = ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, unidade.getNome());
            ps.setString(2, unidade.getEndereco());
            ps.setString(3, unidade.getTelefone());
            ps.setString(4, unidade.getHorarioFuncionamento());
            ps.setInt(5, unidade.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar unidade: " + e.getMessage(), e);
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM unidade WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // ON DELETE RESTRICT — erro se houver vínculos
            throw new RuntimeException("Erro ao deletar unidade. Verifique se há vínculos: "
                    + e.getMessage(), e);
        }
    }

    private Unidade mapearUnidade(ResultSet rs) throws SQLException {
        return new Unidade(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("endereco"),
                rs.getString("telefone"),
                rs.getString("horario_funcionamento")
        );
    }
}
