package org.example.demo.controller;

import org.example.demo.dao.LivroDAO;
import org.example.demo.model.Livro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/detalhes")
public class DetalhesServlet extends HttpServlet {

    private LivroDAO livroDAO = new LivroDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                Livro livro = livroDAO.buscarPorId(id);

                if (livro != null) {
                    request.setAttribute("livro", livro);
                    request.getRequestDispatcher("livro.jsp").forward(request, response);
                } else {
                    response.sendRedirect("dashboard");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("dashboard");
            }
        } else {
            response.sendRedirect("dashboard");
        }
    }
}