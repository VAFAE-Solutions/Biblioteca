package org.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection getConnection() {
        try {
            // Carrega o driver do MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Configurações: substitua pelo nome do seu banco e sua senha do Workbench
            String url = "jdbc:mysql://localhost:3306/biblioteca";
            String user = "root";
            String password = "POj0055@to";

            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erro na conexão: " + e.getMessage());
            return null;
        }
    }
}