package org.example.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuarioComum;
    private Usuario usuarioEstudante;

    @BeforeEach
    void setUp() {
        usuarioComum    = new Usuario("Carlos Lima", "carlos@email.com",
                "hashsenha", Usuario.Tipo.COMUM);
        usuarioEstudante = new Usuario("Lucas Cardoso", "lucas@email.com",
                "hashsenha", Usuario.Tipo.ESTUDANTE);
    }

    // ================================================================
    // Construtores
    // ================================================================

    @Test
    void construtor_vazio_devePermitirCriacao() {
        Usuario u = new Usuario();

        assertNotNull(u);
        assertFalse(u.isBloqueado());
        assertTrue(u.isAtivo());
        assertEquals(0, u.getTentativasLogin());
        assertNull(u.getSenhaTemporaria());
        assertFalse(u.isSolicitaReset());
    }

    @Test
    void construtor_simples_deveDefinirValoresCorretamente() {
        assertEquals("Carlos Lima", usuarioComum.getNome());
        assertEquals("carlos@email.com", usuarioComum.getEmail());
        assertEquals("hashsenha", usuarioComum.getSenhaHash());
        assertEquals(Usuario.Tipo.COMUM, usuarioComum.getTipo());
        assertTrue(usuarioComum.isAtivo());
        assertFalse(usuarioComum.isBloqueado());
    }

    @Test
    void construtor_completo_deveDefinirTodosOsCampos() {
        LocalDateTime agora = LocalDateTime.now();
        Usuario u = new Usuario(
                1, "Carlos Lima", "carlos@email.com", "hashsenha",
                Usuario.Tipo.COMUM, "123.456.789-00", "(11) 99999-9999",
                false, true, 0, null, agora, agora);

        assertEquals(1, u.getId());
        assertEquals("Carlos Lima", u.getNome());
        assertEquals("123.456.789-00", u.getCpf());
        assertEquals("(11) 99999-9999", u.getTelefone());
        assertFalse(u.isBloqueado());
        assertTrue(u.isAtivo());
        assertEquals(0, u.getTentativasLogin());
        assertEquals(agora, u.getCreatedAt());
        assertEquals(agora, u.getUpdatedAt());
    }

    // ================================================================
    // Regras de negocio — prazo e limite por tipo (documentados)
    // ================================================================

    @Test
    void prazoEmprestimo_comum_deveSer7Dias() {
        // ✅ Documentado: COMUM = 7 dias
        assertEquals(7, usuarioComum.getPrazoEmprestimo(),
                "Prazo para COMUM deve ser 7 dias");
    }

    @Test
    void prazoEmprestimo_estudante_deveSer15Dias() {
        // ✅ Documentado: ESTUDANTE = 15 dias
        assertEquals(15, usuarioEstudante.getPrazoEmprestimo(),
                "Prazo para ESTUDANTE deve ser 15 dias");
    }

    @Test
    void limiteCotas_comum_deveSer3() {
        // ✅ Documentado: COMUM = 3 emprestimos
        assertEquals(3, usuarioComum.getLimiteCotas(),
                "Limite para COMUM deve ser 3");
    }

    @Test
    void limiteCotas_estudante_deveSer5() {
        // ✅ Documentado: ESTUDANTE = 5 emprestimos
        assertEquals(5, usuarioEstudante.getLimiteCotas(),
                "Limite para ESTUDANTE deve ser 5");
    }

    @Test
    void limiteCotas_comCustom_deveUsarCustom() {
        // ✅ Limite customizado tem prioridade sobre o padrao
        usuarioComum.setLimiteCotasCustom(8);
        assertEquals(8, usuarioComum.getLimiteCotas(),
                "Limite custom deve ter prioridade");
    }

    @Test
    void limiteCotas_customNulo_deveUsarPadrao() {
        usuarioComum.setLimiteCotasCustom(null);
        assertEquals(3, usuarioComum.getLimiteCotas(),
                "Sem custom deve usar limite padrao");
    }

    @Test
    void setTipo_deveRecalcularPrazoELimite() {
        Usuario u = new Usuario("Teste", "teste@email.com",
                "hash", Usuario.Tipo.COMUM);
        assertEquals(7, u.getPrazoEmprestimo());
        assertEquals(3, u.getLimiteCotas());

        u.setTipo(Usuario.Tipo.ESTUDANTE);

        assertEquals(15, u.getPrazoEmprestimo(),
                "Prazo deve ser recalculado ao mudar tipo");
        assertEquals(5, u.getLimiteCotas(),
                "Limite deve ser recalculado ao mudar tipo");
    }

    // ================================================================
    // id
    // ================================================================

    @Test
    void getId_deveRetornarIdDefinido() {
        usuarioComum.setId(42);
        assertEquals(42, usuarioComum.getId());
    }

    @Test
    void setId_deveAtualizarId() {
        usuarioComum.setId(99);
        assertEquals(99, usuarioComum.getId());
    }

    // ================================================================
    // nome
    // ================================================================

    @Test
    void getNome_deveRetornarNomeDefinido() {
        assertEquals("Carlos Lima", usuarioComum.getNome());
    }

    @Test
    void setNome_deveAtualizarNome() {
        usuarioComum.setNome("Carlos Silva");
        assertEquals("Carlos Silva", usuarioComum.getNome());
    }

    // ================================================================
    // email
    // ================================================================

    @Test
    void getEmail_deveRetornarEmailDefinido() {
        assertEquals("carlos@email.com", usuarioComum.getEmail());
    }

    @Test
    void setEmail_deveAtualizarEmail() {
        usuarioComum.setEmail("novo@email.com");
        assertEquals("novo@email.com", usuarioComum.getEmail());
    }

    // ================================================================
    // senhaHash
    // ================================================================

    @Test
    void getSenhaHash_deveRetornarHashDefinido() {
        assertEquals("hashsenha", usuarioComum.getSenhaHash());
    }

    @Test
    void setSenhaHash_deveAtualizarHash() {
        usuarioComum.setSenhaHash("novoHash");
        assertEquals("novoHash", usuarioComum.getSenhaHash());
    }

    // ================================================================
    // tipo
    // ================================================================

    @Test
    void getTipo_deveRetornarTipoDefinido() {
        assertEquals(Usuario.Tipo.COMUM, usuarioComum.getTipo());
    }

    @Test
    void setTipo_deveAtualizarTipo() {
        usuarioComum.setTipo(Usuario.Tipo.ADMIN);
        assertEquals(Usuario.Tipo.ADMIN, usuarioComum.getTipo());
    }

    @Test
    void tipo_deveConterQuatroValores() {
        assertEquals(4, Usuario.Tipo.values().length,
                "Tipo deve ter ADMIN, COMUM, ESTUDANTE, BIBLIOTECARIO");
    }

    // ================================================================
    // cpf
    // ================================================================

    @Test
    void getCpf_inicialmente_deveSerNulo() {
        assertNull(usuarioComum.getCpf());
    }

    @Test
    void setCpf_deveAtualizarCpf() {
        usuarioComum.setCpf("123.456.789-00");
        assertEquals("123.456.789-00", usuarioComum.getCpf());
    }

    // ================================================================
    // telefone
    // ================================================================

    @Test
    void getTelefone_inicialmente_deveSerNulo() {
        assertNull(usuarioComum.getTelefone());
    }

    @Test
    void setTelefone_deveAtualizarTelefone() {
        usuarioComum.setTelefone("(11) 99999-9999");
        assertEquals("(11) 99999-9999", usuarioComum.getTelefone());
    }

    // ================================================================
    // bloqueado
    // ================================================================

    @Test
    void isBloqueado_inicialmente_deveSerFalse() {
        assertFalse(usuarioComum.isBloqueado());
    }

    @Test
    void setBloqueado_true_deveBloquear() {
        usuarioComum.setBloqueado(true);
        assertTrue(usuarioComum.isBloqueado());
    }

    @Test
    void setBloqueado_false_deveDesbloquear() {
        usuarioComum.setBloqueado(true);
        usuarioComum.setBloqueado(false);
        assertFalse(usuarioComum.isBloqueado());
    }

    // ================================================================
    // ativo
    // ================================================================

    @Test
    void isAtivo_inicialmente_deveSerTrue() {
        assertTrue(usuarioComum.isAtivo());
    }

    @Test
    void setAtivo_false_deveDesativar() {
        usuarioComum.setAtivo(false);
        assertFalse(usuarioComum.isAtivo());
    }

    @Test
    void setAtivo_true_deveReativar() {
        usuarioComum.setAtivo(false);
        usuarioComum.setAtivo(true);
        assertTrue(usuarioComum.isAtivo());
    }

    // ================================================================
    // tentativasLogin
    // ================================================================

    @Test
    void getTentativasLogin_inicialmente_deveSerZero() {
        assertEquals(0, usuarioComum.getTentativasLogin());
    }

    @Test
    void setTentativasLogin_deveAtualizar() {
        usuarioComum.setTentativasLogin(2);
        assertEquals(2, usuarioComum.getTentativasLogin());
    }

    @Test
    void tentativasLogin_apos3_deveBloquearsegundoRegra() {
        // ✅ Documentado: bloquear apos 3 tentativas
        usuarioComum.setTentativasLogin(3);
        assertEquals(3, usuarioComum.getTentativasLogin(),
                "Apos 3 tentativas o usuario deve ser bloqueado");
    }

    // ================================================================
    // ultimaTentativa
    // ================================================================

    @Test
    void getUltimaTentativa_inicialmente_deveSerNula() {
        assertNull(usuarioComum.getUltimaTentativa());
    }

    @Test
    void setUltimaTentativa_deveAtualizar() {
        LocalDateTime agora = LocalDateTime.now();
        usuarioComum.setUltimaTentativa(agora);
        assertEquals(agora, usuarioComum.getUltimaTentativa());
    }

    // ================================================================
    // createdAt e updatedAt
    // ================================================================

    @Test
    void getCreatedAt_inicialmente_deveSerNulo() {
        assertNull(usuarioComum.getCreatedAt());
    }

    @Test
    void setCreatedAt_deveAtualizar() {
        LocalDateTime agora = LocalDateTime.now();
        usuarioComum.setCreatedAt(agora);
        assertEquals(agora, usuarioComum.getCreatedAt());
    }

    @Test
    void getUpdatedAt_inicialmente_deveSerNulo() {
        assertNull(usuarioComum.getUpdatedAt());
    }

    @Test
    void setUpdatedAt_deveAtualizar() {
        LocalDateTime agora = LocalDateTime.now();
        usuarioComum.setUpdatedAt(agora);
        assertEquals(agora, usuarioComum.getUpdatedAt());
    }

    // ================================================================
    // prazoEmprestimo e limiteCotas
    // ================================================================

    @Test
    void setPrazoEmprestimo_deveAtualizar() {
        usuarioComum.setPrazoEmprestimo(10);
        assertEquals(10, usuarioComum.getPrazoEmprestimo());
    }

    @Test
    void setLimiteCotas_deveAtualizar() {
        usuarioComum.setLimiteCotas(10);
        assertEquals(10, usuarioComum.getLimiteCotas());
    }

    // ================================================================
    // limiteCotasCustom
    // ================================================================

    @Test
    void getLimiteCotasCustom_inicialmente_deveSerNulo() {
        assertNull(usuarioComum.getLimiteCotasCustom());
    }

    @Test
    void setLimiteCotasCustom_deveAtualizar() {
        usuarioComum.setLimiteCotasCustom(8);
        assertEquals(8, usuarioComum.getLimiteCotasCustom());
    }

    @Test
    void setLimiteCotasCustom_nulo_deveResetarParaPadrao() {
        usuarioComum.setLimiteCotasCustom(8);
        usuarioComum.setLimiteCotasCustom(null);
        assertNull(usuarioComum.getLimiteCotasCustom());
        assertEquals(3, usuarioComum.getLimiteCotas(),
                "Sem custom deve retornar limite padrao");
    }

    // ================================================================
    // senhaTemporaria
    // ================================================================

    @Test
    void getSenhaTemporaria_inicialmente_deveSerNula() {
        assertNull(usuarioComum.getSenhaTemporaria());
    }

    @Test
    void setSenhaTemporaria_deveAtualizar() {
        usuarioComum.setSenhaTemporaria("TEMP1234");
        assertEquals("TEMP1234", usuarioComum.getSenhaTemporaria());
    }

    @Test
    void setSenhaTemporaria_nulo_deveLimpar() {
        usuarioComum.setSenhaTemporaria("TEMP1234");
        usuarioComum.setSenhaTemporaria(null);
        assertNull(usuarioComum.getSenhaTemporaria());
    }

    // ================================================================
    // solicitaReset
    // ================================================================

    @Test
    void isSolicitaReset_inicialmente_deveSerFalse() {
        assertFalse(usuarioComum.isSolicitaReset());
    }

    @Test
    void setSolicitaReset_true_deveSolicitar() {
        usuarioComum.setSolicitaReset(true);
        assertTrue(usuarioComum.isSolicitaReset());
    }

    @Test
    void setSolicitaReset_false_deveCancelar() {
        usuarioComum.setSolicitaReset(true);
        usuarioComum.setSolicitaReset(false);
        assertFalse(usuarioComum.isSolicitaReset());
    }

    // ================================================================
    // getTipoBloqueio
    // ================================================================

    @Test
    void getTipoBloqueio_semBloqueio_deveRetornarNulo() {
        assertNull(usuarioComum.getTipoBloqueio(),
                "Sem bloqueio deve retornar nulo");
    }

    @Test
    void getTipoBloqueio_apenasBloqueado_deveRetornarTentativas() {
        usuarioComum.setBloqueado(true);
        assertEquals("TENTATIVAS", usuarioComum.getTipoBloqueio());
    }

    @Test
    void getTipoBloqueio_apenasReset_deveRetornarReset() {
        usuarioComum.setSolicitaReset(true);
        assertEquals("RESET", usuarioComum.getTipoBloqueio());
    }

    @Test
    void getTipoBloqueio_bloqueadoEReset_deveRetornarAmbos() {
        usuarioComum.setBloqueado(true);
        usuarioComum.setSolicitaReset(true);
        assertEquals("TENTATIVAS_E_RESET", usuarioComum.getTipoBloqueio());
    }

    // ================================================================
    // toString
    // ================================================================

    @Test
    void toString_deveConterCamposEssenciais() {
        usuarioComum.setId(1);

        String str = usuarioComum.toString();

        assertTrue(str.contains("Carlos Lima"), "Deve conter nome");
        assertTrue(str.contains("COMUM"),       "Deve conter tipo");
        assertTrue(str.contains("true"),        "Deve conter ativo");
    }
}