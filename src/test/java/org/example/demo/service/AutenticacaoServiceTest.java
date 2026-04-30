package org.example.demo.service;

import org.example.demo.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoServiceTest {

    private AutenticacaoService autenticacaoService;

    @BeforeEach
    void setUp() {
        autenticacaoService = new AutenticacaoService();
    }

    // ✅ Testes de gerarHash — não precisam do banco
    @Test
    void gerarHash_comSenhaValida_deveRetornarHash() {
        String hash = AutenticacaoService.gerarHash("admin123");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    void gerarHash_deveRetornar64Caracteres() {
        String hash = AutenticacaoService.gerarHash("admin123");
        assertEquals(64, hash.length());
    }

    @Test
    void gerarHash_mesmaSenha_deveRetornarMesmoHash() {
        String hash1 = AutenticacaoService.gerarHash("senha123");
        String hash2 = AutenticacaoService.gerarHash("senha123");
        assertEquals(hash1, hash2);
    }

    @Test
    void gerarHash_senhasDiferentes_deveRetornarHashesDiferentes() {
        String hash1 = AutenticacaoService.gerarHash("senha123");
        String hash2 = AutenticacaoService.gerarHash("senha456");
        assertNotEquals(hash1, hash2);
    }

    // ✅ Testes de autenticar — validações sem banco
    @Test
    void autenticar_comEmailNulo_deveRetornarNulo() {
        Usuario resultado = autenticacaoService.autenticar(null, "senha123");
        assertNull(resultado);
        assertEquals(AutenticacaoService.ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO,
                autenticacaoService.getUltimoResultado());
    }

    @Test
    void autenticar_comEmailVazio_deveRetornarNulo() {
        Usuario resultado = autenticacaoService.autenticar("", "senha123");
        assertNull(resultado);
        assertEquals(AutenticacaoService.ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO,
                autenticacaoService.getUltimoResultado());
    }

    @Test
    void autenticar_comSenhaNula_deveRetornarNulo() {
        Usuario resultado = autenticacaoService.autenticar("admin@biblioteca.com", null);
        assertNull(resultado);
        assertEquals(AutenticacaoService.ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO,
                autenticacaoService.getUltimoResultado());
    }

    @Test
    void autenticar_comSenhaVazia_deveRetornarNulo() {
        Usuario resultado = autenticacaoService.autenticar("admin@biblioteca.com", "");
        assertNull(resultado);
        assertEquals(AutenticacaoService.ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO,
                autenticacaoService.getUltimoResultado());
    }

    // ✅ Teste getUltimoResultado — estado inicial
    @Test
    void getUltimoResultado_semAutenticar_deveSerNulo() {
        assertNull(autenticacaoService.getUltimoResultado());
    }
}