package org.example.demo.service;

import org.example.demo.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class AutenticacaoServiceTest {

    private AutenticacaoService autenticacaoService;

    // ✅ Hash correto do admin123 — SHA-256
    private static final String HASH_ADMIN123 =
            "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9";

    @BeforeEach
    void setUp() {
        autenticacaoService = new AutenticacaoService();
    }

    @Test
    void gerarHash_comSenhaValida_deveRetornarHashNaoNulo() {
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

    @Test
    void gerarHash_admin123_deveRetornarHashConhecido() {
        String hash = AutenticacaoService.gerarHash("admin123");
        assertEquals(HASH_ADMIN123, hash,
                "Hash do admin123 deve corresponder ao valor no banco");
    }

    @Test
    void gerarHash_deveRetornarApenasCaracteresHexadecimais() {
        String hash = AutenticacaoService.gerarHash("qualquerSenha");
        assertTrue(hash.matches("[0-9a-f]+"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "senha muito longa com espacos e caracteres especiais !@#$%", "123"})
    void gerarHash_comDiversasSenhas_sempreRetorna64Caracteres(String senha) {
        String hash = AutenticacaoService.gerarHash(senha);
        assertEquals(64, hash.length());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void autenticar_comEmailNuloOuVazio_deveRetornarNulo(String email) {
        Usuario resultado = autenticacaoService.autenticar(email, "senha123");
        assertNull(resultado);
        assertEquals(AutenticacaoService.ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO,
                autenticacaoService.getUltimoResultado());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void autenticar_comSenhaNulaOuVazia_deveRetornarNulo(String senha) {
        Usuario resultado = autenticacaoService.autenticar("admin@biblioteca.com", senha);
        assertNull(resultado);
        assertEquals(AutenticacaoService.ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO,
                autenticacaoService.getUltimoResultado());
    }

    @Test
    void autenticar_comEmailESenhaBrancos_deveRetornarNulo() {
        Usuario resultado = autenticacaoService.autenticar("   ", "   ");
        assertNull(resultado);
        assertEquals(AutenticacaoService.ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO,
                autenticacaoService.getUltimoResultado());
    }

    @Test
    void getUltimoResultado_semAutenticar_deveSerNulo() {
        assertNull(autenticacaoService.getUltimoResultado());
    }

    @Test
    void getUltimoResultado_aposAutenticarComEmailVazio_deveSerUsuarioNaoEncontrado() {
        autenticacaoService.autenticar("", "senha");
        assertEquals(AutenticacaoService.ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO,
                autenticacaoService.getUltimoResultado());
    }

    @Test
    void resultadoAutenticacao_deveConterTodosOsValoresEsperados() {
        var valores = AutenticacaoService.ResultadoAutenticacao.values();
        assertEquals(5, valores.length);
    }
}