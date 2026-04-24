package org.example.demo.controller;

import org.example.demo.dao.EmprestimoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "EmprestimoServlet", value = "/reservar")
public class EmprestimoServlet extends HttpServlet {
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // 1. Recupera o Map do usuário da sessão
        Map<String, String> usuario = (Map<String, String>) (session != null ? session.getAttribute("usuarioLogado") : null);
        String livroIdParam = request.getParameter("id");

        if (usuario != null && livroIdParam != null) {
            try {
                // AJUSTE: Pegamos o ID numérico (PK) que veio do banco, pois a FK de empréstimo usa ID
                int usuarioId = Integer.parseInt(usuario.get("id"));
                int livroId = Integer.parseInt(livroIdParam);

                // 2. Chama o DAO passando os IDs numéricos conforme o seu Script SQL
                boolean sucesso = emprestimoDAO.registrarEmprestimo(usuarioId, livroId);

                if (sucesso) {
                    response.sendRedirect("detalhes?id=" + livroId + "&reserva=sucesso");
                } else {
                    response.sendRedirect("detalhes?id=" + livroId + "&reserva=erro");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("dashboard?erro=dados_invalidos");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}