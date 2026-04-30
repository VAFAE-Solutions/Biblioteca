package org.example.demo.dao;

import org.example.demo.Database;
import org.example.demo.model.Multa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MultaDAO {

    public boolean inserir(Multa multa) {
        String sql = """
                INSERT INTO multa (emprestimo_id, usuario_id, valor, pago, data_geracao)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, multa.getEmprestimoId());
            ps.setInt(2, multa.getUsuarioId());
            ps.setBigDecimal(3, multa.getValor());
            ps.setBoolean(4, multa.isPago());
            ps.setDate(5, Date.valueOf(multa.getDataGeracao()));

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    multa.setId(keys.getInt(1));
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir multa: " + e.getMessage(), e);
        }
    }

    public Multa buscarPorId(int id) {
        String sql = "SELECT * FROM multa WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearMulta(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar multa por ID: " + e.getMessage(), e);
        }
    }

    public List<Multa> buscarPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM multa WHERE usuario_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            List<Multa> multas = new ArrayList<>();

            while (rs.next()) {
                multas.add(mapearMulta(rs));
            }
            return multas;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar multas por usuário: " + e.getMessage(), e);
        }
    }

    public List<Multa> buscarPendentesPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM multa WHERE usuario_id = ? AND pago = false";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            List<Multa> multas = new ArrayList<>();

            while (rs.next()) {
                multas.add(mapearMulta(rs));
            }
            return multas;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar multas pendentes: " + e.getMessage(), e);
        }
    }

    public boolean existeMultaPorEmprestimo(int emprestimoId) {
        String sql = "SELECT COUNT(*) FROM multa WHERE emprestimo_id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, emprestimoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar multa: " + e.getMessage(), e);
        }
    }

    public boolean atualizar(Multa multa) {
        String sql = """
                UPDATE multa SET pago = ?, data_pagamento = ?
                WHERE id = ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, multa.isPago());
            ps.setDate(2, multa.getDataPagamento() != null
                    ? Date.valueOf(multa.getDataPagamento()) : null);
            ps.setInt(3, multa.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar multa: " + e.getMessage(), e);
        }
    }

    private Multa mapearMulta(ResultSet rs) throws SQLException {
        Date dataPagamento = rs.getDate("data_pagamento");

        return new Multa(
                rs.getInt("id"),
                rs.getInt("emprestimo_id"),
                rs.getInt("usuario_id"),
                rs.getBigDecimal("valor"),
                rs.getBoolean("pago"),
                rs.getDate("data_geracao").toLocalDate(),
                dataPagamento != null ? dataPagamento.toLocalDate() : null
        );
    }
}
