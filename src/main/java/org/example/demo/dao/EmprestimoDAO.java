package org.example.demo.dao;

import org.example.demo.Database;
import org.example.demo.model.Emprestimo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    public boolean inserir(Emprestimo emprestimo) {
        String sqlEmprestimo = """
                INSERT INTO emprestimo (exemplar_id, usuario_id, data_emprestimo,
                data_devolucao_prevista, status) VALUES (?, ?, ?, ?, ?)
                """;
        String sqlUpdateExemplar = "UPDATE exemplar SET status = 'EMPRESTADO' WHERE id = ?";

        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(sqlEmprestimo,
                        Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, emprestimo.getExemplarId());
                    ps.setInt(2, emprestimo.getUsuarioId());
                    ps.setDate(3, Date.valueOf(emprestimo.getDataEmprestimo()));
                    ps.setDate(4, Date.valueOf(emprestimo.getDataDevolucaoPrevista()));
                    ps.setString(5, emprestimo.getStatus().name());

                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        ResultSet keys = ps.getGeneratedKeys();
                        if (keys.next()) {
                            emprestimo.setId(keys.getInt(1));
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlUpdateExemplar)) {
                    ps.setInt(1, emprestimo.getExemplarId());
                    ps.executeUpdate();
                }

                con.commit();
                return true;

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException("Erro ao registrar empréstimo: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro de conexão: " + e.getMessage(), e);
        }
    }

    public Emprestimo buscarPorId(int id) {
        String sql = "SELECT * FROM emprestimo WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearEmprestimo(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empréstimo por ID: " + e.getMessage(), e);
        }
    }

    public List<Emprestimo> buscarPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM emprestimo WHERE usuario_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            List<Emprestimo> emprestimos = new ArrayList<>();

            while (rs.next()) {
                emprestimos.add(mapearEmprestimo(rs));
            }
            return emprestimos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empréstimos por usuário: " + e.getMessage(), e);
        }
    }

    public List<Emprestimo> buscarPorStatus(Emprestimo.Status status) {
        String sql = "SELECT * FROM emprestimo WHERE status = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();
            List<Emprestimo> emprestimos = new ArrayList<>();

            while (rs.next()) {
                emprestimos.add(mapearEmprestimo(rs));
            }
            return emprestimos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empréstimos por status: " + e.getMessage(), e);
        }
    }

    public List<Emprestimo> buscarAtrasados(LocalDate hoje) {
        String sql = """
                SELECT * FROM emprestimo
                WHERE status = 'ATIVO' AND data_devolucao_prevista < ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(hoje));
            ResultSet rs = ps.executeQuery();
            List<Emprestimo> emprestimos = new ArrayList<>();

            while (rs.next()) {
                emprestimos.add(mapearEmprestimo(rs));
            }
            return emprestimos;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empréstimos atrasados: " + e.getMessage(), e);
        }
    }

    public boolean atualizar(Emprestimo emprestimo) {
        String sql = """
                UPDATE emprestimo SET status = ?, data_devolucao = ?
                WHERE id = ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emprestimo.getStatus().name());
            ps.setDate(2, emprestimo.getDataDevolucao() != null
                    ? Date.valueOf(emprestimo.getDataDevolucao()) : null);
            ps.setInt(3, emprestimo.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar empréstimo: " + e.getMessage(), e);
        }
    }

    // ✅ NOVO — conta empréstimos ativos/atrasados do usuário
    public int contarEmprestimosAtivos(int usuarioId) {
        String sql = """
                SELECT COUNT(*) FROM emprestimo
                WHERE usuario_id = ? AND status IN ('ATIVO', 'ATRASADO')
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar empréstimos: " + e.getMessage(), e);
        }
    }

    private Emprestimo mapearEmprestimo(ResultSet rs) throws SQLException {
        Date dataDevolucao = rs.getDate("data_devolucao");

        return new Emprestimo(
                rs.getInt("id"),
                rs.getInt("exemplar_id"),
                rs.getInt("usuario_id"),
                rs.getDate("data_emprestimo").toLocalDate(),
                rs.getDate("data_devolucao_prevista").toLocalDate(),
                dataDevolucao != null ? dataDevolucao.toLocalDate() : null,
                Emprestimo.Status.valueOf(rs.getString("status"))
        );
    }
}