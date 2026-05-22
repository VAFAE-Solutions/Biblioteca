package org.example.demo.controller;

import org.example.demo.model.Usuario;
import org.example.demo.service.EmprestimoService;
import org.example.demo.service.MultaService;
import org.example.demo.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;

@WebServlet("/admin/emprestimos")
public class EmprestimosAtivosServlet extends HttpServlet {

    private final EmprestimoService emprestimoService = new EmprestimoService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final MultaService multaService = new MultaService();

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

        var emprestimosAtivos    = emprestimoService.buscarAtivos();
        var emprestimosAtrasados = emprestimoService.buscarAtrasados();

        // ✅ Mapa de id da multa por emprestimoId
        var multasMap      = new HashMap<Integer, Integer>();
        // ✅ Mapa de valor da multa por emprestimoId
        var multasValorMap = new HashMap<Integer, Double>();

        emprestimosAtrasados.forEach(e -> {
            var multas = multaService.buscarPendentes(e.getUsuarioId());
            multas.stream()
                    .filter(m -> m.getEmprestimoId() == e.getId())
                    .findFirst()
                    .ifPresent(m -> {
                        multasMap.put(e.getId(), m.getId());
                        multasValorMap.put(e.getId(), m.getValor().doubleValue());
                    });
        });

        request.setAttribute("usuarioLogado", usuarioLogado);
        request.setAttribute("emprestimosAtivos", emprestimosAtivos);
        request.setAttribute("emprestimosAtrasados", emprestimosAtrasados);
        request.setAttribute("multasMap", multasMap);
        request.setAttribute("multasValorMap", multasValorMap);

        request.getRequestDispatcher("/emprestimos_ativos.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {
            if ("devolver".equals(acao)) {
                int emprestimoId = Integer.parseInt(request.getParameter("emprestimoId"));
                emprestimoService.finalizarEmprestimo(emprestimoId);
                response.sendRedirect(request.getContextPath()
                        + "/admin/emprestimos?devolucao=sucesso");

            } else if ("pagar_multa".equals(acao)) {
                int multaId = Integer.parseInt(request.getParameter("multaId"));
                multaService.registrarPagamento(multaId);
                response.sendRedirect(request.getContextPath()
                        + "/admin/emprestimos?pagamento=sucesso");

            } else {
                response.sendRedirect(request.getContextPath() + "/admin/emprestimos");
            }

        }  catch (Exception e) {
        response.sendRedirect(request.getContextPath()
                + "/admin/emprestimos?erro=falha");
        }
    }
}