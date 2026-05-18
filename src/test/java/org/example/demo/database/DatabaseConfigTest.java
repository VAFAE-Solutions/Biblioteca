package org.example.demo.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConfigTest {

    @Test
    void getUrl_deveConterJdbcEBiblioteca() {
        String url = DatabaseConfig.getUrl();
        assertNotNull(url);
        assertTrue(url.startsWith("jdbc:"));
        assertTrue(url.contains("biblioteca"));
    }

    @Test
    void getUsuario_naoDeveSerVazio() {
        assertFalse(DatabaseConfig.getUsuario().isBlank());
    }

    @Test
    void getSenha_naoDeveSerNula() {
        assertNotNull(DatabaseConfig.getSenha());
    }

    @Test
    void getMaxConnections_deveSerPositivo() {
        assertTrue(DatabaseConfig.getMaxConnections() > 0);
    }

    @Test
    void getMinIdle_deveSrMenorOuIgualAoMaxConnections() {
        assertTrue(DatabaseConfig.getMinIdle() <= DatabaseConfig.getMaxConnections());
    }

    @Test
    void getConnectionTimeout_deveSerPositivo() {
        assertTrue(DatabaseConfig.getConnectionTimeout() > 0);
    }

    @Test
    void getIdleTimeout_deveSrMaiorQueConnectionTimeout() {
        assertTrue(DatabaseConfig.getIdleTimeout() > DatabaseConfig.getConnectionTimeout());
    }
}