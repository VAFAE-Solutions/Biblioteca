package org.example.demo.filter;

import org.example.demo.model.Usuario;
import org.example.demo.service.MensagemContatoService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/admin/*")
public class FiltroNotificacoes implements Filter {

    private final MensagemContatoService mensagemService = new MensagemContatoService();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("usuarioLogado") != null) {
            Usuario u = (Usuario) session.getAttribute("usuarioLogado");
            if (u.getTipo() == Usuario.Tipo.ADMIN ||
                    u.getTipo() == Usuario.Tipo.BIBLIOTECARIO) {
                request.setAttribute("totalNaoLidas",
                        mensagemService.contarNaoLidas());
            }
        }

        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}