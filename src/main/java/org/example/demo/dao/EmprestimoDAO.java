package org.example.demo.dao;

import org.example.demo.Database;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmprestimoDAO {

    public boolean registrarEmprestimo(int usuarioId, int livroId) {
        String sqlExemplar = "SELECT id FROM exemplar WHERE livro_id = ? AND status = 'DISPONIVEL' LIMIT 1";
        String sqlEmprestimo = "INSERT INTO emprestimo (exemplar_id, usuario_id, data_emprestimo, data_devolucao_prevista, status) VALUES (?, ?, ?, ?, 'ATIVO')";
        String sqlUpdateExemplar = "UPDATE exemplar SET status = 'EMPRESTADO' WHERE id = ?";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            try {
                int exemplarId = -1;

                try (PreparedStatement stmtEx = conn.prepareStatement(sqlExemplar)) {
                    stmtEx.setInt(1, livroId);
                    ResultSet rs = stmtEx.executeQuery();
                    if (rs.next()) {
                        exemplarId = rs.getInt("id");
                    }
                }

                if (exemplarId == -1) {
                    System.err.println("Nenhum exemplar disponível para o livro ID: " + livroId);
                    return false;
                }

                LocalDate hoje = LocalDate.now();
                LocalDate entrega = hoje.plusDays(7);

                try (PreparedStatement stmtEmp = conn.prepareStatement(sqlEmprestimo)) {
                    stmtEmp.setInt(1, exemplarId);
                    stmtEmp.setInt(2, usuarioId);
                    stmtEmp.setDate(3, Date.valueOf(hoje));
                    stmtEmp.setDate(4, Date.valueOf(entrega));
                    stmtEmp.executeUpdate();
                }

                try (PreparedStatement stmtUp = conn.prepareStatement(sqlUpdateExemplar)) {
                    stmtUp.setInt(1, exemplarId);
                    stmtUp.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Erro na transação de empréstimo: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, String>> listarEmprestimosPorUsuario(int usuarioId) {
        List<Map<String, String>> lista = new ArrayList<>();
        String sql = "SELECT e.id, l.titulo, e.data_emprestimo, e.data_devolucao_prevista, e.status " +
                "FROM emprestimo e " +
                "JOIN exemplar ex ON e.exemplar_id = ex.id " +
                "JOIN livros l ON ex.livro_id = l.id " +
                "WHERE e.usuario_id = ? AND e.status != 'FINALIZADO' " +
                "ORDER BY e.data_devolucao_prevista ASC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, String> emp = new HashMap<>();
                emp.put("id", rs.getString("id"));
                emp.put("titulo", rs.getString("titulo"));
                emp.put("data_saida", rs.getString("data_emprestimo"));
                emp.put("data_entrega", rs.getString("data_devolucao_prevista"));
                emp.put("status", rs.getString("status"));
                lista.add(emp);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * NOVO MÉTODO: Calcula o valor total de multas acumuladas por atraso.
     * Regra: R$ 2,00 por cada dia de atraso em empréstimos ATIVOS.
     */
    public double calcularMultaTotal(int usuarioId) {
        double total = 0;
        // DATEDIFF retorna a diferença em dias entre HOJE e a DATA PREVISTA
        String sql = "SELECT DATEDIFF(CURDATE(), data_devolucao_prevista) AS dias " +
                "FROM emprestimo " +
                "WHERE usuario_id = ? AND status != 'FINALIZADO' " +
                "AND data_devolucao_prevista < CURDATE()";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int diasAtraso = rs.getInt("dias");
                if (diasAtraso > 0) {
                    total += diasAtraso * 2.00;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular multas: " + e.getMessage());
        }
        return total;
    }
}