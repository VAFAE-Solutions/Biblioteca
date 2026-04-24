package org.example.demo.controller;

import org.example.demo.dao.UsuarioDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        // O DAO deve retornar [status, cpf, tipo, id]
        // Verifique se o seu UsuarioDAO está preenchendo a posição 3 com p_usuario_id
        String[] resultado = usuarioDAO.validarLogin(email, senha);
        String status = resultado[0];
        String cpf = resultado[1];
        String tipo = resultado[2];
        String idUsuario = resultado[3]; // <--- CAPTURAMOS O ID NUMÉRICO AQUI

        if ("login_sucesso".equals(status)) {
            HttpSession session = request.getSession();

            /* Criamos o Map para simular o objeto Usuário na Sessão */
            Map<String, String> usuarioSimulado = new HashMap<>();
            usuarioSimulado.put("id", idUsuario); // <--- SALVAMOS O ID NO MAP (Essencial para a Sprint 3)
            usuarioSimulado.put("email", email);
            usuarioSimulado.put("cpf", cpf);
            usuarioSimulado.put("tipo", tipo);

            session.setAttribute("usuarioLogado", usuarioSimulado);
            session.setAttribute("usuarioCpf", cpf);
            session.setAttribute("usuarioTipo", tipo);
            session.setAttribute("usuarioId", idUsuario);

            System.out.println(">>> LOGIN SUCESSO: " + email + " (ID: " + idUsuario + ") Perfil: " + tipo);

            // Redirecionamento por Perfil
            if ("ADMIN".equals(tipo) || "BIBLIOTECARIO".equals(tipo)) {
                response.sendRedirect("admin");
            } else {
                response.sendRedirect("dashboard");
            }

        } else {
            System.out.println(">>> LOGIN FALHA: " + email + " Status: " + status);
            response.sendRedirect("login.jsp?erro=" + status);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("login.jsp");
    }
}