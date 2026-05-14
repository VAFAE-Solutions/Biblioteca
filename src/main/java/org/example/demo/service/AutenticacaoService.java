package org.example.demo.service;

import org.example.demo.dao.UsuarioDAO;
import org.example.demo.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AutenticacaoService {

    public enum ResultadoAutenticacao {
        SUCESSO,
        USUARIO_NAO_ENCONTRADO,
        SENHA_INCORRETA,
        USUARIO_BLOQUEADO,
        USUARIO_INATIVO // ✅ novo status
    }

    private final UsuarioDAO usuarioDAO;
    private ResultadoAutenticacao ultimoResultado;

    public AutenticacaoService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticar(String email, String senha) {

        if (email == null || email.isBlank() ||
                senha == null || senha.isBlank()) {
            ultimoResultado = ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO;
            return null;
        }

        String senhaHash = gerarHash(senha);
        String[] resultado = usuarioDAO.executarLoginProcedure(email, senhaHash);
        String status = resultado[0];

        switch (status) {
            case "usuario_nao_encontrado" -> {
                ultimoResultado = ResultadoAutenticacao.USUARIO_NAO_ENCONTRADO;
                return null;
            }
            case "usuario_inativo" -> { // ✅ novo caso
                ultimoResultado = ResultadoAutenticacao.USUARIO_INATIVO;
                return null;
            }
            case "usuario_bloqueado",
                 "usuario_bloqueado_apos_3_tentativas" -> {
                ultimoResultado = ResultadoAutenticacao.USUARIO_BLOQUEADO;
                return null;
            }
            case "login_falhou" -> {
                ultimoResultado = ResultadoAutenticacao.SENHA_INCORRETA;
                return null;
            }
        }

        int usuarioId = Integer.parseInt(resultado[1]);
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        ultimoResultado = ResultadoAutenticacao.SUCESSO;
        return usuario;
    }

    public static String gerarHash(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash da senha.", e);
        }
    }

    public ResultadoAutenticacao getUltimoResultado() {
        return ultimoResultado;
    }
}
