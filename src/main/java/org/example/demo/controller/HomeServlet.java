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

@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends HttpServlet {

    private LivroDAO livroDAO = new LivroDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Pega o que o usuário digitou na busca do index.jsp
        String txtBusca = request.getParameter("txtBusca");
        List<Livro> lista = null;

        // 2. Só consulta o banco se o usuário realmente digitou algo
        if (txtBusca != null && !txtBusca.trim().isEmpty()) {
            lista = livroDAO.buscarLivros(txtBusca);

            // Log para você conferir no console do IntelliJ se a busca funcionou
            System.out.println(">>> BUSCA INDEX: Termo = " + txtBusca + " | Encontrados = " + lista.size());
        }

        // 3. Devolve os resultados e o termo pesquisado para o JSP
        request.setAttribute("livros", lista);
        request.setAttribute("termoPesquisado", txtBusca);

        // 4. Manda de volta para o seu index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}