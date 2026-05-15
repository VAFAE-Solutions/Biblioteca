package org.example.demo.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static HikariDataSource dataSource;

    static {
        try {
            // ✅ Registra o driver MySQL explicitamente
            Class.forName("com.mysql.cj.jdbc.Driver");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DatabaseConfig.getUrl());
            config.setUsername(DatabaseConfig.getUsuario());
            config.setPassword(DatabaseConfig.getSenha());
            config.setMaximumPoolSize(DatabaseConfig.getMaxConnections());
            config.setMinimumIdle(DatabaseConfig.getMinIdle());
            config.setConnectionTimeout(DatabaseConfig.getConnectionTimeout());
            config.setIdleTimeout(DatabaseConfig.getIdleTimeout());
            config.setConnectionTestQuery("SELECT 1");
            config.setDriverClassName("com.mysql.cj.jdbc.Driver"); // ✅ força o driver
            config.setPoolName("BibliotecaPool");

            dataSource = new HikariDataSource(config);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL não encontrado: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar pool: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void fechar() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}