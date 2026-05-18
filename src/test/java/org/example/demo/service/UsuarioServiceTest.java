package org.example.demo.service;

import org.example.demo.dao.UsuarioDAO;
import org.example.demo.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioDAO usuarioDAO;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioComum;
    private Usuario usuarioEstudante;
    private UsuarioBibliotecario bibliotecario;

    @BeforeEach
    void setUp() {
        usuarioComum = new Usuario();
        usuarioComum.setId(1);
        usuarioComum.setNome("Carlos Lima");
        usuarioComum.setEmail("carlos@email.com");
        usuarioComum.setSenhaHash("hashvalido");
        usuarioComum.setTipo(Usuario.Tipo.COMUM);

        usuarioEstudante = new UsuarioEstudante();
        usuarioEstudante.setId(2);
        usuarioEstudante.setNome("Lucas Cardoso");
        usuarioEstudante.setEmail("lucas@email.com");
        usuarioEstudante.setSenhaHash("hashvalido");
        usuarioEstudante.setTipo(Usuario.Tipo.ESTUDANTE);
        ((UsuarioEstudante) usuarioEstudante).setRa(2402432);

        bibliotecario = new UsuarioBibliotecario();
        bibliotecario.setId(3);
        bibliotecario.setNome("Anderson Jesus");
        bibliotecario.setEmail("ander@biblioteca.com");
        bibliotecario.setSenhaHash("hashvalido");
        bibliotecario.setTipo(Usuario.Tipo.BIBLIOTECARIO);
        bibliotecario.setUnidadeId(1);
    }

    // ================================================================
    // cadastrar — validacoes
    // ================================================================

    @Test
    void cadastrar_comUsuarioNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(null));
    }

    @Test
    void cadastrar_semNome_deveLancarExcecao() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenhaHash("senha123");
        usuario.setTipo(Usuario.Tipo.COMUM);
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(usuario));
    }

    @Test
    void cadastrar_comNomeVazio_deveLancarExcecao() {
        Usuario usuario = new Usuario();
        usuario.setNome("   ");
        usuario.setEmail("teste@email.com");
        usuario.setSenhaHash("senha123");
        usuario.setTipo(Usuario.Tipo.COMUM);
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(usuario));
    }

    @Test
    void cadastrar_semEmail_deveLancarExcecao() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setSenhaHash("senha123");
        usuario.setTipo(Usuario.Tipo.COMUM);
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(usuario));
    }

    @Test
    void cadastrar_semSenha_deveLancarExcecao() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setTipo(Usuario.Tipo.COMUM);
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(usuario));
    }

    @Test
    void cadastrar_semTipo_deveLancarExcecao() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenhaHash("senha123");
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(usuario));
    }

    @Test
    void cadastrar_estudanteSemRa_deveLancarExcecao() {
        // ✅ Documentado: RA obrigatorio para estudante
        UsuarioEstudante estudante = new UsuarioEstudante();
        estudante.setNome("Joao");
        estudante.setEmail("joao@email.com");
        estudante.setSenhaHash("senha123");
        estudante.setRa(0);
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(estudante));
    }

    @Test
    void cadastrar_bibliotecarioSemUnidade_deveLancarExcecao() {
        UsuarioBibliotecario bib = new UsuarioBibliotecario();
        bib.setNome("Maria");
        bib.setEmail("maria@email.com");
        bib.setSenhaHash("senha123");
        bib.setUnidadeId(0);
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(bib));
    }

    @Test
    void cadastrar_emailJaCadastrado_deveLancarExcecao() {
        when(usuarioDAO.buscarPorEmail("carlos@email.com"))
                .thenReturn(usuarioComum);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(usuarioComum),
                "Email duplicado deve lancar excecao");
    }

    @Test
    void cadastrar_usuarioComum_comDadosValidos_deveInserir() {
        when(usuarioDAO.buscarPorEmail("carlos@email.com")).thenReturn(null);
        when(usuarioDAO.inserir(usuarioComum)).thenReturn(true);

        boolean resultado = usuarioService.cadastrar(usuarioComum);

        assertTrue(resultado);
        verify(usuarioDAO).inserir(usuarioComum);
    }

    @Test
    void cadastrar_estudanteComRa_comDadosValidos_deveInserir() {
        when(usuarioDAO.buscarPorEmail("lucas@email.com")).thenReturn(null);
        when(usuarioDAO.inserir(usuarioEstudante)).thenReturn(true);

        boolean resultado = usuarioService.cadastrar(usuarioEstudante);

        assertTrue(resultado);
        verify(usuarioDAO).inserir(usuarioEstudante);
    }

    // ================================================================
    // buscarPorEmail — validacoes
    // ================================================================

    @ParameterizedTest
    @NullAndEmptySource
    void buscarPorEmail_comEmailNuloOuVazio_deveLancarExcecao(String email) {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.buscarPorEmail(email));
    }

    @Test
    void buscarPorEmail_comEmailValido_deveRetornarUsuario() {
        when(usuarioDAO.buscarPorEmail("carlos@email.com"))
                .thenReturn(usuarioComum);

        Usuario resultado = usuarioService.buscarPorEmail("carlos@email.com");

        assertNotNull(resultado);
        assertEquals("Carlos Lima", resultado.getNome());
    }

    @Test
    void buscarPorEmail_naoEncontrado_deveRetornarNulo() {
        when(usuarioDAO.buscarPorEmail("naoexiste@email.com"))
                .thenReturn(null);

        Usuario resultado = usuarioService.buscarPorEmail("naoexiste@email.com");

        assertNull(resultado);
    }

    // ================================================================
    // buscarPorId — validacoes
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void buscarPorId_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.buscarPorId(id));
    }

    @Test
    void buscarPorId_comIdValido_deveRetornarUsuario() {
        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);

        Usuario resultado = usuarioService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    // ================================================================
    // atualizar — validacoes
    // ================================================================

    @Test
    void atualizar_comUsuarioNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.atualizar(null));
    }

    @Test
    void atualizar_comUsuarioSemId_deveLancarExcecao() {
        Usuario usuario = new Usuario();
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.atualizar(usuario));
    }

    @Test
    void atualizar_comDadosValidos_deveRetornarTrue() {
        when(usuarioDAO.atualizar(usuarioComum)).thenReturn(true);

        boolean resultado = usuarioService.atualizar(usuarioComum);

        assertTrue(resultado);
        verify(usuarioDAO).atualizar(usuarioComum);
    }

    // ================================================================
    // bloquear e desbloquear
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void bloquear_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.bloquear(id));
    }

    @Test
    void bloquear_usuarioNaoEncontrado_deveLancarExcecao() {
        when(usuarioDAO.buscarPorId(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.bloquear(99));
    }

    @Test
    void bloquear_usuarioExistente_deveBloquear() {
        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);
        when(usuarioDAO.atualizarBloqueio(1, true,
                usuarioComum.getTentativasLogin())).thenReturn(true);

        boolean resultado = usuarioService.bloquear(1);

        assertTrue(resultado);
        verify(usuarioDAO).atualizarBloqueio(1, true,
                usuarioComum.getTentativasLogin());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void desbloquear_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.desbloquear(id));
    }

    @Test
    void desbloquear_usuarioExistente_deveDesbloquearEZerarTentativas() {
        usuarioComum.setBloqueado(true);
        usuarioComum.setTentativasLogin(3);

        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);
        when(usuarioDAO.atualizarBloqueio(1, false, 0)).thenReturn(true);

        boolean resultado = usuarioService.desbloquear(1);

        assertTrue(resultado);
        verify(usuarioDAO).atualizarBloqueio(1, false, 0);
    }

    // ================================================================
    // desativar e reativar
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void desativar_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.desativar(id));
    }

    @Test
    void desativar_usuarioNaoEncontrado_deveLancarExcecao() {
        when(usuarioDAO.buscarPorId(99)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.desativar(99));
    }

    @Test
    void desativar_usuarioExistente_deveDesativar() {
        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);
        when(usuarioDAO.desativar(1)).thenReturn(true);

        boolean resultado = usuarioService.desativar(1);

        assertTrue(resultado);
        verify(usuarioDAO).desativar(1);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void reativar_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.reativar(id));
    }

    @Test
    void reativar_comIdValido_deveReativar() {
        when(usuarioDAO.reativar(1)).thenReturn(true);

        boolean resultado = usuarioService.reativar(1);

        assertTrue(resultado);
        verify(usuarioDAO).reativar(1);
    }

    // ================================================================
    // ajustarLimiteCotas
    // ================================================================

    @Test
    void ajustarLimiteCotas_limiteAcima20_deveLancarExcecao() {
        // ✅ Documentado: limite maximo 20
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.ajustarLimiteCotas(1, 21));
    }

    @Test
    void ajustarLimiteCotas_limiteZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.ajustarLimiteCotas(1, 0));
    }

    @Test
    void ajustarLimiteCotas_limiteNegativo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.ajustarLimiteCotas(1, -1));
    }

    @Test
    void ajustarLimiteCotas_paraAdmin_deveLancarExcecao() {
        // ✅ Admin nao pode ter limite ajustado
        Usuario admin = new UsuarioAdministrador();
        admin.setId(1);
        admin.setNome("Admin");

        when(usuarioDAO.buscarPorId(1)).thenReturn(admin);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.ajustarLimiteCotas(1, 5),
                "Admin nao deve ter limite de cotas ajustado");
    }

    @Test
    void ajustarLimiteCotas_paraBibliotecario_deveLancarExcecao() {
        when(usuarioDAO.buscarPorId(3)).thenReturn(bibliotecario);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.ajustarLimiteCotas(3, 5),
                "Bibliotecario nao deve ter limite de cotas ajustado");
    }

    @Test
    void ajustarLimiteCotas_paraEstudante_deveAjustar() {
        when(usuarioDAO.buscarPorId(2)).thenReturn(usuarioEstudante);
        when(usuarioDAO.ajustarLimiteCotas(2, 8)).thenReturn(true);

        boolean resultado = usuarioService.ajustarLimiteCotas(2, 8);

        assertTrue(resultado);
        verify(usuarioDAO).ajustarLimiteCotas(2, 8);
    }

    // ================================================================
    // solicitarReset
    // ================================================================

    @ParameterizedTest
    @NullAndEmptySource
    void solicitarReset_comEmailNuloOuVazio_deveLancarExcecao(String email) {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.solicitarReset(email));
    }

    @Test
    void solicitarReset_emailNaoEncontrado_deveLancarExcecao() {
        when(usuarioDAO.buscarPorEmail("naoexiste@email.com"))
                .thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.solicitarReset("naoexiste@email.com"));
    }

    @Test
    void solicitarReset_emailValido_deveSolicitar() {
        when(usuarioDAO.buscarPorEmail("carlos@email.com"))
                .thenReturn(usuarioComum);
        when(usuarioDAO.solicitarReset(1)).thenReturn(true);

        boolean resultado = usuarioService.solicitarReset("carlos@email.com");

        assertTrue(resultado);
        verify(usuarioDAO).solicitarReset(1);
    }

    // ================================================================
    // aprovarReset
    // ================================================================

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100})
    void aprovarReset_comIdInvalido_deveLancarExcecao(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.aprovarReset(id));
    }

    @Test
    void aprovarReset_comIdValido_deveRetornarSenhaTemporaria() {
        when(usuarioDAO.aprovarReset(anyInt(), anyString())).thenReturn(true);

        String senhaTemp = usuarioService.aprovarReset(1);

        assertNotNull(senhaTemp);
        assertEquals(8, senhaTemp.length(),
                "Senha temporaria deve ter 8 caracteres");
        assertEquals(senhaTemp.toUpperCase(), senhaTemp,
                "Senha temporaria deve ser maiuscula");
    }

    // ================================================================
    // atualizarSenha
    // ================================================================

    @Test
    void atualizarSenha_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.atualizarSenha(-1, "senhaAtual", "novaSenha"));
    }

    @Test
    void atualizarSenha_novaSenhaCurta_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.atualizarSenha(1, "senhaAtual", "abc"),
                "Nova senha com menos de 6 caracteres deve lancar excecao");
    }

    @Test
    void atualizarSenha_senhaAtualIncorreta_deveLancarExcecao() {
        // ✅ Hash de "senhaCorreta"
        String hashCorreto = AutenticacaoService.gerarHash("senhaCorreta");
        usuarioComum.setSenhaHash(hashCorreto);

        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.atualizarSenha(1, "senhaErrada", "novaSenha123"),
                "Senha atual incorreta deve lancar excecao");
    }

    @Test
    void atualizarSenha_comDadosValidos_deveAtualizarSenha() {
        String hashAtual = AutenticacaoService.gerarHash("senhaAtual123");
        usuarioComum.setSenhaHash(hashAtual);

        when(usuarioDAO.buscarPorId(1)).thenReturn(usuarioComum);
        when(usuarioDAO.atualizarSenha(eq(1), anyString())).thenReturn(true);

        boolean resultado = usuarioService.atualizarSenha(
                1, "senhaAtual123", "novaSenha123");

        assertTrue(resultado);
        verify(usuarioDAO).atualizarSenha(eq(1), anyString());
    }

    // ================================================================
    // listarTodos
    // ================================================================

    @Test
    void listarTodos_deveRetornarLista() {
        when(usuarioDAO.listarTodos()).thenReturn(List.of(usuarioComum));

        var resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(usuarioDAO).listarTodos();
    }

    @Test
    void listarTodosIncluindoInativos_deveRetornarTodos() {
        when(usuarioDAO.listarTodosIncluindoInativos())
                .thenReturn(List.of(usuarioComum, usuarioEstudante));

        var resultado = usuarioService.listarTodosIncluindoInativos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioDAO).listarTodosIncluindoInativos();
    }

    @Test
    void listarBloqueadosOuComReset_deveRetornarApenasBloqueados() {
        usuarioComum.setBloqueado(true);
        when(usuarioDAO.listarBloqueadosOuComReset())
                .thenReturn(List.of(usuarioComum));

        var resultado = usuarioService.listarBloqueadosOuComReset();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(usuarioDAO).listarBloqueadosOuComReset();
    }
}