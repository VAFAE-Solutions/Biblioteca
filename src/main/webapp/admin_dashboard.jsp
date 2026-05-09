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
        .sidebar-admin a.text-danger:hover { background: #343a40; }
        .main-content { margin-left: 260px; padding: 20px; }
        .stats-card { border-left: 5px solid #40c4d4; }
    </style>
</head>
<body class="bg-light">

<div class="sidebar-admin">
    <div class="p-4 text-center">
        <h5>📚
            <c:choose>
                <c:when test="${usuarioLogado.tipo == 'ADMIN'}">Admin Panel</c:when>
                <c:otherwise>Bibliotecário Panel</c:otherwise>
            </c:choose>
        </h5>
        <hr>
    </div>

    <%-- Menu comum a ambos --%>
    <a href="${pageContext.request.contextPath}/admin">📊 Dashboard</a>
    <a href="${pageContext.request.contextPath}/dashboard">📖 Catálogo</a>
    <a href="#">📅 Empréstimos Ativos</a>

    <%-- Menu exclusivo do Admin --%>
    <c:if test="${usuarioLogado.tipo == 'ADMIN'}">
        <a href="#">👥 Gerenciar Usuários</a>
        <a href="#">📋 Relatórios Globais</a>
        <a href="#">👨‍💼 Cadastrar Bibliotecário</a>
    </c:if>

    <%-- Menu exclusivo do Bibliotecário --%>
    <c:if test="${usuarioLogado.tipo == 'BIBLIOTECARIO'}">
        <a href="#">👥 Usuários da Unidade</a>
        <a href="#">📦 Estoque da Unidade</a>
        <a href="#">🔖 Reservas Planejadas</a>
    </c:if>

    <a href="${pageContext.request.contextPath}/logout"
       class="text-danger mt-5">🚪 Sair</a>
</div>

<div class="main-content">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>
            Bem-vindo,
            <c:choose>
                <c:when test="${usuarioLogado.tipo == 'ADMIN'}">
                    Admin: ${usuarioLogado.nome}
                </c:when>
                <c:otherwise>
                    Bibliotecário: ${usuarioLogado.nome}
                </c:otherwise>
            </c:choose>
        </h2>

        <%-- Botão cadastrar livro — ambos podem --%>
        <a href="#" class="btn btn-primary">+ Cadastrar Novo Livro</a>
    </div>

    <%-- Cards de estatísticas --%>
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card p-3 shadow-sm stats-card">
                <h6>Total de Livros</h6>
                <h3>${livros.size()}</h3>
            </div>
        </div>

        <%-- Card extra para Admin --%>
        <c:if test="${usuarioLogado.tipo == 'ADMIN'}">
            <div class="col-md-4">
                <div class="card p-3 shadow-sm"
                     style="border-left: 5px solid #28a745;">
                    <h6>Total de Usuários</h6>
                    <h3>${totalUsuarios != null ? totalUsuarios : '-'}</h3>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card p-3 shadow-sm"
                     style="border-left: 5px solid #dc3545;">
                    <h6>Empréstimos Atrasados</h6>
                    <h3>${totalAtrasados != null ? totalAtrasados : '-'}</h3>
                </div>
            </div>
        </c:if>

        <%-- Card extra para Bibliotecário --%>
        <c:if test="${usuarioLogado.tipo == 'BIBLIOTECARIO'}">
            <div class="col-md-4">
                <div class="card p-3 shadow-sm"
                     style="border-left: 5px solid #ffc107;">
                    <h6>Empréstimos Ativos</h6>
                    <h3>${totalAtivos != null ? totalAtivos : '-'}</h3>
                </div>
            </div>
        </c:if>
    </div>

    <%-- Tabela de livros --%>
    <div class="card shadow-sm">
        <div class="card-header bg-white">
            <strong>Gerenciamento de Acervo</strong>
        </div>
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
                            <%-- Excluir só para Admin --%>
                            <c:if test="${usuarioLogado.tipo == 'ADMIN'}">
                                <button class="btn btn-sm btn-danger">Excluir</button>
                            </c:if>
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