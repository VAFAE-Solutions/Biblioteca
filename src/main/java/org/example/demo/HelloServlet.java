package org.example.demo;

import java.io.*;
import java.sql.Connection; // Adicionado para reconhecer a conexão
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Status do Banco de Dados:";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Tenta abrir a conexão usando a sua classe Database
        Connection conn = Database.getConnection();

        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");

        if (conn != null) {
            out.println("<h2 style='color: green'>Conectado ao MySQL com sucesso! 🚀</h2>");
            try {
                conn.close(); // Sempre feche a conexão após usar
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            out.println("<h2 style='color: red'>Falha na conexão. Verifique o console do IntelliJ. ❌</h2>");
        }

        out.println("</body></html>");
    }

    public void destroy() {
    }
}