<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Busca - Library Digital</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f4f4; margin-top: 56px; }
        .topbar { background-color: #40c4d4; padding: 10px 20px; display: flex; align-items: center; justify-content: space-between; color: white; position: fixed; top: 0; left: 0; right: 0; z-index: 1000; height: 56px; }
        .search-box { width: 300px; }
        .sidebar { width: 220px; height: 100vh; background: #fff; border-right: 1px solid #ccc; position: fixed; top: 56px; padding-top: 20px; }
        .sidebar a { display: block; padding: 15px; color: #333; text-decoration: none; border-bottom: 1px solid #ddd; }
        .sidebar a:hover { background-color: #f0f0f0; color: #40c4d4; font-weight: bold; }
        .sidebar a.active { background-color: #e8f7f8; color: #40c4d4; font-weight: bold; border-left: 4px solid #40c4d4; }
        .content { margin-left: 240px; padding: 30px; }
        .card-img-top { height: 200px; object-fit: cover; }
        .book-card { transition: transform 0.2s; border: none; }
        .book-card:hover { transform: scale(1.03); }
    </style>
</head>
<body>

<div class="topbar">
    <div><strong>📚 Library Digital</strong></div>
    <form class="d-flex" action="${pageContext.request.contextPath}/buscar" method="get">
        <input class="form-control me-2 search-box" type="search" name="txtBusca"
               value="${termoPesquisado}" placeholder="Procurar Livro, Autor ou Gênero">
        <button class="btn btn-light" type="submit">🔍</button>
    </form>
    <div>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm me-2">Entrar</a>
        <a href="${pageContext.request.contextPath}/cadastro" class="btn btn-light btn-sm">Cadastrar-se</a>
    </div>
</div>

<div class="sidebar">
    <a href="${pageContext.request.contextPath}/buscar?txtBusca=${termoPesquisado}&filtro=${filtro}" class="active">🔎 Catálogo</a>
    <a href="${pageContext.request.contextPath}/home">🌐 Página Inicial</a>
</div>

<div class="content">
    <h4 class="mb-4">
        <c:choose>
            <c:when test="${not empty termoPesquisado}">🔎 Resultados para: "${termoPesquisado}"</c:when>
            <c:otherwise>📖 Catálogo de Livros</c:otherwise>
        </c:choose>
    </h4>

    <div class="row">
        <c:forEach var="livro" items="${livros}">
            <div class="col-md-3 mb-4">
                <div class="card h-100 book-card shadow-sm">
                    <img src="${livro.temCapaImagem() ? pageContext.request.contextPath.concat('/capa?id=').concat(String.valueOf(livro.id)) : (not empty livro.capaUrl ? livro.capaUrl : '')}"
                         class="card-img-top" alt="${livro.titulo}"
                         onerror="this.src='${pageContext.request.contextPath}/images/sem-capa.svg'">
                    <div class="card-body">
                        <h6 class="card-title text-truncate">${livro.titulo}</h6>
                        <p class="card-text small text-muted">${livro.autor}</p>
                    </div>
                    <div class="card-footer bg-transparent border-top-0">
                        <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}&origem=buscar&txtBusca=${termoPesquisado}&filtro=${filtro}"
                           class="btn btn-info text-white btn-sm w-100">Ver Detalhes</a>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty livros}">
            <div class="col-12">
                <div class="alert alert-light border shadow-sm mt-3">
                    Nenhum livro encontrado para "<strong>${termoPesquisado}</strong>".
                    <br>
                    <a href="${pageContext.request.contextPath}/home" class="alert-link text-info">Voltar para a Página Inicial</a>
                </div>
            </div>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
