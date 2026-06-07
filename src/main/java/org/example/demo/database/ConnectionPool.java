package org.example.demo.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static HikariDataSource dataSource;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Tenta Railway primeiro
            String urlRailway = DatabaseConfig.getUrlRailway();
            if (urlRailway != null && !urlRailway.isBlank()) {
                try {
                    dataSource = criarPool(
                            urlRailway,
                            DatabaseConfig.getUsuarioRailway(),
                            DatabaseConfig.getSenhaRailway(),
                            "PoolRailway"
                    );
                    // Testa a conexão de fato
                    try (Connection c = dataSource.getConnection()) {
                        System.out.println("✅ Conectado ao Railway: " + urlRailway);
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Railway indisponível, usando banco local: " + e.getMessage());
                    if (dataSource != null) {
                        dataSource.close();
                        dataSource = null;
                    }
                }
            }

            // ✅ Fallback para banco local
            if (dataSource == null) {
                dataSource = criarPool(
                        DatabaseConfig.getUrlLocal(),
                        DatabaseConfig.getUsuarioLocal(),
                        DatabaseConfig.getSenhaLocal(),
                        "PoolLocal"
                );
                try (Connection c = dataSource.getConnection()) {
                    System.out.println("✅ Conectado ao banco local: " + DatabaseConfig.getUrlLocal());
                }
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL não encontrado: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar pool de conexões: " + e.getMessage(), e);
        }
    }

    private static HikariDataSource criarPool(String url, String usuario,
                                              String senha, String nome) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(usuario);
        config.setPassword(senha);
        config.setMaximumPoolSize(DatabaseConfig.getMaxConnections());
        config.setMinimumIdle(DatabaseConfig.getMinIdle());
        config.setConnectionTimeout(DatabaseConfig.getConnectionTimeout());
        config.setIdleTimeout(DatabaseConfig.getIdleTimeout());
        config.setConnectionTestQuery("SELECT 1");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPoolName(nome);
        return new HikariDataSource(config);
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