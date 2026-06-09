<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca Digital - Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f4f4; margin-top: 56px; }
        .topbar { background-color: #40c4d4; padding: 10px 20px; display: flex; align-items: center; justify-content: space-between; color: white; position: fixed; top: 0; left: 0; right: 0; z-index: 1000; height: 56px; }
        .search-box { width: 220px; }
        .sidebar { width: 220px; height: 100vh; background: #fff; border-right: 1px solid #ccc; position: fixed; top: 56px; padding-top: 20px; overflow-y: auto; }
        .sidebar a { display: block; padding: 15px; color: #333; text-decoration: none; border-bottom: 1px solid #ddd; }
        .sidebar a:hover { background-color: #f0f0f0; color: #40c4d4; font-weight: bold; }
        .sidebar a.active { background-color: #e8f7f8; color: #40c4d4; font-weight: bold; border-left: 4px solid #40c4d4; }
        .sidebar a.link-home { color: #40c4d4; font-size: 13px; }
        .content { margin-left: 240px; padding: 30px; }
        .profile-box { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); }
        .card-img-top { height: 200px; object-fit: cover; }
        .book-card { transition: transform 0.2s; border: none; }
        .book-card:hover { transform: scale(1.03); }
        @media (max-width: 1024px) { .sidebar { width: 180px; } .content { margin-left: 195px; } }
        @media (max-width: 768px) {
            body { margin-top: 0 !important; }
            .topbar { flex-wrap: wrap !important; height: auto !important; gap: 8px; padding: 8px !important; }
            .sidebar { width: 100%; height: auto; position: relative; top: 0; display: flex; flex-wrap: wrap; border-right: none; border-bottom: 1px solid #ccc; }
            .sidebar a { padding: 8px 12px; font-size: 13px; border-bottom: none; border-right: 1px solid #ddd; }
            .content { margin-left: 0; padding: 15px; }
        }
        @media (max-width: 480px) { .sidebar a { width: 100%; font-size: 12px; } }
    </style>
</head>
<body>

<div class="topbar">
    <div><strong>📚 Library Digital</strong></div>
    <form class="d-flex gap-2" action="${pageContext.request.contextPath}/dashboard" method="get">
        <input class="form-control search-box" type="search" name="txtBusca"
               value="${termoPesquisado}" placeholder="Buscar livro, autor...">
        <select name="filtro" class="form-select" style="width: 120px;">
            <option value="">Todos</option>
            <option value="titulo"  ${filtroAtivo == 'titulo'  ? 'selected' : ''}>Título</option>
            <option value="autor"   ${filtroAtivo == 'autor'   ? 'selected' : ''}>Autor</option>
            <option value="genero"  ${filtroAtivo == 'genero'  ? 'selected' : ''}>Gênero</option>
        </select>
        <button class="btn btn-light" type="submit">🔍</button>
    </form>
    <div>
        <span class="me-3"><strong>${usuarioLogado.nome}</strong></span>
        <a href="${pageContext.request.contextPath}/meus-emprestimos" class="btn btn-outline-light btn-sm me-2">Meus Empréstimos</a>
        <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Sair</a>
    </div>
</div>

<div class="sidebar">
    <a href="${pageContext.request.contextPath}/dashboard" class="active">🏠 Catálogo</a>
    <a href="${pageContext.request.contextPath}/perfil">👤 Meu Perfil</a>
    <a href="${pageContext.request.contextPath}/meus-emprestimos">📖 Meus Empréstimos</a>
    <a href="${pageContext.request.contextPath}/minhas-reservas">🔖 Minhas Reservas</a>
    <a href="${pageContext.request.contextPath}/multas">💰 Multas</a>
    <a href="${pageContext.request.contextPath}/home" class="link-home">🌐 Página Inicial</a>
    <a href="${pageContext.request.contextPath}/logout" style="color: #dc3545;">🚪 Sair</a>
</div>

<div class="content">
    <div class="profile-box">
        <div>
            <h3>Bem Vindo, ${not empty usuarioLogado.nome ? usuarioLogado.nome : usuarioLogado.email}</h3>
            <p class="text-muted mb-0">Perfil: ${usuarioLogado.tipo}</p>
        </div>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0">
            <c:choose>
                <c:when test="${not empty termoPesquisado}">
                    🔎 Resultados para: "${termoPesquisado}"
                    <small class="text-muted fs-6">(${livros.size()} livro(s))</small>
                </c:when>
                <c:otherwise>📖 Catálogo de Livros</c:otherwise>
            </c:choose>
        </h4>
        <c:if test="${not empty termoPesquisado}">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary btn-sm">✕ Limpar busca</a>
        </c:if>
    </div>

    <div class="row">
        <c:forEach var="livro" items="${livros}">
            <div class="col-md-3 mb-4">
                <div class="card h-100 book-card shadow-sm">
                    <%-- ✅ Fallback: banco → URL → placeholder --%>
                    <c:choose>
                        <c:when test="${livro.temCapaImagem()}">
                            <img src="${pageContext.request.contextPath}/capa?id=${livro.id}"
                                 class="card-img-top" alt="${livro.titulo}"
                                 onerror="this.src='${pageContext.request.contextPath}/images/sem-capa.svg'">
                        </c:when>
                        <c:otherwise>
                            <img src="${not empty livro.capaUrl ? livro.capaUrl : ''}"
                                 class="card-img-top" alt="${livro.titulo}"
                                 onerror="this.src='${pageContext.request.contextPath}/images/sem-capa.svg'">
                        </c:otherwise>
                    </c:choose>
                    <div class="card-body">
                        <h6 class="card-title text-truncate">${livro.titulo}</h6>
                        <p class="card-text small text-muted">${livro.autor}</p>
                    </div>
                    <div class="card-footer bg-transparent border-top-0">
                        <a href="${pageContext.request.contextPath}/detalhes?id=${livro.id}"
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
                    <a href="${pageContext.request.contextPath}/dashboard" class="alert-link text-info">
                        Clique aqui para ver todos os livros.
                    </a>
                </div>
            </div>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
