package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.model.UsuarioEstudante;
import org.example.demo.service.EmprestimoService;
import org.example.demo.service.MultaService;
import org.example.demo.service.ReservaService;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final MultaService multaService = new MultaService();
    private final ReservaService reservaService = new ReservaService();
    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        try {
            int id = usuarioLogado.getId();

            long totalEmprestimos = emprestimoService
                    .buscarPorUsuario(id)
                    .stream()
                    .filter(e -> e.getStatus().name().equals("ATIVO")
                            || e.getStatus().name().equals("ATRASADO"))
                    .count();

            double totalMulta = multaService.calcularTotalMultasPendentes(id);

            long totalReservas = reservaService
                    .buscarPorUsuario(id)
                    .stream()
                    .filter(r -> r.getStatus().name().equals("AGUARDANDO"))
                    .count();

            request.setAttribute("totalEmprestimos", totalEmprestimos);
            request.setAttribute("totalMulta", totalMulta);
            request.setAttribute("totalReservas", totalReservas);

            if (usuarioLogado instanceof UsuarioEstudante estudante) {
                request.setAttribute("ra", estudante.getRa());
            }

            request.getRequestDispatcher("/perfil_usuario.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/dashboard?erro=perfil_falhou");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        String acao = request.getParameter("acao");

        try {
            if ("trocar_senha".equals(acao)) {
                // ✅ Trocar senha
                String senhaAtual = request.getParameter("senhaAtual");
                String novaSenha  = request.getParameter("novaSenha");
                String confirmar  = request.getParameter("confirmarSenha");

                if (!novaSenha.equals(confirmar)) {
                    throw new IllegalArgumentException("As senhas não coincidem.");
                }

                usuarioService.atualizarSenha(usuarioLogado.getId(), senhaAtual, novaSenha);
                response.sendRedirect(request.getContextPath()
                        + "/perfil?senha=sucesso");

            } else {
                // ✅ Atualizar dados do perfil
                String nome     = request.getParameter("nome");
                String telefone = request.getParameter("telefone");
                String cpf      = request.getParameter("cpf");

                if (nome == null || nome.trim().length() < 3) {
                    throw new IllegalArgumentException("Nome deve ter ao menos 3 caracteres.");
                }

                usuarioLogado.setNome(nome.trim());
                usuarioLogado.setTelefone(telefone);
                usuarioLogado.setCpf(cpf);

                usuarioService.atualizar(usuarioLogado);
                session.setAttribute("usuarioLogado", usuarioLogado);

                response.sendRedirect(request.getContextPath()
                        + "/perfil?atualizado=sucesso");
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao atualizar. Tente novamente.");
            doGet(request, response);
        }
    }
}