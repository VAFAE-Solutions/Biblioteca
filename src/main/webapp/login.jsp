<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .input-senha-wrapper { position: relative; display: flex; align-items: center; }
        .input-senha-wrapper input { width: 100%; padding-right: 45px; }
        .toggle-senha { position: absolute; right: 12px; cursor: pointer; color: #aaa; font-size: 18px; user-select: none; }
        .toggle-senha:hover { color: #39c3cf; }
        .email-input-group { display: flex; align-items: center; border: 1px solid #ccc; border-radius: 8px; overflow: hidden; }
        .email-input-group input { border: none; flex: 1; padding: 12px; font-size: 14px; outline: none; }
        .email-input-group span { background: #f0f2f5; padding: 12px; color: #666; white-space: nowrap; font-size: 14px; border-left: 1px solid #ccc; }
    </style>
</head>
<body>
<div class="login-wrapper">
    <div class="login-container">
        <h2>Acesse sua Conta</h2>

        <c:if test="${param.cadastro == 'sucesso'}">
            <div class="mensagem sucesso">Cadastro realizado com sucesso! Faça login para continuar.</div>
        </c:if>

        <c:if test="${not empty erro}">
            <div class="erro">
                <c:choose>
                    <c:when test="${erro == 'usuario_bloqueado'}">
                        🔒 Sua conta foi bloqueada após 3 tentativas incorretas.
                        <br><br>
                        Para recuperar o acesso, solicite o reset de senha abaixo e,
                        em seguida, dirija-se presencialmente à nossa unidade para
                        efetuar o procedimento de desbloqueio junto ao bibliotecário.
                        <br><br>
                        <a href="${pageContext.request.contextPath}/esqueceu-senha"
                           style="color:#d32f2f; font-weight: bold;">🔑 Solicitar reset de senha →</a>
                    </c:when>
                    <c:when test="${erro == 'usuario_inativo'}">
                        Usuário desativado. Entre em contato com a biblioteca.
                    </c:when>
                    <c:when test="${erro == 'login_falhou'}">
                        Senha incorreta. Tente novamente.
                    </c:when>
                    <c:otherwise>
                        Usuário não encontrado.
                        <a href="${pageContext.request.contextPath}/cadastro" style="color:#d32f2f;">Cadastre-se aqui</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" onsubmit="montarEmail()">
            <div class="form-group">
                <label>E-mail</label>
                <div class="email-input-group">
                    <input type="text" id="emailPrefix" placeholder="seunome" required>
                    <span>@biblioteca.com</span>
                </div>
                <input type="hidden" name="email" id="emailCompleto">
            </div>
            <div class="form-group">
                <label>Senha</label>
                <div class="input-senha-wrapper">
                    <input type="password" name="senha" id="senhaLogin" placeholder="••••••••" required>
                    <span class="toggle-senha" onclick="toggleSenha('senhaLogin', this)">
                        <i class="bi bi-eye-slash"></i>
                    </span>
                </div>
            </div>
            <button type="submit" class="btn-entrar">Entrar no Sistema</button>
        </form>

        <div style="text-align: center; margin-top: 15px;">
            <a href="${pageContext.request.contextPath}/esqueceu-senha"
               style="color: #888; font-size: 14px; text-decoration: none;">Esqueceu a senha?</a>
        </div>

        <div style="text-align: center; margin-top: 20px; padding-top: 15px; border-top: 1px solid #eee;">
            <p style="color: #666; margin-bottom: 10px;">Não tem uma conta?</p>
            <a href="${pageContext.request.contextPath}/cadastro"
               style="display: inline-block; padding: 10px 20px; background: #f0f2f5; color: #39c3cf;
                      text-decoration: none; border-radius: 8px; font-weight: bold; border: 1px solid #39c3cf;">
                Criar nova conta
            </a>
        </div>
    </div>
</div>

<script>
    function montarEmail() {
        const prefix = document.getElementById('emailPrefix').value.trim();
        document.getElementById('emailCompleto').value = prefix + '@biblioteca.com';
    }
    function toggleSenha(inputId, btn) {
        const input = document.getElementById(inputId);
        const icon = btn.querySelector('i');
        if (input.type === 'password') { input.type = 'text'; icon.className = 'bi bi-eye'; }
        else { input.type = 'password'; icon.className = 'bi bi-eye-slash'; }
    }
</script>
</body>
</html>
