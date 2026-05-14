package org.example.demo.controller;

import org.example.demo.model.Exemplar;
import org.example.demo.model.Livro;
import org.example.demo.model.Usuario;
import org.example.demo.model.UsuarioBibliotecario;
import org.example.demo.service.ExemplarService;
import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/estoque")
public class EstoqueUnidadeServlet extends HttpServlet {

    private final ExemplarService exemplarService = new ExemplarService();
    private final LivroService livroService = new LivroService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo() != Usuario.Tipo.ADMIN &&
                usuarioLogado.getTipo() != Usuario.Tipo.BIBLIOTECARIO) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        int unidadeId = 0;
        if (usuarioLogado instanceof UsuarioBibliotecario bib) {
            unidadeId = bib.getUnidadeId();
        }

        List<Livro> livros = livroService.listarTodos();

        // ✅ Popula o livro em cada exemplar
        List<Exemplar> exemplares = livros.stream()
                .flatMap(l -> {
                    List<Exemplar> exs = exemplarService.buscarPorLivro(l.getId());
                    exs.forEach(ex -> ex.setLivro(l));
                    return exs.stream();
                })
                .toList();

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.setAttribute("exemplares", exemplares);
        request.setAttribute("livros", livros);
        request.setAttribute("unidadeId", unidadeId);

        request.getRequestDispatcher("/estoque_unidade.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        int unidadeId = 0;
        if (usuarioLogado instanceof UsuarioBibliotecario bib) {
            unidadeId = bib.getUnidadeId();
        }

        try {
            int livroId = Integer.parseInt(request.getParameter("livroId"));
            String codigoPatrimonio = request.getParameter("codigoPatrimonio");

            Exemplar exemplar = new Exemplar(livroId, unidadeId, codigoPatrimonio);
            exemplarService.cadastrar(exemplar);

            response.sendRedirect(request.getContextPath()
                    + "/admin/estoque?cadastro=sucesso");

        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/estoque?erro=" + e.getMessage());
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/estoque?erro=Erro ao cadastrar exemplar.");
        }
    }
}