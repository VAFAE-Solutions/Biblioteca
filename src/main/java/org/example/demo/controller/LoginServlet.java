package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.AutenticacaoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AutenticacaoService autenticacaoService = new AutenticacaoService();

    // Exibe a página de login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // Processa o formulário de login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Usuario usuario = autenticacaoService.autenticar(email, senha);

        switch (autenticacaoService.getUltimoResultado()) {
            case SUCESSO -> {
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", usuario);
                session.setMaxInactiveInterval(1800);

                // Redireciona conforme o tipo
                switch (usuario.getTipo()) {
                    case ADMIN,
                         BIBLIOTECARIO -> response.sendRedirect(
                            request.getContextPath() + "/admin");
                    case ESTUDANTE,
                         COMUM         -> response.sendRedirect(
                            request.getContextPath() + "/dashboard");
                }
            }
            case USUARIO_BLOQUEADO -> {
                request.setAttribute("erro", "usuario_bloqueado");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            case SENHA_INCORRETA -> {
                request.setAttribute("erro", "login_falhou");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            case USUARIO_NAO_ENCONTRADO -> {
                request.setAttribute("erro", "usuario_nao_encontrado");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }
    }
}