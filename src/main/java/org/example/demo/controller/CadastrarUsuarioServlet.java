package org.example.demo.controller;

import org.example.demo.model.*;
import org.example.demo.service.AutenticacaoService;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/cadastrar-usuario")
public class CadastrarUsuarioServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

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

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.getRequestDispatcher("/cadastrar_usuario.jsp")
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
            String tipo     = request.getParameter("tipo");

            // ✅ Só ADMIN pode cadastrar outro ADMIN
            if ("ADMIN".equals(tipo) && usuarioLogado.getTipo() != Usuario.Tipo.ADMIN) {
                throw new IllegalArgumentException("Apenas administradores podem cadastrar outros administradores.");
            }

            Usuario novoUsuario;

            switch (tipo) {
                case "ESTUDANTE" -> {
                    int ra = Integer.parseInt(request.getParameter("ra"));
                    UsuarioEstudante estudante = new UsuarioEstudante();
                    estudante.setRa(ra);
                    novoUsuario = estudante;
                }
                case "ADMIN" -> novoUsuario = new UsuarioAdministrador();
                default -> novoUsuario = new Usuario();
            }

            novoUsuario.setNome(nome);
            novoUsuario.setEmail(email);
            novoUsuario.setSenhaHash(AutenticacaoService.gerarHash(senha));
            novoUsuario.setTipo(Usuario.Tipo.valueOf(tipo));
            novoUsuario.setTelefone(telefone);
            novoUsuario.setCpf(cpf);

            usuarioService.cadastrar(novoUsuario);

            response.sendRedirect(request.getContextPath()
                    + "/admin/cadastrar-usuario?cadastro=sucesso");

        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.setAttribute("usuarioLogado", usuarioLogado);
            request.getRequestDispatcher("/cadastrar_usuario.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao cadastrar usuário. Tente novamente.");
            request.setAttribute("usuarioLogado", usuarioLogado);
            request.getRequestDispatcher("/cadastrar_usuario.jsp")
                    .forward(request, response);
        }
    }
}