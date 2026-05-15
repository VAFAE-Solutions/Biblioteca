<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Consulta de Multas</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<h1>Consulta de Multas</h1>

<form id="formMulta">
    <label>Nome do usuário:</label>
    <input type="text" id="usuarioMulta" required>

    <label>Dias de atraso:</label>
    <input type="number" id="diasAtraso" required>

    <button type="submit">Calcular Multa</button>
</form>

<p id="resultadoMulta"></p>

<script src="js/operacoes.js"></script>
</body>
</html>