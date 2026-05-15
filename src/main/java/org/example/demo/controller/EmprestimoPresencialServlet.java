package org.example.demo.controller;

import org.example.demo.model.*;
import org.example.demo.service.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/emprestimo-presencial")
public class EmprestimoPresencialServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final UsuarioService usuarioService = new UsuarioService();
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

        // ✅ Busca usuário pelo email se fornecido
        String emailBusca = request.getParameter("emailUsuario");
        if (emailBusca != null && !emailBusca.isBlank()) {
            Usuario usuarioBuscado = usuarioService.buscarPorEmail(emailBusca);
            if (usuarioBuscado != null) {
                request.setAttribute("usuarioBuscado", usuarioBuscado);

                // Busca exemplares disponíveis
                int unidadeId = 0;
                if (usuarioLogado instanceof UsuarioBibliotecario bib) {
                    unidadeId = bib.getUnidadeId();
                }

                List<Livro> livros = livroService.listarTodos();
                request.setAttribute("livros", livros);
                request.setAttribute("unidadeId", unidadeId);
            } else {
                request.setAttribute("erroUsuario", "Usuário não encontrado.");
            }
        }

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.getRequestDispatcher("/emprestimo_presencial.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        String acao = request.getParameter("acao");

        try {
            if ("registrar".equals(acao)) {
                int usuarioId  = Integer.parseInt(request.getParameter("usuarioId"));
                int livroId    = Integer.parseInt(request.getParameter("livroId"));

                int unidadeId = 0;
                if (usuarioLogado instanceof UsuarioBibliotecario bib) {
                    unidadeId = bib.getUnidadeId();
                }

                // Busca exemplar disponível do livro na unidade
                var exemplares = exemplarService
                        .buscarDisponiveisPorLivroEUnidade(livroId, unidadeId);

                if (exemplares.isEmpty()) {
                    throw new IllegalStateException(
                            "Nenhum exemplar disponível para este livro na unidade.");
                }

                // Pega o primeiro exemplar disponível
                int exemplarId = exemplares.get(0).getId();
                emprestimoService.realizarEmprestimo(exemplarId, usuarioId);

                response.sendRedirect(request.getContextPath()
                        + "/admin/emprestimo-presencial?sucesso=true");

            } else if ("devolver".equals(acao)) {
                int emprestimoId = Integer.parseInt(request.getParameter("emprestimoId"));
                emprestimoService.finalizarEmprestimo(emprestimoId);

                response.sendRedirect(request.getContextPath()
                        + "/admin/emprestimos?devolucao=sucesso");
            }

        } catch (IllegalStateException | IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/emprestimo-presencial?erro=" + e.getMessage());
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/emprestimo-presencial?erro=Erro ao processar. Tente novamente.");
        }
    }
}