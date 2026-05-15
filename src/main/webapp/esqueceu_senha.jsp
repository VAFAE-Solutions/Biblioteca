<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Esqueceu a Senha - Library</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .login-wrapper { display: flex; justify-content: center; align-items: center; min-height: 100vh; background: #f4f4f4; }
        .login-container { background: white; padding: 40px; border-radius: 14px; box-shadow: 0 0 20px rgba(0,0,0,0.1); width: 100%; max-width: 450px; }
        h2 { text-align: center; margin-bottom: 10px; color: #222; }
        .subtitulo { text-align: center; color: #666; margin-bottom: 30px; font-size: 14px; }
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 8px; font-weight: bold; color: #333; }
        .form-group input { width: 100%; padding: 14px; border: 1px solid #ccc; border-radius: 8px; font-size: 16px; }
        .form-group input:focus { outline: none; border-color: #39c3cf; }
        .btn-enviar { width: 100%; padding: 15px; border: none; background: #39c3cf; color: white; font-size: 18px; border-radius: 8px; cursor: pointer; }
        .btn-enviar:hover { background: #27aab5; }
        .mensagem { padding: 12px; border-radius: 6px; margin-bottom: 20px; text-align: center; }
        .sucesso { background: #d4edda; color: #155724; }
        .erro { background: #ffdede; color: #c40000; }
        .link-voltar { text-align: center; margin-top: 20px; }
        .link-voltar a { color: #39c3cf; text-decoration: none; font-weight: bold; }
    </style>
</head>
<body>
<div class="login-wrapper">
    <div class="login-container">
        <h2>🔑 Esqueceu a Senha?</h2>
        <p class="subtitulo">
            Informe seu e-mail para solicitar o desbloqueio ao administrador.
        </p>

        <c:if test="${param.sucesso == 'true'}">
            <div class="mensagem sucesso">
                ✅ Solicitação enviada! O administrador irá analisar e fornecer uma senha temporária em breve.
            </div>
        </c:if>

        <c:if test="${not empty erro}">
            <div class="mensagem erro">❌ ${erro}</div>
        </c:if>

        <c:if test="${param.sucesso != 'true'}">
            <form action="${pageContext.request.contextPath}/esqueceu-senha" method="post">
                <div class="form-group">
                    <label>E-mail cadastrado</label>
                    <input type="email" name="email"
                           placeholder="seu@email.com" required>
                </div>
                <button type="submit" class="btn-enviar">
                    📨 Solicitar Desbloqueio
                </button>
            </form>
        </c:if>

        <div class="link-voltar">
            <a href="${pageContext.request.contextPath}/login">← Voltar ao Login</a>
        </div>
    </div>
</div>
</body>
</html>