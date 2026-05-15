<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Complete seu Cadastro - Library</title>
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
        .form-group input, .form-group select {
            width: 100%;
            padding: 14px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 16px;
        }
        .form-group input:focus, .form-group select:focus {
            outline: none;
            border: 1px solid #39c3cf;
        }
        .form-group small {
            color: #666;
            font-size: 12px;
            margin-top: 5px;
            display: block;
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
        .campo-ra  { display: none; }
        .campo-cpf { display: none; }
    </style>
</head>
<body>
<div class="container">
    <div class="cadastro-card">
        <h2>Complete seu Cadastro</h2>
        <p class="subtitulo">Passo 2 de 2 - Dados Adicionais</p>

        <c:if test="${not empty erro}">
            <div class="mensagem erro">${erro}</div>
        </c:if>

        <div id="mensagemErro" class="mensagem erro" style="display:none;"></div>

        <form id="formCadastroPasso2"
              action="${pageContext.request.contextPath}/cadastro-completo"
              method="post">

            <input type="hidden" name="nome" value="${sessionScope.cadastroNome}">
            <input type="hidden" name="email" value="${sessionScope.cadastroEmail}">

            <div class="form-group">
                <label>Tipo de Usuário</label>
                <select name="tipo" id="tipo" onchange="mostrarCampos()" required>
                    <option value="">Selecione...</option>
                    <option value="ESTUDANTE">Estudante</option>
                    <option value="COMUM">Usuário Comum</option>
                </select>
            </div>

            <%-- Campo RA — só para estudante --%>
            <div class="form-group campo-ra" id="campoRa">
                <label>RA (Registro Acadêmico)</label>
                <input type="number" name="ra" id="ra"
                       placeholder="Ex: 12345678">
                <small>Obrigatório para estudantes</small>
            </div>

            <%-- ✅ Campo CPF — para estudante E usuário comum --%>
            <div class="form-group campo-cpf" id="campoCpf">
                <label>CPF</label>
                <input type="text" name="cpf" id="cpf"
                       placeholder="000.000.000-00">
                <small>Obrigatório</small>
            </div>

            <div class="form-group">
                <label>Telefone</label>
                <input type="tel" name="telefone" id="telefone"
                       placeholder="(11) 99999-9999">
            </div>

            <button type="submit" class="btn-cadastro">Finalizar Cadastro</button>
        </form>

        <div class="link-login">
            <p>Já tem conta?</p>
            <a href="${pageContext.request.contextPath}/login">← Fazer Login</a>
        </div>
    </div>
</div>

<script>
    function mostrarCampos() {
        const tipo = document.getElementById('tipo').value;
        const campoRa  = document.getElementById('campoRa');
        const campoCpf = document.getElementById('campoCpf');
        const ra  = document.getElementById('ra');
        const cpf = document.getElementById('cpf');

        campoRa.style.display  = 'none';
        campoCpf.style.display = 'none';
        ra.required  = false;
        cpf.required = false;

        if (tipo === 'ESTUDANTE') {
            campoRa.style.display  = 'block';
            campoCpf.style.display = 'block'; // ✅ CPF também para estudante
            ra.required  = true;
            cpf.required = true;
        } else if (tipo === 'COMUM') {
            campoCpf.style.display = 'block';
            cpf.required = true;
        }
    }

    document.getElementById('formCadastroPasso2')
        .addEventListener('submit', function(event) {

        const tipo     = document.getElementById('tipo').value;
        const telefone = document.getElementById('telefone').value;
        const erro     = document.getElementById('mensagemErro');

        erro.style.display = 'none';

        if (!tipo) {
            event.preventDefault();
            erro.textContent = 'Selecione o tipo de usuário.';
            erro.style.display = 'block';
            return false;
        }

        if (tipo === 'ESTUDANTE') {
            const ra = document.getElementById('ra').value;
            if (!ra || ra.length < 5) {
                event.preventDefault();
                erro.textContent = 'RA inválido. Digite pelo menos 5 números.';
                erro.style.display = 'block';
                return false;
            }
        }

        // ✅ Valida CPF para ESTUDANTE e COMUM
        if (tipo === 'ESTUDANTE' || tipo === 'COMUM') {
            const cpf = document.getElementById('cpf').value.replace(/\D/g, '');
            if (cpf.length !== 11) {
                event.preventDefault();
                erro.textContent = 'CPF inválido. Digite 11 números.';
                erro.style.display = 'block';
                return false;
            }
        }

        if (telefone) {
            const tel = telefone.replace(/\D/g, '');
            if (tel.length < 10 || tel.length > 11) {
                event.preventDefault();
                erro.textContent = 'Telefone inválido. Use (DDD) + 8 ou 9 dígitos.';
                erro.style.display = 'block';
                return false;
            }
        }
    });

    // Máscara telefone
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

    // Máscara CPF
    document.getElementById('cpf').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            value = value.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
            e.target.value = value;
        }
    });
</script>
</body>
</html>