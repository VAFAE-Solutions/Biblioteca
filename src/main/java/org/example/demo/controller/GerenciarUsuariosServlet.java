package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.model.UsuarioEstudante;
import org.example.demo.service.MensagemContatoService;
import org.example.demo.service.MultaService;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/usuarios")
public class GerenciarUsuariosServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final MultaService multaService = new MultaService();
    private final MensagemContatoService mensagemService = new MensagemContatoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado.getTipo() != Usuario.Tipo.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String idParam = request.getParameter("detalhe");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Usuario usuarioDetalhe = usuarioService.buscarPorId(id);
                if (usuarioDetalhe != null) {
                    request.setAttribute("usuarioDetalhe", usuarioDetalhe);
                    request.setAttribute("multasUsuario", multaService.buscarPorUsuario(id));
                    request.setAttribute("totalMulta", multaService.calcularTotalMultasPendentes(id));
                }
            } catch (NumberFormatException ignored) {}
        }

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.setAttribute("usuarios", usuarioService.listarTodosIncluindoInativos());
        request.setAttribute("usuariosBloqueadosOuReset", usuarioService.listarBloqueadosOuComReset());

        request.getRequestDispatcher("/gerenciar_usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        int id = Integer.parseInt(request.getParameter("id"));

        try {
            switch (acao) {
                case "bloquear"       -> usuarioService.bloquear(id);
                case "desbloquear"    -> usuarioService.desbloquear(id);
                case "desativar"      -> usuarioService.desativar(id);
                case "reativar"       -> usuarioService.reativar(id);
                case "ajustar_limite" -> {
                    String limiteParam = request.getParameter("limite");
                    if (limiteParam != null && !limiteParam.isBlank()) {
                        usuarioService.ajustarLimiteCotas(id, Integer.parseInt(limiteParam));
                    }
                }
                case "resetar_limite" -> usuarioService.resetarLimiteCotas(id);
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

                        // ✅ Admin pode editar RA de estudante
                        if (usuario instanceof UsuarioEstudante estudante && raParam != null && !raParam.isBlank()) {
                            estudante.setRa(Integer.parseInt(raParam));
                        }

                        usuarioService.atualizar(usuario);
                    }
                    response.sendRedirect(request.getContextPath()
                            + "/admin/usuarios?acao=editado&detalhe=" + id);
                    return;
                }
                case "aprovar_reset" -> {
                    Usuario usuario = usuarioService.buscarPorId(id);
                    String senhaTemp = usuarioService.aprovarReset(id);

                    try {
                        mensagemService.enviar(
                                null,
                                "Sistema — Reset de Senha",
                                "sistema@biblioteca.com",
                                "Senha Temporaria — " + usuario.getNome(),
                                "O usuario " + usuario.getNome() + " (" + usuario.getEmail() + ") "
                                        + "solicitou reset de senha.\n\n"
                                        + "Senha temporaria: " + senhaTemp + "\n\n"
                                        + "Entregue esta senha ao usuario quando ele comparecer presencialmente."
                        );
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    response.sendRedirect(request.getContextPath()
                            + "/admin/usuarios?acao=reset_aprovado&senha=" + senhaTemp + "&id=" + id);
                    return;
                }
            }
            response.sendRedirect(request.getContextPath() + "/admin/usuarios?acao=" + acao);
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/usuarios?acao=erro&msg=" + e.getMessage());
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/usuarios?acao=erro");
        }
    }
}
