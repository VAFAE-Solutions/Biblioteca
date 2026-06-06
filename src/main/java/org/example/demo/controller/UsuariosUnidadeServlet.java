package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.model.UsuarioBibliotecario;
import org.example.demo.model.UsuarioEstudante;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/admin/usuarios-unidade")
public class UsuariosUnidadeServlet extends HttpServlet {

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

        int unidadeId = 0;
        if (usuarioLogado instanceof UsuarioBibliotecario bib) {
            unidadeId = bib.getUnidadeId();
        }

        final int unidadeFinal = unidadeId;
        List<Usuario> todosUsuarios = usuarioService.listarTodos();

        List<Usuario> usuariosDaUnidade = unidadeFinal > 0
                ? todosUsuarios.stream()
                .filter(u -> u.getTipo() == Usuario.Tipo.ESTUDANTE
                        || u.getTipo() == Usuario.Tipo.COMUM)
                .collect(Collectors.toList())
                : todosUsuarios;

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.setAttribute("usuarios", usuariosDaUnidade);
        request.setAttribute("unidadeId", unidadeId);

        request.getRequestDispatcher("/usuarios_unidade.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        int id = Integer.parseInt(request.getParameter("id"));

        try {
            switch (acao) {
                case "bloquear" -> {
                    usuarioService.bloquear(id);
                    response.sendRedirect(request.getContextPath()
                            + "/admin/usuarios-unidade?acao=bloqueado");
                }
                case "desbloquear" -> {
                    usuarioService.desbloquear(id);
                    response.sendRedirect(request.getContextPath()
                            + "/admin/usuarios-unidade?acao=desbloqueado");
                }
                case "desativar" -> {
                    usuarioService.desativar(id);
                    response.sendRedirect(request.getContextPath()
                            + "/admin/usuarios-unidade?acao=desativado");
                }
                case "reativar" -> {
                    usuarioService.reativar(id);
                    response.sendRedirect(request.getContextPath()
                            + "/admin/usuarios-unidade?acao=reativado");
                }
                case "editar" -> {
                    // ✅ Editar dados do usuário
                    Usuario usuario = usuarioService.buscarPorId(id);
                    if (usuario != null) {
                        String nome     = request.getParameter("nome");
                        String telefone = request.getParameter("telefone");
                        String cpf      = request.getParameter("cpf");
                        String raParam  = request.getParameter("ra");

                        if (nome != null && !nome.isBlank()) usuario.setNome(nome.trim());
                        usuario.setTelefone(telefone);
                        usuario.setCpf(cpf);

                        if (usuario instanceof UsuarioEstudante estudante
                                && raParam != null && !raParam.isBlank()) {
                            estudante.setRa(Integer.parseInt(raParam));
                        }

                        usuarioService.atualizar(usuario);
                    }
                    // ✅ Reabre o modal do usuário editado
                    response.sendRedirect(request.getContextPath()
                            + "/admin/usuarios-unidade?acao=editado&usuarioId=" + id);
                }
                default -> response.sendRedirect(request.getContextPath()
                        + "/admin/usuarios-unidade");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/usuarios-unidade?acao=erro");
        }
    }
}
