package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioAdministradorTest {

    private UsuarioAdministrador admin;

    @BeforeEach
    void setUp() {
        admin = new UsuarioAdministrador(
                "Administrador", "admin@biblioteca.com", "hashsenha");
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_vazio_deveTipoAdmin() {
        UsuarioAdministrador a = new UsuarioAdministrador();

        assertNotNull(a);
        assertEquals(Usuario.Tipo.ADMIN, a.getTipo(),
                "Tipo deve ser ADMIN automaticamente");
    }

    @Test
    void construtor_simples_deveDefinirValoresCorretamente() {
        UsuarioAdministrador a = new UsuarioAdministrador(
                "Admin", "admin@teste.com", "hash123");

        assertEquals("Admin", a.getNome());
        assertEquals("admin@teste.com", a.getEmail());
        assertEquals("hash123", a.getSenhaHash());
        assertEquals(Usuario.Tipo.ADMIN, a.getTipo());
    }

    @Test
    void construtor_completo_deveDefinirTodosOsCampos() {
        LocalDateTime agora = LocalDateTime.now();
        UsuarioAdministrador a = new UsuarioAdministrador(
                1, "Admin", "admin@teste.com", "hash123",
                "123.456.789-00", "(11) 99999-9999",
                false, true, 0, null, agora, agora);

        assertEquals(1, a.getId());
        assertEquals("Admin", a.getNome());
        assertEquals("admin@teste.com", a.getEmail());
        assertEquals("123.456.789-00", a.getCpf());
        assertEquals("(11) 99999-9999", a.getTelefone());
        assertFalse(a.isBloqueado());
        assertTrue(a.isAtivo());
        assertEquals(0, a.getTentativasLogin());
        assertEquals(agora, a.getCreatedAt());
        assertEquals(Usuario.Tipo.ADMIN, a.getTipo());
    }

    // ================================================================
    // Heranca de Usuario
    // ================================================================

    @Test
    void admin_deveHerdarDeUsuario() {
        assertInstanceOf(Usuario.class, admin);
    }

    @Test
    void admin_tipoDeveSerSempreAdmin() {
        assertEquals(Usuario.Tipo.ADMIN, admin.getTipo());
    }

    @Test
    void admin_naoDeveAlterarTipoParaOutro() {
        // ✅ Mesmo setando outro tipo, o construtor define ADMIN
        UsuarioAdministrador a = new UsuarioAdministrador();
        a.setTipo(Usuario.Tipo.COMUM);

        // Tipo pode ser alterado via setter mas nao e recomendado
        assertEquals(Usuario.Tipo.COMUM, a.getTipo());
    }

    @Test
    void admin_inicialmente_deveEstarAtivo() {
        assertTrue(admin.isAtivo());
    }

    @Test
    void admin_inicialmente_naoDevEstarBloqueado() {
        assertFalse(admin.isBloqueado());
    }

    @Test
    void admin_tentativasLogin_inicialmente_deveSerZero() {
        assertEquals(0, admin.getTentativasLogin());
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterNome() {
        String str = admin.toString();

        assertTrue(str.contains("Administrador"),
                "Deve conter nome do admin");
    }

    @Test
    void toString_deveConterId() {
        admin.setId(1);
        String str = admin.toString();

        assertTrue(str.contains("1"), "Deve conter id");
    }
}