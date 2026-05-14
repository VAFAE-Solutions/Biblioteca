package org.example.demo.controller;

import org.example.demo.model.Livro;
import org.example.demo.model.Usuario;
import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/editar-livro")
public class EditarLivroServlet extends HttpServlet {

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

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        Livro livro = livroService.buscarPorId(Integer.parseInt(idParam));
        if (livro == null) {
            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        request.setAttribute("livro", livro);
        request.setAttribute("usuarioLogado", usuarioLogado);
        request.getRequestDispatcher("/editar_livro.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        try {
            Livro livro = new Livro();
            livro.setId(Integer.parseInt(request.getParameter("id")));
            livro.setTitulo(request.getParameter("titulo"));
            livro.setAutor(request.getParameter("autor"));
            livro.setEditora(request.getParameter("editora"));
            livro.setGenero(request.getParameter("genero"));
            livro.setDescricao(request.getParameter("descricao"));
            livro.setSumario(request.getParameter("sumario"));
            livro.setCapaUrl(request.getParameter("capaUrl"));

            String anoParam = request.getParameter("anoPublicacao");
            if (anoParam != null && !anoParam.isBlank()) {
                livro.setAnoPublicacao(Integer.parseInt(anoParam));
            }

            livroService.atualizar(livro);

            response.sendRedirect(request.getContextPath()
                    + "/admin?edicao=sucesso");

        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.setAttribute("usuarioLogado", usuarioLogado);
            request.getRequestDispatcher("/editar_livro.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao editar livro. Tente novamente.");
            request.setAttribute("usuarioLogado", usuarioLogado);
            request.getRequestDispatcher("/editar_livro.jsp")
                    .forward(request, response);
        }
    }
}
