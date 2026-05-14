package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.model.UsuarioEstudante;
import org.example.demo.service.AutenticacaoService;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet({"/cadastro", "/cadastro-completo"})
public class CadastroServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.endsWith("cadastro-completo")) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("cadastroNome") == null) {
                response.sendRedirect(request.getContextPath() + "/cadastro");
                return;
            }
            request.getRequestDispatcher("/cadastro_completo.jsp")
                    .forward(request, response);
        } else {
            request.getRequestDispatcher("/cadastro.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.endsWith("cadastro-completo")) {
            processarPasso2(request, response);
        } else {
            processarPasso1(request, response);
        }
    }

    private void processarPasso1(HttpServletRequest request,
                                 HttpServletResponse response)
            throws ServletException, IOException {

        String nome  = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (nome == null || nome.trim().length() < 3) {
            request.setAttribute("erro", "Nome deve ter ao menos 3 caracteres.");
            request.getRequestDispatcher("/cadastro.jsp")
                    .forward(request, response);
            return;
        }
        if (email == null || email.isBlank()) {
            request.setAttribute("erro", "E-mail é obrigatório.");
            request.getRequestDispatcher("/cadastro.jsp")
                    .forward(request, response);
            return;
        }
        if (senha == null || senha.length() < 6) {
            request.setAttribute("erro", "Senha deve ter ao menos 6 caracteres.");
            request.getRequestDispatcher("/cadastro.jsp")
                    .forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("cadastroNome", nome.trim());
        session.setAttribute("cadastroEmail", email.trim());
        session.setAttribute("cadastroSenha", senha);

        response.sendRedirect(request.getContextPath() + "/cadastro-completo");
    }

    private void processarPasso2(HttpServletRequest request,
                                 HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        String nome  = (String) session.getAttribute("cadastroNome");
        String email = (String) session.getAttribute("cadastroEmail");
        String senha = (String) session.getAttribute("cadastroSenha");

        String tipoStr  = request.getParameter("tipo");
        String telefone = request.getParameter("telefone");
        String cpf      = request.getParameter("cpf");
        String raParam  = request.getParameter("ra");

        try {
            Usuario usuario;
            Usuario.Tipo tipo = Usuario.Tipo.valueOf(tipoStr);

            if (tipo == Usuario.Tipo.ESTUDANTE) {
                UsuarioEstudante estudante = new UsuarioEstudante();
                estudante.setRa(Integer.parseInt(raParam));
                estudante.setCpf(cpf); // ✅ CPF agora é setado para estudante também
                usuario = estudante;
            } else {
                usuario = new Usuario();
                usuario.setCpf(cpf);
            }

            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenhaHash(AutenticacaoService.gerarHash(senha));
            usuario.setTipo(tipo);
            usuario.setTelefone(telefone);

            usuarioService.cadastrar(usuario);

            session.removeAttribute("cadastroNome");
            session.removeAttribute("cadastroEmail");
            session.removeAttribute("cadastroSenha");

            response.sendRedirect(request.getContextPath()
                    + "/login?cadastro=sucesso");

        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/cadastro_completo.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao cadastrar. Tente novamente.");
            request.getRequestDispatcher("/cadastro_completo.jsp")
                    .forward(request, response);
        }
    }
}
