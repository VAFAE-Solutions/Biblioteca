package org.example.demo.service;

import org.example.demo.model.Usuario;
import org.example.demo.model.UsuarioEstudante;
import org.example.demo.model.UsuarioBibliotecario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService();
    }

    // ✅ Testes cadastrar — sem banco
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
        UsuarioEstudante estudante = new UsuarioEstudante();
        estudante.setNome("João");
        estudante.setEmail("joao@email.com");
        estudante.setSenhaHash("senha123");
        estudante.setRa(0); // RA inválido
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(estudante));
    }

    @Test
    void cadastrar_bibliotecarioSemUnidade_deveLancarExcecao() {
        UsuarioBibliotecario bibliotecario = new UsuarioBibliotecario();
        bibliotecario.setNome("Maria");
        bibliotecario.setEmail("maria@email.com");
        bibliotecario.setSenhaHash("senha123");
        bibliotecario.setUnidadeId(0); // unidade inválida
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar(bibliotecario));
    }

    // ✅ Testes buscarPorEmail — validações sem banco
    @Test
    void buscarPorEmail_comEmailNulo_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.buscarPorEmail(null));
    }

    @Test
    void buscarPorEmail_comEmailVazio_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.buscarPorEmail(""));
    }

    // ✅ Testes buscarPorId — validações sem banco
    @Test
    void buscarPorId_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.buscarPorId(-1));
    }

    @Test
    void buscarPorId_comIdZero_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.buscarPorId(0));
    }

    // ✅ Testes atualizar — validações sem banco
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

    // ✅ Testes bloquear/desbloquear — validações sem banco
    @Test
    void bloquear_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.bloquear(0));
    }

    @Test
    void desbloquear_comIdInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.desbloquear(0));
    }

    // ✅ Teste listarTodos — só valida instância
    @Test
    void listarTodos_serviceDeveEstarInstanciado() {
        assertNotNull(usuarioService);
    }
}