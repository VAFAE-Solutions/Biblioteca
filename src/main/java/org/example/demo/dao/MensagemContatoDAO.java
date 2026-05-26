package org.example.demo.dao;

import org.example.demo.database.Database;
import org.example.demo.model.MensagemContato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MensagemContatoDAO {

    public boolean inserir(MensagemContato mensagem) {
        String sql = """
                INSERT INTO mensagem_contato
                (usuario_id, nome, email, assunto, mensagem)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            if (mensagem.getUsuarioId() != null) {
                ps.setInt(1, mensagem.getUsuarioId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, mensagem.getNome());
            ps.setString(3, mensagem.getEmail());
            ps.setString(4, mensagem.getAssunto());
            ps.setString(5, mensagem.getMensagem());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) mensagem.setId(keys.getInt(1));
            }
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir mensagem: " + e.getMessage(), e);
        }
    }

    public List<MensagemContato> listarTodas() {
        String sql = "SELECT * FROM mensagem_contato ORDER BY data_envio DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            List<MensagemContato> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar mensagens: " + e.getMessage(), e);
        }
    }

    public List<MensagemContato> listarNaoLidas() {
        String sql = "SELECT * FROM mensagem_contato WHERE lida = FALSE ORDER BY data_envio DESC";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            List<MensagemContato> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar mensagens não lidas: " + e.getMessage(), e);
        }
    }

    public boolean marcarComoLida(int id) {
        String sql = "UPDATE mensagem_contato SET lida = TRUE WHERE id = ?";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao marcar mensagem como lida: " + e.getMessage(), e);
        }
    }

    public int contarNaoLidas() {
        String sql = "SELECT COUNT(*) FROM mensagem_contato WHERE lida = FALSE";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar mensagens: " + e.getMessage(), e);
        }
    }

    private MensagemContato mapear(ResultSet rs) throws SQLException {
        MensagemContato m = new MensagemContato();
        m.setId(rs.getInt("id"));
        m.setUsuarioId(rs.getObject("usuario_id") != null ? rs.getInt("usuario_id") : null);
        m.setNome(rs.getString("nome"));
        m.setEmail(rs.getString("email"));
        m.setAssunto(rs.getString("assunto"));
        m.setMensagem(rs.getString("mensagem"));
        m.setLida(rs.getBoolean("lida"));
        m.setDataEnvio(rs.getTimestamp("data_envio").toLocalDateTime());
        return m;
    }
}