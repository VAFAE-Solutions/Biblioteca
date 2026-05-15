package org.example.demo.dao;

import org.example.demo.database.Database;
import org.example.demo.model.FilaReserva;
import org.example.demo.model.Livro;
import org.example.demo.model.Reserva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public boolean inserir(Reserva reserva) {
        String sql = """
                INSERT INTO reservas (livro_id, usuario_id, unidade_id, data_reserva, status, posicao_fila)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, reserva.getLivroId());
            ps.setInt(2, reserva.getUsuarioId());
            ps.setInt(3, reserva.getUnidadeId());
            ps.setDate(4, Date.valueOf(reserva.getDataReserva()));
            ps.setString(5, reserva.getStatus().name());
            ps.setInt(6, reserva.getPosicaoFila());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    reserva.setId(keys.getInt(1));
                }
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir reserva: " + e.getMessage(), e);
        }
    }

    public Reserva buscarPorId(int id) {
        String sql = """
                SELECT r.*, l.titulo as livro_titulo, l.capa_url as livro_capa
                FROM reservas r
                JOIN livros l ON r.livro_id = l.id
                WHERE r.id = ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearReservaComLivro(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reserva por ID: " + e.getMessage(), e);
        }
    }

    public List<Reserva> buscarPorUsuario(int usuarioId) {
        String sql = """
                SELECT r.*, l.titulo as livro_titulo, l.capa_url as livro_capa
                FROM reservas r
                JOIN livros l ON r.livro_id = l.id
                WHERE r.usuario_id = ?
                ORDER BY r.posicao_fila
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            List<Reserva> reservas = new ArrayList<>();

            while (rs.next()) {
                reservas.add(mapearReservaComLivro(rs));
            }
            return reservas;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas por usuário: " + e.getMessage(), e);
        }
    }

    public FilaReserva buscarFilaPorLivroEUnidade(int livroId, int unidadeId) {
        String sql = """
                SELECT r.*, l.titulo as livro_titulo, l.capa_url as livro_capa
                FROM reservas r
                JOIN livros l ON r.livro_id = l.id
                WHERE r.livro_id = ? AND r.unidade_id = ? AND r.status = 'AGUARDANDO'
                ORDER BY r.posicao_fila
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, livroId);
            ps.setInt(2, unidadeId);
            ResultSet rs = ps.executeQuery();

            FilaReserva fila = new FilaReserva(livroId, unidadeId);
            List<Reserva> reservas = new ArrayList<>();

            while (rs.next()) {
                reservas.add(mapearReservaComLivro(rs));
            }
            fila.setFila(reservas);
            return fila;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar fila de reservas: " + e.getMessage(), e);
        }
    }

    // ✅ Novo método — busca todas as reservas aguardando de uma unidade
    public List<Reserva> buscarAguardandoPorUnidade(int unidadeId) {
        String sql = """
                SELECT r.*, l.titulo as livro_titulo, l.capa_url as livro_capa
                FROM reservas r
                JOIN livros l ON r.livro_id = l.id
                WHERE r.unidade_id = ? AND r.status = 'AGUARDANDO'
                ORDER BY r.posicao_fila
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, unidadeId);
            ResultSet rs = ps.executeQuery();
            List<Reserva> reservas = new ArrayList<>();

            while (rs.next()) {
                reservas.add(mapearReservaComLivro(rs));
            }
            return reservas;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas por unidade: " + e.getMessage(), e);
        }
    }

    public boolean atualizar(Reserva reserva) {
        String sql = "UPDATE reservas SET status = ? WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reserva.getStatus().name());
            ps.setInt(2, reserva.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar reserva: " + e.getMessage(), e);
        }
    }

    private Reserva mapearReservaComLivro(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva(
                rs.getInt("id"),
                rs.getInt("livro_id"),
                rs.getInt("usuario_id"),
                rs.getInt("unidade_id"),
                rs.getDate("data_reserva").toLocalDate(),
                Reserva.Status.valueOf(rs.getString("status")),
                rs.getInt("posicao_fila")
        );

        Livro livro = new Livro();
        livro.setId(rs.getInt("livro_id"));
        livro.setTitulo(rs.getString("livro_titulo"));
        livro.setCapaUrl(rs.getString("livro_capa"));

        reserva.setLivro(livro);
        return reserva;
    }
}