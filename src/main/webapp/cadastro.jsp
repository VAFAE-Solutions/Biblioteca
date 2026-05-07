<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Cadastro - Biblioteca</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="cadastro-wrapper">
    <div class="cadastro-container">
        <h2>Criar nova conta</h2>
        <p style="text-align: center; color: #666; margin-bottom: 25px;">Passo 1 de 2 - Dados básicos</p>

        <div id="mensagemErro" class="mensagem erro" style="display: none;"></div>

        <form id="formCadastro" action="${pageContext.request.contextPath}/cadastro-2.jsp" method="get">
            <div class="form-group">
                <label>Nome completo</label>
                <input type="text" name="nome" id="nome" placeholder="Digite seu nome completo" required>
            </div>

            <div class="form-group">
                <label>E-mail</label>
                <input type="email" name="email" id="email" placeholder="seu@email.com" required>
            </div>

            <div class="form-group">
                <label>Senha</label>
                <input type="password" name="senha" id="senha" placeholder="Mínimo 6 caracteres" required>
            </div>

            <div class="form-group">
                <label>Confirmar senha</label>
                <input type="password" name="confirmarSenha" id="confirmarSenha" placeholder="Digite a senha novamente" required>
            </div>

            <button type="submit" class="btn-entrar">
                Próximo →
            </button>
        </form>

        <div class="link-cadastro">
            <p>Já tem uma conta?</p>
            <a href="${pageContext.request.contextPath}/login.jsp">← Fazer login</a>
        </div>
    </div>
</div>

<script>
    document.getElementById('formCadastro').addEventListener('submit', function(event) {
        const senha = document.getElementById('senha').value;
        const confirmarSenha = document.getElementById('confirmarSenha').value;
        const nome = document.getElementById('nome').value;

        document.getElementById('mensagemErro').style.display = 'none';

        if (nome.trim().length < 3) {
            event.preventDefault();
            document.getElementById('mensagemErro').textContent = 'Nome deve ter pelo menos 3 caracteres';
            document.getElementById('mensagemErro').style.display = 'block';
            return false;
        }

        if (senha.length < 6) {
            event.preventDefault();
            document.getElementById('mensagemErro').textContent = 'Senha deve ter pelo menos 6 caracteres';
            document.getElementById('mensagemErro').style.display = 'block';
            return false;
        }

        if (senha !== confirmarSenha) {
            event.preventDefault();
            document.getElementById('mensagemErro').textContent = 'As senhas não coincidem';
            document.getElementById('mensagemErro').style.display = 'block';
            return false;
        }
    });
</script>

</body>
</html>