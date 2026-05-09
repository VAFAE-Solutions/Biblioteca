<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Cadastro - Library</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: Arial, sans-serif; }
        body { background: #f4f4f4; }
        .container {
            width: 100%;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px;
        }
        .cadastro-card {
            width: 100%;
            max-width: 550px;
            background: #fff;
            padding: 40px;
            border-radius: 14px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .cadastro-card h2 {
            text-align: center;
            margin-bottom: 10px;
            font-size: 34px;
            color: #222;
        }
        .subtitulo {
            text-align: center;
            color: #666;
            margin-bottom: 30px;
        }
        .form-group { margin-bottom: 22px; }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }
        .form-group input {
            width: 100%;
            padding: 14px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 16px;
        }
        .form-group input:focus {
            outline: none;
            border: 1px solid #39c3cf;
        }
        .btn-cadastro {
            width: 100%;
            padding: 15px;
            border: none;
            background: #39c3cf;
            color: #fff;
            font-size: 18px;
            border-radius: 8px;
            cursor: pointer;
            transition: 0.3s;
        }
        .btn-cadastro:hover { background: #27aab5; }
        .mensagem {
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 20px;
            text-align: center;
        }
        .erro { background: #ffdede; color: #c40000; }
        .link-login {
            text-align: center;
            margin-top: 25px;
        }
        .link-login a {
            text-decoration: none;
            color: #39c3cf;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="cadastro-card">
        <h2>Criar Conta</h2>
        <p class="subtitulo">Passo 1 de 2 - Dados Básicos</p>

        <%-- Erro vindo do servidor --%>
        <c:if test="${not empty erro}">
            <div class="mensagem erro">${erro}</div>
        </c:if>

        <div id="mensagemErro" class="mensagem erro" style="display:none;"></div>

        <form id="formCadastro" action="${pageContext.request.contextPath}/cadastro" method="post">
            <div class="form-group">
                <label>Nome Completo</label>
                <input type="text" name="nome" id="nome"
                       placeholder="Digite seu nome completo" required>
            </div>
            <div class="form-group">
                <label>E-mail</label>
                <input type="email" name="email" id="email"
                       placeholder="seu@email.com" required>
            </div>
            <div class="form-group">
                <label>Senha</label>
                <input type="password" name="senha" id="senha"
                       placeholder="Mínimo 6 caracteres" required>
            </div>
            <div class="form-group">
                <label>Confirmar Senha</label>
                <input type="password" id="confirmarSenha"
                       placeholder="Digite novamente" required>
            </div>
            <button type="submit" class="btn-cadastro">Próximo →</button>
        </form>

        <div class="link-login">
            <p>Já possui conta?</p>
            <a href="${pageContext.request.contextPath}/login">← Fazer Login</a>
        </div>
    </div>
</div>

<script>
    document.getElementById('formCadastro').addEventListener('submit', function(event) {
        const nome = document.getElementById('nome').value;
        const senha = document.getElementById('senha').value;
        const confirmarSenha = document.getElementById('confirmarSenha').value;
        const erro = document.getElementById('mensagemErro');

        erro.style.display = 'none';

        if (nome.trim().length < 3) {
            event.preventDefault();
            erro.textContent = 'Nome deve ter ao menos 3 caracteres.';
            erro.style.display = 'block';
            return false;
        }
        if (senha.length < 6) {
            event.preventDefault();
            erro.textContent = 'Senha deve ter ao menos 6 caracteres.';
            erro.style.display = 'block';
            return false;
        }
        if (senha !== confirmarSenha) {
            event.preventDefault();
            erro.textContent = 'As senhas não coincidem.';
            erro.style.display = 'block';
            return false;
        }
    });
</script>
</body>
</html>