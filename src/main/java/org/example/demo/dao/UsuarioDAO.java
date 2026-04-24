package org.example.demo.dao;

import org.example.demo.Database;
import java.sql.*;

public class UsuarioDAO {

    /**
     * Valida o login via Procedure e busca os dados do usuário.
     * @return String[] onde [0]=status, [1]=cpf, [2]=tipo, [3]=id_numérico.
     */
    public String[] validarLogin(String email, String senha) {
        // Aumentamos para 4 posições para incluir o ID que o banco exige nos empréstimos
        String[] retorno = {"erro", "", "", ""};
        String sqlProcedure = "{call sp_autenticar_usuario(?, ?, ?, ?, ?)}";

        try (Connection conn = Database.getConnection()) {

            // 1. Executa a Procedure de Autenticação
            try (CallableStatement stmt = conn.prepareCall(sqlProcedure)) {
                stmt.setString(1, email);
                stmt.setString(2, senha);
                stmt.registerOutParameter(3, Types.VARCHAR); // p_status_id
                stmt.registerOutParameter(4, Types.INTEGER); // p_usuario_id
                stmt.registerOutParameter(5, Types.VARCHAR); // p_tipo

                stmt.execute();

                retorno[0] = stmt.getString(3); // Status
                retorno[2] = stmt.getString(5); // Tipo
                retorno[3] = String.valueOf(stmt.getInt(4)); // ID numérico (Crucial para a Sprint 3)

                System.out.println(">>> DEBUG PROCEDURE: Status = " + retorno[0] + " | ID = " + retorno[3]);
            }

            // 2. Se o login foi sucesso, busca o CPF na tabela 'usuario'
            if ("login_sucesso".equals(retorno[0])) {
                // AJUSTE: Tabela alterada de 'usuarios' para 'usuario' conforme seu script SQL
                String sqlCpf = "SELECT cpf FROM usuario WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlCpf)) {
                    pstmt.setString(1, email.trim());

                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            retorno[1] = rs.getString("cpf");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println(">>> DEBUG ERRO SQL: " + e.getMessage());
            e.printStackTrace();
        }
        return retorno;
    }
}