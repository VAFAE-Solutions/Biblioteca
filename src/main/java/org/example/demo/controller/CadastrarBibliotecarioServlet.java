package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.model.UsuarioBibliotecario;
import org.example.demo.service.AutenticacaoService;
import org.example.demo.service.UnidadeService;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/cadastrar-bibliotecario")
public class CadastrarBibliotecarioServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final UnidadeService unidadeService = new UnidadeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo() != Usuario.Tipo.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.setAttribute("unidades", unidadeService.listarTodas());
        request.getRequestDispatcher("/cadastrar_bibliotecario.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        try {
            String nome     = request.getParameter("nome");
            String email    = request.getParameter("email");
            String senha    = request.getParameter("senha");
            String telefone = request.getParameter("telefone");
            String cpf      = request.getParameter("cpf");
            int unidadeId   = Integer.parseInt(request.getParameter("unidadeId"));

            UsuarioBibliotecario bibliotecario = new UsuarioBibliotecario();
            bibliotecario.setNome(nome);
            bibliotecario.setEmail(email);
            bibliotecario.setSenhaHash(AutenticacaoService.gerarHash(senha));
            bibliotecario.setTipo(Usuario.Tipo.BIBLIOTECARIO);
            bibliotecario.setTelefone(telefone);
            bibliotecario.setCpf(cpf);
            bibliotecario.setUnidadeId(unidadeId);

            usuarioService.cadastrar(bibliotecario);

            response.sendRedirect(request.getContextPath()
                    + "/admin/cadastrar-bibliotecario?cadastro=sucesso");

        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.setAttribute("usuarioLogado", usuarioLogado);
            request.setAttribute("unidades", unidadeService.listarTodas());
            request.getRequestDispatcher("/cadastrar_bibliotecario.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao cadastrar bibliotecário. Tente novamente.");
            request.setAttribute("usuarioLogado", usuarioLogado);
            request.setAttribute("unidades", unidadeService.listarTodas());
            request.getRequestDispatcher("/cadastrar_bibliotecario.jsp")
                    .forward(request, response);
        }
    }
}