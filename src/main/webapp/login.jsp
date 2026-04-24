<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Login</title>

    <!-- IMPORTANTE: caminho simples -->
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="login-wrapper">
    <div class="login-container">

        <h2>Acesse sua Conta</h2>

        <form action="login" method="post">

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