package org.example.demo.controller;

import org.example.demo.model.Livro;
import org.example.demo.model.Usuario;
import org.example.demo.service.LivroService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;

@WebServlet("/admin/cadastrar-livro")
public class CadastrarLivroServlet extends HttpServlet {

    private final LivroService livroService = new LivroService();

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
        request.getRequestDispatcher("/cadastrar_livro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        try {
            Livro livro = new Livro();
            livro.setTitulo(request.getParameter("titulo"));
            livro.setAutor(request.getParameter("autor"));
            livro.setEditora(request.getParameter("editora"));
            livro.setGenero(request.getParameter("genero"));
            livro.setDescricao(request.getParameter("descricao"));
            livro.setSumario(request.getParameter("sumario"));

            String capaUrl = request.getParameter("capaUrl");
            livro.setCapaUrl(capaUrl);

            // ✅ Baixa e salva imagem automaticamente
            byte[] imagem = baixarImagem(capaUrl);
            livro.setCapaImagem(imagem);

            String anoParam = request.getParameter("anoPublicacao");
            if (anoParam != null && !anoParam.isBlank()) {
                livro.setAnoPublicacao(Integer.parseInt(anoParam));
            }

            livroService.cadastrar(livro);
            response.sendRedirect(request.getContextPath() + "/admin?cadastro=sucesso");

        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.setAttribute("usuarioLogado", usuarioLogado);
            request.getRequestDispatcher("/cadastrar_livro.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao cadastrar livro. Tente novamente.");
            request.setAttribute("usuarioLogado", usuarioLogado);
            request.getRequestDispatcher("/cadastrar_livro.jsp").forward(request, response);
        }
    }

    // ✅ Baixa imagem da URL e retorna os bytes
    private byte[] baixarImagem(String capaUrl) {
        if (capaUrl == null || capaUrl.isBlank()) return null;

        try {
            URI uri = new URI(capaUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.connect();

            if (conn.getResponseCode() == 200) {
                String contentType = conn.getContentType();
                if (contentType != null && contentType.startsWith("image/")) {
                    try (InputStream is = conn.getInputStream()) {
                        return is.readAllBytes();
                    }
                }
            }
        } catch (Exception e) {
            // Link inválido ou inacessível — ignora
        }
        return null;
    }
}