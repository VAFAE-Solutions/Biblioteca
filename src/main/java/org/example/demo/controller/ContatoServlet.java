package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.MensagemContatoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/contato")
public class ContatoServlet extends HttpServlet {

    private final MensagemContatoService mensagemService = new MensagemContatoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogado") != null) {
            Usuario u = (Usuario) session.getAttribute("usuarioLogado");
            request.setAttribute("usuarioLogado", u);
            request.setAttribute("nomePreenchido", u.getNome());
            request.setAttribute("emailPreenchido", u.getEmail());
            request.setAttribute("usuarioId", u.getId());
        }

        request.getRequestDispatcher("/contato.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = session != null
                ? (Usuario) session.getAttribute("usuarioLogado") : null;

        try {
            String nome     = request.getParameter("nome");
            String email    = request.getParameter("email");
            String assunto  = request.getParameter("assunto");
            String mensagem = request.getParameter("mensagem");

            // ✅ Se logado, usa email completo da conta
            if (usuarioLogado != null) {
                email = usuarioLogado.getEmail();
            }

            Integer usuarioId = usuarioLogado != null ? usuarioLogado.getId() : null;

            mensagemService.enviar(usuarioId, nome, email, assunto, mensagem);

            response.sendRedirect(request.getContextPath() + "/contato?enviado=sucesso");

        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            if (usuarioLogado != null) {
                request.setAttribute("usuarioLogado", usuarioLogado);
                request.setAttribute("nomePreenchido", usuarioLogado.getNome());
                request.setAttribute("emailPreenchido", usuarioLogado.getEmail());
            }
            request.getRequestDispatcher("/contato.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao enviar mensagem. Tente novamente.");
            request.getRequestDispatcher("/contato.jsp").forward(request, response);
        }
    }
}