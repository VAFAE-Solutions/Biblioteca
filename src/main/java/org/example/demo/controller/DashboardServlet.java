package org.example.demo.controller;

import org.example.demo.dao.LivroDAO;
import org.example.demo.model.Livro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardServlet", value = "/dashboard")
public class DashboardServlet extends HttpServlet {

    private LivroDAO livroDAO = new LivroDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Verifica se existe uma sessão ativa e se o usuário está logado (Segurança)
        HttpSession session = request.getSession(false);

        // Ajuste: Verificamos se a sessão existe E se o atributo 'usuarioLogado' não é nulo
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            System.out.println(">>> DASHBOARD DEBUG: Acesso negado. Redirecionando para login.");
            response.sendRedirect("login.jsp?erro=acesso_negado");
            return; // Interrompe a execução para não carregar o resto
        }

        // DEBUG para console (ajudará você a ver se o Map está chegando certo)
        System.out.println(">>> DASHBOARD DEBUG: Usuário Logado: " + session.getAttribute("usuarioLogado"));

        // 2. Captura o termo de busca vindo do formulário (name="txtBusca")
        String txtBusca = request.getParameter("txtBusca");
        List<Livro> lista;

        // 3. Lógica de decisão: Busca filtrada ou Listagem total
        if (txtBusca != null && !txtBusca.trim().isEmpty()) {
            System.out.println(">>> DASHBOARD DEBUG: Realizando busca por: " + txtBusca);
            lista = livroDAO.buscarLivros(txtBusca);
        } else {
            System.out.println(">>> DASHBOARD DEBUG: Carregando todos os livros.");
            lista = livroDAO.listarTodos();
        }

        // 4. Envia os dados para o JSP
        request.setAttribute("livros", lista);
        request.setAttribute("termoPesquisado", txtBusca);

        // 5. Encaminha para o JSP do Dashboard
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}