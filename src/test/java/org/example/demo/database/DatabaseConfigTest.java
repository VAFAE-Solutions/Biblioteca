package org.example.demo.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConfigTest {

    @Test
    void getUrlRailway_deveEstarPreenchida() {
        String url = DatabaseConfig.getUrlRailway();
        assertNotNull(url);
    }

    @Test
    void getUrlLocal_deveConterJdbcEBiblioteca() {
        String url = DatabaseConfig.getUrlLocal();
        assertNotNull(url);
        assertTrue(url.startsWith("jdbc:"));
        assertTrue(url.contains("biblioteca"));
    }

    @Test
    void getUsuarioLocal_naoDeveSerVazio() {
        assertFalse(DatabaseConfig.getUsuarioLocal().isBlank());
    }

    @Test
    void getSenhaLocal_naoDeveSerNula() {
        assertNotNull(DatabaseConfig.getSenhaLocal());
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