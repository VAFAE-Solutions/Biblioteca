<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Complete seu Cadastro - Biblioteca</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="cadastro-wrapper">
    <div class="cadastro-container">
        <h2>Complete seu cadastro</h2>
        <p style="text-align: center; color: #666; margin-bottom: 25px;">Passo 2 de 2 - Dados adicionais</p>

        <div id="mensagemErro" class="mensagem erro" style="display: none;"></div>

        <form id="formCadastroPasso2" action="${pageContext.request.contextPath}/cadastro-completo" method="post">

            <div class="form-group">
                <label>RA (Registro Acadêmico) ou CPF</label>
                <input type="text"
                       name="raCpf"
                       id="raCpf"
                       placeholder="Ex: 12345678 ou 123.456.789-00"
                       required>
                <small style="color: #666; font-size: 12px;">Digite seu RA se for aluno, ou CPF se for comunidade externa</small>
            </div>

            <div class="form-group">
                <label>Endereço completo</label>
                <input type="text"
                       name="endereco"
                       id="endereco"
                       placeholder="Rua, número, bairro, cidade - CEP"
                       required>
            </div>

            <div class="form-group">
                <label>Telefone</label>
                <input type="tel"
                       name="telefone"
                       id="telefone"
                       placeholder="(11) 99999-9999"
                       required>
            </div>

            <!-- Campos ocultos para manter dados do passo 1 -->
            <input type="hidden" name="nome" id="nomeHidden">
            <input type="hidden" name="email" id="emailHidden">
            <input type="hidden" name="senha" id="senhaHidden">

            <button type="submit" class="btn-entrar">
                Finalizar Cadastro
            </button>
        </form>

        <div class="link-cadastro">
            <p>Já tem conta?</p>
            <a href="${pageContext.request.contextPath}/login.jsp">← Fazer login</a>
        </div>
    </div>
</div>

<script>
    // Recuperar os dados do passo 1 que vieram via URL
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);

        document.getElementById('nomeHidden').value = urlParams.get('nome') || '';
        document.getElementById('emailHidden').value = urlParams.get('email') || '';
        document.getElementById('senhaHidden').value = urlParams.get('senha') || '';

        // Se não tiver dados, redirecionar de volta para o passo 1
        if (!document.getElementById('nomeHidden').value) {
            window.location.href = '${pageContext.request.contextPath}/cadastro.jsp';
        }
    };

    // Validação do formulário
    document.getElementById('formCadastroPasso2').addEventListener('submit', function(event) {
        const raCpf = document.getElementById('raCpf').value;
        const endereco = document.getElementById('endereco').value;
        const telefone = document.getElementById('telefone').value;

        document.getElementById('mensagemErro').style.display = 'none';

        // Validar RA/CPF
        let numeros = raCpf.replace(/[^0-9]/g, '');
        if (numeros.length < 8) {
            event.preventDefault();
            document.getElementById('mensagemErro').textContent = 'RA ou CPF inválido. Digite pelo menos 8 números.';
            document.getElementById('mensagemErro').style.display = 'block';
            return false;
        }

        // Validar telefone
        let telefoneNumeros = telefone.replace(/[^0-9]/g, '');
        if (telefoneNumeros.length < 10 || telefoneNumeros.length > 11) {
            event.preventDefault();
            document.getElementById('mensagemErro').textContent = 'Telefone inválido. Use (DDD) + 8 ou 9 dígitos.';
            document.getElementById('mensagemErro').style.display = 'block';
            return false;
        }

        // Validar endereço
        if (endereco.trim().length < 10) {
            event.preventDefault();
            document.getElementById('mensagemErro').textContent = 'Endereço muito curto. Digite um endereço completo.';
            document.getElementById('mensagemErro').style.display = 'block';
            return false;
        }
    });

    // Máscara para telefone
    document.getElementById('telefone').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            if (value.length <= 2) {
                value = value.replace(/^(\d{0,2})/, '($1');
            } else if (value.length <= 7) {
                value = value.replace(/^(\d{2})(\d{0,5})/, '($1) $2');
            } else {
                value = value.replace(/^(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3');
            }
            e.target.value = value;
        }
    });

    // Máscara para CPF (se digitar 11 números)
    document.getElementById('raCpf').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length === 11) {
            value = value.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
            e.target.value = value;
        }
    });
</script>

</body>
</html>