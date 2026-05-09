package org.example.demo.filter;

import org.example.demo.model.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebFilter("/*")
public class FiltroAutenticacao implements Filter {

    private static final List<String> ROTAS_PUBLICAS = List.of(
            "/login",
            "/logout",
            "/home",
            "/detalhes",
            "/buscar",
            "/cadastro",
            "/cadastro-completo",
            "/css",
            "/js",
            "/imagens"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request   = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String uri         = request.getRequestURI();
        String contextPath = request.getContextPath();

        // Verifica se é rota pública
        boolean rotaPublica = ROTAS_PUBLICAS.stream()
                .anyMatch(rota -> uri.startsWith(contextPath + rota));

        if (rotaPublica) {
            chain.doFilter(req, resp);
            return;
        }

        // Verifica se há sessão ativa
        HttpSession session   = request.getSession(false);
        Usuario usuarioLogado = session != null
                ? (Usuario) session.getAttribute("usuarioLogado")
                : null;

        if (usuarioLogado == null) {
            response.sendRedirect(contextPath + "/login");
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}