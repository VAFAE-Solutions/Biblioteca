<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Empréstimo de Livro</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<h1>Tela de Empréstimo</h1>

<form id="formEmprestimo">
    <label>Nome do usuário:</label>
    <input type="text" id="usuario" required>

    <label>Título do livro:</label>
    <input type="text" id="livro" required>

    <label>Data do empréstimo:</label>
    <input type="date" id="dataEmprestimo" required>

    <label>Data prevista para devolução:</label>
    <input type="date" id="dataDevolucao" required>

    <button type="submit">Registrar Empréstimo</button>
</form>

<script src="js/operacoes.js"></script>
</body>
</html>