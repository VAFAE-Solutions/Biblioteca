package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtém a sessão se ela existir
        HttpSession session = request.getSession(false);

        if (session != null) {
            // 2. Destrói a sessão e remove todos os atributos (email, cpf, etc)
            session.invalidate();
            System.out.println(">>> LOGOUT DEBUG: Sessão encerrada com sucesso.");
        }

        // 3. Redireciona para a página inicial (index.jsp)
        // Isso garante que a URL mude de /logout para /index.jsp, limpando a tela branca
        response.sendRedirect("index.jsp");
    }
}