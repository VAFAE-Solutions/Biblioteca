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
                        Usuário não encontrado.
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

    </div>
</div>

</body>
</html>