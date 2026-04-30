<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Painel Administrativo - Library Digital</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .sidebar-admin { background: #212529; color: white; min-height: 100vh; width: 250px; position: fixed; }
        .sidebar-admin a { color: #adb5bd; text-decoration: none; padding: 15px; display: block; }
        .sidebar-admin a:hover { background: #343a40; color: white; }
        .main-content { margin-left: 260px; padding: 20px; }
        .stats-card { border-left: 5px solid #40c4d4; }
    </style>
</head>
<body class="bg-light">

<div class="sidebar-admin">
    <div class="p-4 text-center"><h5>📚 Admin Panel</h5><hr></div>
    <a href="${pageContext.request.contextPath}/admin">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/dashboard">📖 Catálogo</a>
    <a href="#">👥 Usuários</a>
    <a href="#">📅 Empréstimos Ativos</a>
    <a href="${pageContext.request.contextPath}/logout" class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Bem-vindo, ${usuarioLogado.tipo}: ${usuarioLogado.nome}</h2>
        <a href="#" class="btn btn-primary">+ Cadastrar Novo Livro</a>
    </div>

    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card p-3 shadow-sm stats-card">
                <h6>Total de Livros</h6>
                <h3>${livros.size()}</h3>
            </div>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-header bg-white"><strong>Gerenciamento de Acervo</strong></div>
        <div class="card-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Capa</th>
                    <th>Título / Autor</th>
                    <th>Editora</th>
                    <th>Gênero</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="l" items="${livros}">
                    <tr>
                        <td>${l.id}</td>
                        <td>
                            <img src="${not empty l.capaUrl ? l.capaUrl : ''}"
                                 width="40"
                                 onerror="this.src='https://via.placeholder.com/40x60?text=?'">
                        </td>
                        <td>
                            <strong>${l.titulo}</strong>
                            <br><small>${l.autor}</small>
                        </td>
                        <td>${l.editora}</td>
                        <td>${l.genero}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/detalhes?id=${l.id}"
                               class="btn btn-sm btn-info text-white">Ver</a>
                            <button class="btn btn-sm btn-warning">Editar</button>
                            <button class="btn btn-sm btn-danger">Excluir</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>