package org.example.demo.dao;

import org.example.demo.database.Database;

import java.sql.*;
import java.util.*;

public class RelatorioDAO {

    // ✅ Livros mais emprestados
    public List<Map<String, Object>> livrosMaisEmprestados(int limite) {
        String sql = """
                SELECT l.titulo, l.autor, COUNT(e.id) as total_emprestimos
                FROM emprestimo e
                JOIN exemplar ex ON e.exemplar_id = ex.id
                JOIN livros l ON ex.livro_id = l.id
                GROUP BY l.id, l.titulo, l.autor
                ORDER BY total_emprestimos DESC
                LIMIT ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> resultado = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("titulo", rs.getString("titulo"));
                row.put("autor", rs.getString("autor"));
                row.put("totalEmprestimos", rs.getInt("total_emprestimos"));
                resultado.add(row);
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros mais emprestados: " + e.getMessage(), e);
        }
    }

    // ✅ Taxa de atraso
    public Map<String, Object> taxaAtraso() {
        String sql = """
                SELECT
                    COUNT(*) as total,
                    SUM(CASE WHEN status = 'ATRASADO' THEN 1 ELSE 0 END) as atrasados,
                    SUM(CASE WHEN status = 'ATIVO' THEN 1 ELSE 0 END) as ativos,
                    SUM(CASE WHEN status = 'FINALIZADO' THEN 1 ELSE 0 END) as finalizados
                FROM emprestimo
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            Map<String, Object> resultado = new LinkedHashMap<>();

            if (rs.next()) {
                int total      = rs.getInt("total");
                int atrasados  = rs.getInt("atrasados");
                int ativos     = rs.getInt("ativos");
                int finalizados = rs.getInt("finalizados");
                double taxa    = total > 0 ? (atrasados * 100.0 / total) : 0;

                resultado.put("total", total);
                resultado.put("atrasados", atrasados);
                resultado.put("ativos", ativos);
                resultado.put("finalizados", finalizados);
                resultado.put("taxaAtraso", String.format("%.1f", taxa));
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao calcular taxa de atraso: " + e.getMessage(), e);
        }
    }

    // ✅ Empréstimos por usuário
    public List<Map<String, Object>> emprestimosPorUsuario(int limite) {
        String sql = """
                SELECT u.nome, u.email, u.tipo,
                       COUNT(e.id) as total_emprestimos,
                       SUM(CASE WHEN e.status = 'ATRASADO' THEN 1 ELSE 0 END) as atrasados
                FROM emprestimo e
                JOIN usuario u ON e.usuario_id = u.id
                GROUP BY u.id, u.nome, u.email, u.tipo
                ORDER BY total_emprestimos DESC
                LIMIT ?
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> resultado = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("nome", rs.getString("nome"));
                row.put("email", rs.getString("email"));
                row.put("tipo", rs.getString("tipo"));
                row.put("totalEmprestimos", rs.getInt("total_emprestimos"));
                row.put("atrasados", rs.getInt("atrasados"));
                resultado.add(row);
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empréstimos por usuário: " + e.getMessage(), e);
        }
    }

    // ✅ Livros atrasados
    public List<Map<String, Object>> livrosAtrasados() {
        String sql = """
                SELECT l.titulo, l.autor, u.nome as usuario_nome,
                       e.data_devolucao_prevista,
                       DATEDIFF(NOW(), e.data_devolucao_prevista) as dias_atraso
                FROM emprestimo e
                JOIN exemplar ex ON e.exemplar_id = ex.id
                JOIN livros l ON ex.livro_id = l.id
                JOIN usuario u ON e.usuario_id = u.id
                WHERE e.status = 'ATRASADO'
                ORDER BY dias_atraso DESC
                """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> resultado = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("titulo", rs.getString("titulo"));
                row.put("autor", rs.getString("autor"));
                row.put("usuarioNome", rs.getString("usuario_nome"));
                row.put("dataPrevista", rs.getDate("data_devolucao_prevista").toLocalDate());
                row.put("diasAtraso", rs.getInt("dias_atraso"));
                resultado.add(row);
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar livros atrasados: " + e.getMessage(), e);
        }
    }
    // ✅ Resumo de multas
    public Map<String, Object> resumoMultas() {
        String sql = """
            SELECT
                COUNT(*) as total,
                SUM(CASE WHEN pago = 0 THEN 1 ELSE 0 END) as pendentes,
                SUM(CASE WHEN pago = 1 THEN 1 ELSE 0 END) as pagas,
                SUM(CASE WHEN pago = 0 THEN valor ELSE 0 END) as total_pendente,
                SUM(CASE WHEN pago = 1 THEN valor ELSE 0 END) as total_arrecadado
            FROM multa
            """;

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            Map<String, Object> resultado = new LinkedHashMap<>();

            if (rs.next()) {
                resultado.put("total", rs.getInt("total"));
                resultado.put("pendentes", rs.getInt("pendentes"));
                resultado.put("pagas", rs.getInt("pagas"));
                resultado.put("totalPendente", rs.getBigDecimal("total_pendente") != null
                        ? rs.getBigDecimal("total_pendente") : java.math.BigDecimal.ZERO);
                resultado.put("totalArrecadado", rs.getBigDecimal("total_arrecadado") != null
                        ? rs.getBigDecimal("total_arrecadado") : java.math.BigDecimal.ZERO);
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar resumo de multas: " + e.getMessage(), e);
        }
    }
}