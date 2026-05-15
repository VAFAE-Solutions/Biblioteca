package org.example.demo.database;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    public static Connection getConnection() {
        try {
            return ConnectionPool.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter conexão do pool: " + e.getMessage(), e);
        }
    }
}