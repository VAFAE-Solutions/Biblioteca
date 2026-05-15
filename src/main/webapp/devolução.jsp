%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Devolução de Livro</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<h1>Tela de Devolução</h1>

<form id="formDevolucao">
    <label>Nome do usuário:</label>
    <input type="text" id="usuarioDevolucao" required>

    <label>Título do livro:</label>
    <input type="text" id="livroDevolucao" required>

    <label>Data da devolução:</label>
    <input type="date" id="dataEntrega" required>

    <button type="submit">Registrar Devolução</button>
</form>

<script src="js/operacoes.js"></script>
</body>
</html>