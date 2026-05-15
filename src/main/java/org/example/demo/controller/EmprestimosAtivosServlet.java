package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.EmprestimoService;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/emprestimos")
public class EmprestimosAtivosServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();
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

        emprestimoService.verificarEAtualizarAtrasos();

        // ✅ Monta mapa de usuários para exibir nome em vez de ID
        var emprestimosAtivos   = emprestimoService.buscarAtivos();
        var emprestimosAtrasados = emprestimoService.buscarAtrasados();

        // Busca nomes dos usuários
        var usuariosMap = new java.util.HashMap<Integer, String>();
        emprestimosAtivos.forEach(e -> {
            if (!usuariosMap.containsKey(e.getUsuarioId())) {
                Usuario u = usuarioService.buscarPorId(e.getUsuarioId());
                usuariosMap.put(e.getUsuarioId(), u != null ? u.getNome() : "ID: " + e.getUsuarioId());
            }
        });
        emprestimosAtrasados.forEach(e -> {
            if (!usuariosMap.containsKey(e.getUsuarioId())) {
                Usuario u = usuarioService.buscarPorId(e.getUsuarioId());
                usuariosMap.put(e.getUsuarioId(), u != null ? u.getNome() : "ID: " + e.getUsuarioId());
            }
        });

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.setAttribute("emprestimosAtivos", emprestimosAtivos);
        request.setAttribute("emprestimosAtrasados", emprestimosAtrasados);
        request.setAttribute("usuariosMap", usuariosMap);

        request.getRequestDispatcher("/emprestimos_ativos.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int emprestimoId = Integer.parseInt(request.getParameter("emprestimoId"));
            emprestimoService.finalizarEmprestimo(emprestimoId);
            response.sendRedirect(request.getContextPath()
                    + "/admin/emprestimos?devolucao=sucesso");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/emprestimos?devolucao=erro");
        }
    }
}