package org.example.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.demo.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FiltroAutenticacaoTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private FilterChain chain;

    @InjectMocks
    private FiltroAutenticacao filtro;

    private static final String CONTEXT = "/demo";

    @BeforeEach
    void setUp() {
        when(request.getContextPath()).thenReturn(CONTEXT);
    }

    // ================================================================
    // Rotas publicas — devem passar sem autenticacao
    // ================================================================

    @ParameterizedTest
    @ValueSource(strings = {
            "/login", "/logout", "/home", "/cadastro", "/cadastro-completo",
            "/detalhes", "/buscar", "/sobre", "/generos", "/localizacao",
            "/duvidas", "/contato", "/esqueceu-senha"
    })
    void doFilter_rotaPublica_devePassarSemAutenticacao(String rota)
            throws Exception {

        when(request.getRequestURI()).thenReturn(CONTEXT + rota);

        filtro.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doFilter_raizDoProjeto_devePassarSemAutenticacao()
            throws Exception {

        when(request.getRequestURI()).thenReturn(CONTEXT + "/");

        filtro.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    // ================================================================
    // Rotas protegidas sem sessao — deve redirecionar para login
    // ================================================================

    @ParameterizedTest
    @ValueSource(strings = {
            "/dashboard", "/perfil", "/meus-emprestimos",
            "/minhas-reservas", "/multas", "/admin", "/admin/usuarios",
            "/admin/emprestimos", "/admin/relatorios"
    })
    void doFilter_rotaProtegida_semSessao_deveRedirecionarParaLogin(String rota)
            throws Exception {

        when(request.getRequestURI()).thenReturn(CONTEXT + rota);
        when(request.getSession(false)).thenReturn(null);

        filtro.doFilter(request, response, chain);

        verify(response).sendRedirect(CONTEXT + "/login");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void doFilter_rotaProtegida_sessaoSemUsuario_deveRedirecionarParaLogin()
            throws Exception {

        when(request.getRequestURI()).thenReturn(CONTEXT + "/dashboard");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogado")).thenReturn(null);

        filtro.doFilter(request, response, chain);

        verify(response).sendRedirect(CONTEXT + "/login");
        verify(chain, never()).doFilter(request, response);
    }

    // ================================================================
    // Rotas protegidas com sessao valida — deve passar
    // ================================================================

    @Test
    void doFilter_rotaProtegida_comSessaoValida_devePassar()
            throws Exception {

        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setId(1);
        usuarioLogado.setNome("Carlos Lima");
        usuarioLogado.setTipo(Usuario.Tipo.COMUM);

        when(request.getRequestURI()).thenReturn(CONTEXT + "/dashboard");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogado")).thenReturn(usuarioLogado);

        filtro.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doFilter_rotaAdmin_comAdminLogado_devePassar()
            throws Exception {

        Usuario admin = new Usuario();
        admin.setId(1);
        admin.setTipo(Usuario.Tipo.ADMIN);

        when(request.getRequestURI()).thenReturn(CONTEXT + "/admin");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogado")).thenReturn(admin);

        filtro.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doFilter_rotaAdmin_comBibliotecarioLogado_devePassar()
            throws Exception {

        Usuario bibliotecario = new Usuario();
        bibliotecario.setId(2);
        bibliotecario.setTipo(Usuario.Tipo.BIBLIOTECARIO);

        when(request.getRequestURI()).thenReturn(CONTEXT + "/admin/emprestimos");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("usuarioLogado")).thenReturn(bibliotecario);

        filtro.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    // ================================================================
    // init e destroy — ciclo de vida do filtro
    // ================================================================

    @Test
    void init_naoDeveLancarExcecao() {
        assertDoesNotThrow(() -> filtro.init(null));
    }

    @Test
    void destroy_naoDeveLancarExcecao() {
        assertDoesNotThrow(() -> filtro.destroy());
    }
}