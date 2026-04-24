package org.example.demo.controller;

import org.example.demo.dao.EmprestimoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MeusEmprestimosServlet", value = "/meus-emprestimos")
public class MeusEmprestimosServlet extends HttpServlet {
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Recupera o Map do usuário logado para pegar o ID
        Map<String, String> usuario = (Map<String, String>) (session != null ? session.getAttribute("usuarioLogado") : null);

        if (usuario != null) {
            try {
                int usuarioId = Integer.parseInt(usuario.get("id"));

                // 1. Busca a lista de empréstimos ativos
                List<Map<String, String>> lista = emprestimoDAO.listarEmprestimosPorUsuario(usuarioId);

                // 2. AJUSTE: Calcula o valor total de multas por atraso
                double valorMulta = emprestimoDAO.calcularMultaTotal(usuarioId);

                // 3. Envia os dados para o JSP
                request.setAttribute("emprestimos", lista);
                request.setAttribute("totalMulta", valorMulta); // Enviando o valor calculado

                request.getRequestDispatcher("meus_emprestimos.jsp").forward(request, response);

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("dashboard?erro=lista_falhou");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}