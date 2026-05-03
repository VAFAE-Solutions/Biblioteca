<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="login-wrapper">
    <div class="login-container">

        <h2>Acesse sua Conta</h2>

        <%-- Mensagens de erro vindas do LoginServlet --%>
        <c:if test="${not empty erro}">
            <div class="erro">
                <c:choose>
                    <c:when test="${erro == 'usuario_bloqueado'}">
                        Usuário bloqueado. Entre em contato com a biblioteca.
                    </c:when>
                    <c:when test="${erro == 'login_falhou'}">
                        Senha incorreta. Tente novamente.
                    </c:when>
                    <c:otherwise>
                        Usuário não encontrado. <a href="${pageContext.request.contextPath}/cadastro.html" style="color: #d32f2f;">Cadastre-se aqui</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">

            <div class="form-group">
                <label>E-mail</label>
                <input type="email" name="email" placeholder="seu@email.com" required>
            </div>

            <div class="form-group">
                <label>Senha</label>
                <input type="password" name="senha" placeholder="********" required>
            </div>

            <button type="submit" class="btn-entrar">
                Entrar no Sistema
            </button>

        </form>

        <!-- NOVO: Link para cadastro -->
        <div style="text-align: center; margin-top: 20px; padding-top: 15px; border-top: 1px solid #eee;">
            <p style="color: #666; margin-bottom: 10px;">Não tem uma conta?</p>
            <a href="${pageContext.request.contextPath}/cadastro.html"
               style="display: inline-block;
                      padding: 10px 20px;
                      background: #f0f2f5;
                      color: #2d73d5;
                      text-decoration: none;
                      border-radius: 8px;
                      font-weight: bold;
                      border: 1px solid #2d73d5;">
                Criar nova conta
            </a>
        </div>

    </div>
</div>

</body>
</html>