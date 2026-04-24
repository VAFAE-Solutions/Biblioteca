package org.example.demo.controller;

import org.example.demo.dao.LivroDAO;
import org.example.demo.model.Livro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BuscaServlet", value = "/buscar")
public class BuscaServlet extends HttpServlet {

    private LivroDAO livroDAO = new LivroDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Pega o que o usuário digitou no campo de busca (txtBusca)
        String termo = request.getParameter("txtBusca");

        List<Livro> listaResultados;

        // 2. Lógica de segurança: se o termo estiver vazio, traz tudo, senão filtra
        if (termo == null || termo.trim().isEmpty()) {
            listaResultados = livroDAO.listarTodos();
        } else {
            listaResultados = livroDAO.buscarLivros(termo);
        }

        // 3. Pendura a lista na "mochila" (request) para o JSP exibir
        request.setAttribute("livros", listaResultados);

        // 4. Manda de volta para o Dashboard com os novos resultados
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}